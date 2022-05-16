package redhat.com.routeBuilder;


import org.apache.camel.component.mongodb.MongoDbConstants;
import com.mongodb.client.model.Filters;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.models.Product;
import org.apache.camel.LoggingLevel;
import org.bson.types.ObjectId;


public class ApiRouteBuilder extends RouteBuilder{
    protected String KAFKA_TOPIC = "{{quarkus.openshift.env.vars.kafka-topic}}";
    protected String KAFKA_BOOTSTRAP_SERVERS = "{{quarkus.openshift.env.vars.kafka-bootstrap-servers}}";
    protected String MONGO_DB_HOST = "{{quarkus.openshift.env.vars.mongo-db-host}}";
    protected String MONGO_DB_DATABASE = "{{quarkus.openshift.env.vars.mongo-db-database}}";
    protected String MONGO_DB_COLLECTION = "{{quarkus.openshift.env.vars.mongo-db-collection}}";
    protected String MONGO_DB_USERNAME = "{{quarkus.openshift.env.vars.mongo-db-username}}";
    protected String MONGO_DB_PASSWORD = "{{quarkus.openshift.env.vars.mongo-db-password}}";

    @Override
    public void configure() throws Exception {
        
        //REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.auto)
        	.component("platform-http")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("/").port(8080)
			.apiContextPath("/openapi")
			.apiProperty("api.title", "Camel Quarkus Kafka API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true");
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus")
        .produces("application/json")
       
        .post("/products")
                .consumes("application/json") 
                .type(Product.class)
				.description("Send product to kafka")
				.route().routeId("postProductSend")
                .convertBodyTo(Product.class)
                .to("direct:sendToKafka")                   
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))                     
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))                    
                .endRest()
        .get("/products/{code}")
                .description("Gets a product from MongoDB from code")
                .route().routeId("getProductMongo")
                .log("Called findByCode API")
                .to("direct:getFromMongoDbByCode")                
                .endRest()
        ;      
        //Route that sends message to kafka topic
        from("direct:sendToKafka").routeId("sendToKafka")
        .marshal().json()   // marshall message to send to kafka
        .setHeader(KafkaConstants.KEY, constant("Camel")) // Key of the message
        .to("kafka:"+ KAFKA_TOPIC + "?brokers=" + KAFKA_BOOTSTRAP_SERVERS)
        ;

        //Route gets object from mongo by Id
        from("direct:getFromMongoDbByCode").routeId("getFromMongoDbByCode")
        //.setHeader(MongoDbConstants.CRITERIA, constant(Filters.("code", "${header.code}")))
        .log("code searched: ${header.code}")
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&username="+MONGO_DB_USERNAME+"&password="+MONGO_DB_PASSWORD+"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=findAll")
        .log("Response Body: ${body}")
        .setBody(simple("${body}"))
        ;
    }
}