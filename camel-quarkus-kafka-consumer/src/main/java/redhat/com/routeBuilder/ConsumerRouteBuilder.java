package redhat.com.routeBuilder;


import org.apache.camel.component.mongodb.MongoDbConstants;

import com.mongodb.client.model.Filters;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import redhat.com.models.Product;
import org.apache.camel.LoggingLevel;
import org.bson.types.ObjectId;


public class ConsumerRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{kafka.topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{kafka.bootstrap.servers}}";
    protected String MONGO_DB_HOST = "{{mongo.db.host}}";
    protected String MONGO_DB_DATABASE = "{{mongo.db.database}}";
    protected String MONGO_DB_COLLECTION = "{{mongo.db.collection}}";

    @Override
    public void configure() throws Exception {
        
               
        //Route that consumes message to kafka topic
        from("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS)
        .routeId("kafkaConsumer")
        .setHeader(KafkaConstants.KEY, constant("Camel"))
        .convertBodyTo(Product.class)
        .log("Message received from Kafka : ${body}")
        .marshal().json()   // marshall message
        .to("direct:insertMongoDb")
        ;

        //Route insert object on mongoDB
        from("direct:insertMongoDb").routeId("insertMongoDb")        
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=insert")
        
        ;
    }
}