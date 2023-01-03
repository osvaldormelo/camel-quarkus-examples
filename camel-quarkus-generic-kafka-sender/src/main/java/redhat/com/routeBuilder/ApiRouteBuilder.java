package redhat.com.routeBuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.component.kafka.KafkaConstants;
public class ApiRouteBuilder extends RouteBuilder {
    protected String KAFKA_TOPIC = "{{quarkus.openshift.env.vars.kafka-topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";

    @Override
    public void configure() throws Exception {
        // REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.auto)
                .component("platform-http")
                .dataFormatProperty("prettyPrint", "true")
                .contextPath("/").port(8080)
                .apiContextPath("/openapi")
                .apiProperty("api.title", "Camel Quarkus Generic Kafka API Demo")
                .apiProperty("api.version", "1.0.0-SNAPSHOT")
                .apiProperty("cors", "true");

        // REST methods configuration
        rest().tag("API Demo using Camel and Quarkus")
                .produces("application/json")
                .post("/payloads")
                .consumes("application/json")
                .description("Send payload to kafka")
                .route().routeId("postPayload")                
                .to("direct:sendToKafka")
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                .endRest();
        //Route that sends message to kafka topic
        from("direct:sendToKafka").routeId("sendToKafka")
        .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
        .to("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS)
        .setBody(simple("Message Sended!"));
        ;
    }
}
