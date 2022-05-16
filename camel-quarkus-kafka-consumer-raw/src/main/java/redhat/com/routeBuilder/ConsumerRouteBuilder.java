//camel-k: language=java dependency=mvn:org.apache.camel.quarkus:camel-quarkus-kafka dependency=mvn:io.strimzi:kafka-client:0.7.1.redhat-00003
package redhat.com.routeBuilder;

import com.mongodb.client.model.Filters;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.models.Message;
import org.apache.camel.LoggingLevel;
import org.bson.types.ObjectId;


public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC_RAW = "{{quarkus.openshift.env.vars.kafka-topic-raw}}";
    protected String KAFKA_TOPIC_PROCESSED = "{{quarkus.openshift.env.vars.kafka-topic-processed}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";
    protected String KAFKA_GROUP_ID = "{{quarkus.openshift.env.vars.kafka-group-id}}";
    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC_RAW + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS + "&groupId=" + KAFKA_GROUP_ID)
        .routeId("kafkaConsumerRawTopic")
        .unmarshal(new JacksonDataFormat(Message.class))
        .log("Message received from Kafka Topic raw : ${body}")
        .to("direct:insertProcessedTopic")
        ;

        //Route insert object on Processed Topic
        from("direct:insertProcessedTopic")
        .routeId("insertProcessedTopic")        
        .marshal().json()   // marshall message to send to kafka
        .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
        .to("kafka:"+ KAFKA_TOPIC_PROCESSED + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS)
        .log("Message send from Kafka Topic processed : ${body}")
        ;
    }
}