// camel-k: language=java dependency=mvn:org.apache.camel.quarkus:camel-quarkus-kafka
package com.redhat.routes;

import org.apache.camel.builder.RouteBuilder;
import com.redhat.models.Message;
import org.apache.camel.component.jackson.JacksonDataFormat;

public class ChatControllerRoute extends RouteBuilder{

    protected String KAFKA_TOPIC_RAW = "{{quarkus.openshift.env.vars.kafka-topic-raw}}";
    protected String KAFKA_TOPIC_PROCESSED = "{{quarkus.openshift.env.vars.kafka-topic-processed}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";
    protected String KAFKA_GROUP_ID = "{{quarkus.openshift.env.vars.kafka-group-id}}";
    protected String ARTEMIS_DESTINATION_NAME_RAW = "{{quarkus.openshift.env.vars.quarkus-artemis-destinationName-raw}}";

    @Override
    public void configure() throws Exception {
        
        from("vertx-websocket://echo")
            .log(">>> Message received from WebSocket Client : ${body} - ${headers}")
            .unmarshal(new JacksonDataFormat(Message.class))
            //.wireTap("direct:send-to-artemis")
        .to("direct:send-to-kafka");

        from("direct:send-to-kafka").routeId("send-to-kafka")
            .marshal().json()   // marshall message to send to kafka
            //.setHeader("kafka.KEY", constant("Camel")) // Key of the message
        .to("kafka:"+KAFKA_TOPIC_RAW+"?brokers="+KAFKA_BOOTSTRAP_SERVERS + "&requestRequiredAcks=0");

        /*from("direct:send-to-artemis")
            .routeId("send-to-artemis")
            .marshal().json()   // marshall message to send to artemis
            .log(">>> Message sended to Artemis : ${body} - ${headers}")            
        .to("jms:topic:"+ ARTEMIS_DESTINATION_NAME_RAW);*/
             

        //Route that consumes message to kafka topic
        // from("kafka:"+KAFKA_TOPIC_RAW+"?brokers="+KAFKA_BOOTSTRAP_SERVERS+"&groupId=KAFKA_GROUP_ID")
        //     .routeId("kafka-consumer-raw")
        //     .unmarshal(new JacksonDataFormat(Message.class))
        //     .log("Message received from Kafka Topic raw : ${body}");

    }
    
}
