# camel-quarkus-ws-to-event-artemis Project

This project uses Quarkus, the Supersonic Subatomic Java Framework. This is a project for a proof of concept using a websocket connection for an AMQ Broker(Apache Artemis) queue. This module is a producer that produce messages on AMQ Broker topic.

## Deploy on Openshift

* Create a namespace
  
  ```shell script
  oc new-project <your namespace>
  ```
* Install AMQ Broker(Artemis) Operator
  
  ![](/images/InstallAMQBrokerOperator.png)

* Deploy AMQ Broker(Artemis)
  
  ![](/images/DeployAMQBrokerCluster.png)

* Create Topics raw and processed automatically when you publish first messages

* Deploy Camel Quarkus APP
  
  ```shell script
  ./mvnw clean package -Dquarkus.kubernetes.deploy=true
  ```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/camel-ws-to-event-artemis-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- OpenShift ([guide](https://quarkus.io/guides/deploying-to-openshift)): Generate OpenShift resources from annotations
- Camel Core ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-core)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Ref, Simple and Tokenize
- Camel JAXB ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jaxb.html)): Unmarshal XML payloads to POJOs and back using JAXB2 XML marshalling standard
- Artemis JMS ([guide](https://quarkus.io/guides/jms)): Use JMS APIs to connect to ActiveMQ Artemis via its native protocol
- Camel ActiveMQ ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/activemq.html)): Send messages to (or consume from) Apache ActiveMQ. This component extends the Camel JMS component

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
