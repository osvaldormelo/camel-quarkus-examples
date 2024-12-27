package com.example;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jws.WebService;
import br.com.solarbr.opshml.apps.kafka_soap_api_dev_ri_0_solar_core_apis_dev.kafka.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Logger;
import java.util.Map;

@ApplicationScoped
@WebService(endpointInterface = "br.com.solarbr.opshml.apps.kafka_soap_api_dev_ri_0_solar_core_apis_dev.kafka.KafkaPortType", targetNamespace = "http://kafka-soap-api-dev-ri-0-solar-core-apis-dev.apps.opshml.solarbr.com.br/kafka", serviceName = "BoletoServiceService", portName = "SendBoletoPort")
public class BoletoService extends RouteBuilder implements KafkaPortType {

    private static final Logger logger = Logger.getLogger(BoletoService.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /// Estou carregando a partir do Application.properties as Variaveis de uso avançado do Kafka caso precisem:
    protected String KAFKA_PIX_TOPIC = "{{quarkus.openshift.env.vars.kafka-pix-topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";
    protected String KAFKA_PRODUCER_COMPRESSION_CODEC = "{{quarkus.openshift.env.vars.kafka-producer-compression-codec}}";
    protected String KAFKA_PRODUCER_REQUIRED_ACKS = "{{quarkus.openshift.env.vars.kafka-producer-required-acks}}";
    protected String KAFKA_PRODUCER_BUFFER_MEMORY_SIZE = "{{quarkus.openshift.env.vars.kafka-producer-buffer-memory-size}}";
    protected String KAFKA_PRODUCER_LINGER_MS = "{{quarkus.openshift.env.vars.kafka-producer-linger-ms}}";
    protected String KAFKA_PRODUCER_BATCH_SIZE = "{{quarkus.openshift.env.vars.kafka-producer-batch-size}}";
    protected String KAFKA_PRODUCER_SECURITY_PROTOCOL = "{{quarkus.openshift.env.vars.kafka-producer-security-protocol}}";
    protected String KAFKA_PRODUCER_TRUSTSTORE_LOCATION = "{{quarkus.openshift.env.vars.kafka-producer-ssl-truststore-location}}";
    protected String KAFKA_PRODUCER_TRUSTSTORE_PASSWORD = "{{quarkus.openshift.env.vars.kafka-producer-ssl-truststore-password}}";
    protected String KAFKA_PRODUCER_SASL_MECHANISM = "{{quarkus.openshift.env.vars.kafka-producer-sasl-mechanism}}";
    protected String KAFKA_PRODUCER_JAAS_CONFIG = "{{quarkus.openshift.env.vars.kafka-producer-sasl-jaas-config}}";
    ///
    /// Aqui é o método SendBoleto que recebe o BoletoRequest e envia para o Kafka que está definido no wsdl
    /// 
    @Override
    public PixResponse sendBoleto(BoletoRequest request) {
        logger.info("Recebido boleto: " + request);

        try {
            // Converte o boleto para JSON
            String jsonBoleto = objectMapper.writeValueAsString(request.getBoleto());
            // Envia para a rota Camel com tópico e chave dinâmicos
            getCamelContext().createProducerTemplate().sendBodyAndHeaders(
                    "direct:kafkaRouteBoleto",
                    jsonBoleto,
                    Map.of(
                    "topic", request.getTopic().toString(),        
                    KafkaConstants.KEY, request.getKey()));
        } catch (Exception e) {
            logger.severe("Erro ao processar boleto: " + e.getMessage());
        }

        // Responde ao cliente SOAP
        PixResponse response = new PixResponse();
        response.setStatus("Boleto recebido com SUCESSO");
        return response;
    }
    ///
    /// Aqui é o método sendPix que recebe o PixRequest e envia para o Kafka que está definido no wsdl
    /// 
    @Override
    public PixResponse sendPix(PixRequest request) {
        logger.info("Recebido Pix: " + request);

        getCamelContext().createProducerTemplate().sendBody("direct:kafkaRoutePix", request.getNumero().toString());

        // Responde ao cliente SOAP
        PixResponse response = new PixResponse();
        response.setStatus("PIX recebido com SUCESSO");
        return response;
    }
    ///
    /// Aqui começa o camel propriamente dito, onde temos as rotas para o Kafka
    /// 
    @Override
    public void configure() throws Exception {
        // Configuração do Kafka como destino dos boletos
        from("direct:kafkaRouteBoleto").routeId("kafkaRouteBoleto")
                .process(new Processor() {
                    public void process(Exchange exchange) {
                        
                        
                        String key = exchange.getIn().getHeader(KafkaConstants.KEY, String.class);
                        String topic = exchange.getIn().getHeader("topic", String.class);
                        // Configura a chave dinamica
                        exchange.getIn().setHeader(KafkaConstants.KEY, key);
                        exchange.getIn().setHeader("CamelKafkaTopic", topic);                        
                    }
                })
                // Usa roteamento dinâmico baseado nos cabeçalhos configurados acima
                .toD("kafka:${header.CamelKafkaTopic}?brokers=" + KAFKA_BOOTSTRAP_SERVERS)// Rota dinâmica baseada no tópico do XML, se não achar manda para o default definido no application.properties
                .log("Enviado para Kafka no tópico \"${header.CamelKafkaTopic}\" com o body: \n${body}");

        // Configuração do Kafka como destino dos PIX
        from("direct:kafkaRoutePix").routeId("kafkaRoutePix")
            .process(new Processor() {
                public void process(Exchange exchange) {
                    // Configura chave para as mensagens PIX, pode ser comentado se quiserem usar a chave default
                    exchange.getIn().setHeader(KafkaConstants.KEY, "PIXKey");
                }
            })
            .to("kafka:"+ KAFKA_PIX_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS)
            .log("Enviado para Kafka: ${body}");
    }
}
