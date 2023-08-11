package redhat.com.routeBuilder;


import org.apache.camel.component.mongodb.MongoDbConstants;
import com.mongodb.client.model.Filters;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import com.redhat.models.Approval;
import org.apache.camel.LoggingLevel;
import org.bson.types.ObjectId;


public class ApiRouteBuilder extends RouteBuilder{
    
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
			.apiProperty("api.title", "Camel Quarkus Approval API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true");
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus")
        .produces("application/json")
       
        .post("/approval")
                .consumes("application/json") 
                .type(Approval.class)
				.description("Create Approval to mongo")
				.route().routeId("postApprovalSend")
                .convertBodyTo(Approval.class)
                .to("direct:createInMongo")                   
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))                     
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))                    
                .endRest()
        .get("/approval/{id}")
                .description("Gets a approval from MongoDB from id")
                .route().routeId("getApprovalMongo")
                .log("Called getApprovalMongo API")
                .to("direct:getFromMongoDbById")                
                .endRest()
        .get("/approve/{mud}")
                .description("Aprove in MongoDB from id")
                .route().routeId("getApproveMongo")
                .log("Called getApproveMongo API")
                .to("direct:updateInMongo")                
                .endRest()
        ;      
        //Route that sends message to kafka topic
        from("direct:createInMongo").routeId("createInMongo")
        //.marshal().json()   // marshall message to send to kafka
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&username="+MONGO_DB_USERNAME+"&password="+MONGO_DB_PASSWORD+"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=insert")
        ;

        //Route gets object from mongo by Id
        from("direct:getFromMongoDbById").routeId("getFromMongoDbById")
        //.setHeader(MongoDbConstants.CRITERIA, constant(Filters.("code", "${header.code}")))
        .log("code searched: ${header.code}")
        .to("mongodb:mongoBean?hosts="+ MONGO_DB_HOST +"&username="+MONGO_DB_USERNAME+"&password="+MONGO_DB_PASSWORD+"&database="+ MONGO_DB_DATABASE +"&collection="+ MONGO_DB_COLLECTION +"&operation=findAll")
        .log("Response Body: ${body}")
        .setBody(simple("${body}"))
        ;

        from("direct:updateInMongo").routeId("updateInMongo")
        .setHeader(MongoDbConstants.CRITERIA, simple("{ \"mud\": \"${header.mud}\" }"))// Critério de correspondência pelo campo "mud"
        .log("Valor de mud: ${header.mud}")
        .setBody(simple("{ \"$set\" : { \"description\" : \"Aprovada\" } }")) // A atualização que você deseja aplicar
        .to("mongodb:mongoBean?hosts=" + MONGO_DB_HOST + "&username=" + MONGO_DB_USERNAME + "&password=" + MONGO_DB_PASSWORD + "&database=" + MONGO_DB_DATABASE + "&collection=" + MONGO_DB_COLLECTION + "&operation=update")
        .log("Response Body: ${body}")
        .setBody(simple("${body}"));
    }
}