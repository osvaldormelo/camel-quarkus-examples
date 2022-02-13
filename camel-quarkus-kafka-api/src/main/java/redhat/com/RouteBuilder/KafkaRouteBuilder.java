package redhat.com.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;

import redhat.com.models.Product;

import org.apache.camel.LoggingLevel;



public class KafkaRouteBuilder extends RouteBuilder{
    protected String TOPIC = "{{kafka.topic}}";
    protected String BOOTSTRAP_SERVERS = "{{kafka.bootstrap.servers}}";
    @Override
    public void configure() throws Exception {
        
        //REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.json)
        	.component("platform-http")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("/").port(8080)
			.apiContextPath("/openapi")
			.apiProperty("api.title", "Camel Quarkus Kafka API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true");
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus").produces("application/json") 
        .post("/products")
                .type(Product.class)
				.description("Send product to kafka")
				.route().routeId("restproductsend") 
				.to("direct:sendToKafka")
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
				.endRest()
        ;
        //Route that sends message to kafka topic
        from("direct:sendToKafka").routeId("sendToKafka")
        .setBody(constant("Message from Camel"))          // Message to send
        .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
        .to("kafka:"+ TOPIC + "?brokers=" + BOOTSTRAP_SERVERS);
    }
}