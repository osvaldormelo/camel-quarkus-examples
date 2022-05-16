package redhat.com.routeBuilder;


import org.apache.camel.component.mongodb.MongoDbConstants;

import com.mongodb.client.model.Filters;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.models.Product;
import org.apache.camel.LoggingLevel;
import org.bson.types.ObjectId;


public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{quarkus.openshift.env.vars.kafka-topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";
    protected String KAFKA_GROUP_ID = "{{quarkus.openshift.env.vars.kafka-group-id}}";
    protected String MONGO_DB_HOST = "{{quarkus.openshift.env.vars.mongo-db-host}}";
    protected String MONGO_DB_DATABASE = "{{quarkus.openshift.env.vars.mongo-db-database}}";
    protected String MONGO_DB_COLLECTION = "{{quarkus.openshift.env.vars.mongo-db-collection}}";
    protected String MONGO_DB_USERNAME = "{{quarkus.openshift.env.vars.mongo-db-username}}";
    protected String MONGO_DB_PASSWORD = "{{quarkus.openshift.env.vars.mongo-db-password}}";
    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS + "&groupId=" + KAFKA_GROUP_ID)
        .routeId("kafkaConsumer")
        .unmarshal(new JacksonDataFormat(Product.class))
        .log("Message received from Kafka : ${body}")
        .to("direct:insertMongoDb")
        ;

        //Route insert object on mongoDB
        from("direct:insertMongoDb").routeId("insertMongoDb")        
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&username="+MONGO_DB_USERNAME+"&password="+MONGO_DB_PASSWORD+"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=insert")
        
        ;
    }
}