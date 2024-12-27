# camel-quarkus-soap-kafka

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

---

This example originated from the need to use a SOAP service in Camel to replace a legacy SOAP service with minimal impact on existing applications. Therefore, we used the same WSDL that already existed, the invoice.wsdl.

---

## **1. Prerequisites**

1. **Podman or Docker installed** - To run Kafka and Kafka UI via containers.
2. **Java 17+ installed** - Required for Quarkus.
3. **Maven 3.8+ installed** - For project dependency and build management.
4. **Access to code.quarkus.redhat.com** - For project scaffolding.

---

## **2. Setting Up Kafka and Kafka UI**

Use **Podman Compose** to set up Kafka and Kafka UI locally.

### **podman-compose.yml** (already in the project)
```yaml
version: '3.7'

services:
  zookeeper:
    image: quay.io/debezium/zookeeper:1.9
    ports:
      - "2181:2181"
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181

  kafka:
    image: quay.io/debezium/kafka:1.9
    ports:
      - "9092:9092"
    environment:
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:9092,PLAINTEXT_HOST://localhost:9092
      KAFKA_LISTENERS: PLAINTEXT://0.0.0.0:9092,PLAINTEXT_HOST://0.0.0.0:9093
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_AUTO_CREATE_TOPICS_ENABLE: "true"

  kafka-ui:
    image: provectuslabs/kafka-ui:latest
    ports:
      - "8080:8080"
    environment:
      KAFKA_CLUSTERS_0_NAME: local
      KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS: kafka:9092
      KAFKA_CLUSTERS_0_ZOOKEEPER: zookeeper:2181
```

Steps to Run Kafka:
```shell
podman-compose up -d
```
Access Kafka UI at <http://localhost:8080>.

---

## **3. Creating the Quarkus Project**

Go to <https://code.quarkus.redhat.com> and follow these steps:
1. Select the following extensions:
   - Apache Camel CXF SOAP - To handle SOAP requests.
   - Apache Camel Kafka - For integration with Kafka.
   - Apache Camel Core - Required for routing.
2. Download the project and import it into your IDE.

---

## **4. Generating Classes from WSDL**

Place your `invoice.wsdl` in the `src/main/resources` folder.

Run the following command to generate Java classes from the WSDL file:
```shell
mvn org.apache.cxf:cxf-codegen-plugin:wsdl2java
```

> **_NOTE:_** You may need to rename the generated packages from `javax` to `jakarta` in some cases.

---

## **5. Configuring application.properties**

`application.properties` - Configuration explained:
```properties
# Server Port
quarkus.http.port=8083

# CXF SOAP Configurations
quarkus.cxf.path=/boleto
quarkus.cxf.endpoint-implementor=com.example.BoletoService
quarkus.cxf.endpoint-name=BoletoServiceService

# WSDL Path
quarkus.cxf.wsdl-path=invoice.wsdl

# Kafka Configuration
quarkus.openshift.env.vars.kafka-bootstrap-servers=${KAFKA_BOOTSTRAP_SERVERS:kafka:9092}
quarkus.openshift.env.vars.kafka-boleto-topic=${KAFKA_BOLETO_TOPIC:boletos}
quarkus.openshift.env.vars.kafka-pix-topic=${KAFKA_PIX_TOPIC:pix}

# Producer Configurations
quarkus.openshift.env.vars.kafka-producer-compression-codec=none
quarkus.openshift.env.vars.kafka-producer-required-acks=0
quarkus.openshift.env.vars.kafka-producer-buffer-memory-size=33554432
quarkus.openshift.env.vars.kafka-producer-linger-ms=1
quarkus.openshift.env.vars.kafka-producer-batch-size=16384

# Security Configurations
quarkus.openshift.env.vars.kafka-producer-security-protocol=SASL_SSL
quarkus.openshift.env.vars.kafka-producer-ssl-truststore-location=/etc/security/truststore/truststore.jks
quarkus.openshift.env.vars.kafka-producer-ssl-truststore-password=changeit
quarkus.openshift.env.vars.kafka-producer-sasl-mechanism=SCRAM-SHA-512
quarkus.openshift.env.vars.kafka-producer-sasl-jaas-config=org.apache.kafka.common.security.scram.ScramLoginModule required username=admin password=admin-secret;

# Dev Mode
quarkus.devservices.enabled=true
```

Key Parameters Explained:
1. **Kafka Topics** - `quarkus.openshift.env.vars.kafka-boleto-topic` and `kafka-pix-topic` define default topics for sending messages.
2. **Bootstrap Servers** - Configures Kafka server address. Replace `localhost:9092` if running remotely.
3. **Security Settings** - Enable SSL/TLS and SASL authentication for secure Kafka communication.
4. **Dev Mode** - Allows live reload for faster development.

---

## **6. Running the Application**

Start in Development Mode:
```shell
./mvnw compile quarkus:dev
```

### **Send SOAP Requests**

Use tools like Postman or SoapUI to send SOAP requests to the endpoint:

**URL:** `http://localhost:8083/boleto`

**Sample Request (XML):**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:kaf="http://kafka-soap-api-dev-ri-0-solar-core-apis-dev.apps.opshml.solarbr.com.br/kafka">
   <soapenv:Header/>
   <soapenv:Body>
      <kaf:BoletoRequest>
         <topic>boletos</topic>
         <key>12345</key>
         <boleto>
            <NRONOTAFISCAL>123</NRONOTAFISCAL>
            <STATUS>ACTIVE</STATUS>
            <DATAEMISSAO>2024-01-01</DATAEMISSAO>
            <VALORTOTAL>100.50</VALORTOTAL>
            <VENCIMENTO>2024-02-01</VENCIMENTO>
            <VENCIMENTOREAL>2024-02-01</VENCIMENTOREAL>
            <NUMERODOCUMENTO>123456</NUMERODOCUMENTO>
            <VALORBOLETO>100.50</VALORBOLETO>
            <CODIGOBARRA>12345678901234567890</CODIGOBARRA>
            <PIXCOPIAECOLA>paymentString</PIXCOPIAECOLA>
         </boleto>
      </kaf:BoletoRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

### **Confirm WSDL**

Verify the WSDL is correctly exposed by accessing:
```shell
http://localhost:8083/boleto/BoletoServiceService?wsdl
```

---

## **7. Observing Logs and Messages**

1. **View Logs** - Check terminal logs for message status and errors.
2. **Kafka UI** - Monitor Kafka topics and messages at <http://localhost:8080>.

---

## **8. Building and Deploying to OpenShift**

1. **Build Image:**
   ```shell
   ./mvnw package -Dquarkus.container-image.build=true
   ```
2. **Deploy:**
   ```shell
   ./mvnw package -Dquarkus.kubernetes.deploy=true
   ```

---

## **9. Additional References**

- [Apache Camel Kafka Documentation](https://camel.apache.org/components/latest/kafka-component.html)
- [Apache Camel CXF Documentation](https://camel.apache.org/components/latest/cxf-component.html)
- [Quarkus Guides](https://quarkus.io/guides/)
