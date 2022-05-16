package com.redhat.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import com.redhat.models.Message;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class ChatControllerRoute extends RouteBuilder{
    
    protected String ARTEMIS_DESTINATION_NAME_RAW = "{{quarkus.openshift.env.vars.quarkus-artemis-destinationName-raw}}";
    @Override
    public void configure() throws Exception {
        
        
        from("vertx-websocket://echo")
            .log(">>> Message received from WebSocket Client : ${body} - ${headers}")
            .routeId("artemisConsumerRawTopic")
            .unmarshal(new JacksonDataFormat(Message.class))
        .to("direct:send-to-artemis");

        from("direct:send-to-artemis")
            .routeId("send-to-artemis")
            .marshal().json()   // marshall message to send to artemis            
            .to("jms:topic:"+ ARTEMIS_DESTINATION_NAME_RAW)
            .log(">>> Message sended to Artemis : ${body} - ${headers}");                  

    }
    
}
