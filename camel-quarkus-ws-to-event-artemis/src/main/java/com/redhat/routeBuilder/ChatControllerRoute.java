package com.redhat.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.models.Message;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.paho.PahoConstants;

public class ChatControllerRoute extends RouteBuilder{
    
    protected String ARTEMIS_DESTINATION_NAME_RAW = "{{quarkus.openshift.env.vars.quarkus-artemis-destinationName-raw}}";
    @Override
    public void configure() throws Exception {
        
        
        //REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.json)
        	.component("platform-http")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("/").port(8080)
			.apiContextPath("/openapi")
			.apiProperty("api.title", "Camel Quarkus Artemis API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true")            
            ;
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus with API or Infinispan Cache")
        .produces("application/json")
        .post("/messages")
                .consumes("application/json") 
                .type(Message.class)
				.description("Send Message to Artemis")
				.route().routeId("postMessagetoArtemis")
                .convertBodyTo(Message.class)
                .to("direct:send-to-artemis")                   
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))                     
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))                    
                .endRest()
        ;
        from("vertx-websocket://echo")
           // .log(">>> Message received from WebSocket Client : ${body} - ${headers}")
            .routeId("artemisConsumerRawTopic")
            .unmarshal(new JacksonDataFormat(Message.class))
        .to("direct:send-to-artemis");

        from("direct:send-to-artemis")
            .routeId("send-to-artemis")
            .marshal().json()   // marshall message to send to artemis            
            //.setExchangePattern(ExchangePattern.InOnly)
            //.to("activemq:topic:" + ARTEMIS_DESTINATION_NAME_RAW + "?deliveryMode=1&deliveryPersistent=false&requestTimeout=120000")
            //.to("activemq:topic:" + ARTEMIS_DESTINATION_NAME_RAW + "?artemisStreamingEnabled=true&disableReplyTo=true")
            .setHeader(PahoConstants.CAMEL_PAHO_OVERRIDE_TOPIC, simple(ARTEMIS_DESTINATION_NAME_RAW))            
            .to("paho-mqtt5:" + ARTEMIS_DESTINATION_NAME_RAW)
            .log(">>> Message sended to Artemis : ${body} - ${headers}");                  

    }
    
}
