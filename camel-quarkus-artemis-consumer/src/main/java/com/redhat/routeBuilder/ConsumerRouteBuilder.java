package com.redhat.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import com.redhat.models.Message;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class ConsumerRouteBuilder extends RouteBuilder{
    protected String ARTEMIS_DESTINATION_NAME_PROCESSED = "{{quarkus.openshift.env.vars.quarkus-artemis-destinationName-processed}}";
    protected String ARTEMIS_DESTINATION_NAME_RAW = "{{quarkus.openshift.env.vars.quarkus-artemis-destinationName-raw}}";
    @Override
    public void configure() throws Exception {
        
        
        from("jms:topic:"+ ARTEMIS_DESTINATION_NAME_RAW)
        .routeId("artemisConsumerRawTopic")
        .unmarshal(new JacksonDataFormat(Message.class))
        .log("Message received from Artemis Raw Topic : ${body} - ${headers}")
        .to("direct:insertProcessedTopic");           
        
        //Route insert object on Processed Topic
        from("direct:insertProcessedTopic")
        .routeId("insertProcessedTopic")        
        .marshal().json()   // marshall message to send to artemis        
        .to("jms:topic:"+ ARTEMIS_DESTINATION_NAME_PROCESSED)
        .log("Message send to Artemis Processed Topic: ${body}")
        ;
    }
    
}
