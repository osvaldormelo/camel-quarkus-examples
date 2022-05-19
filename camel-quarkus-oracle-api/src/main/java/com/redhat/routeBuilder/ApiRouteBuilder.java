package redhat.com.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.component.jackson.JacksonDataFormat;
import com.redhat.models.Key;
import com.redhat.models.KeyList;
import com.redhat.processors.MapProcessor;
import com.redhat.processors.RowProcessor;
import com.redhat.processors.RowsProcessor;
import org.apache.camel.LoggingLevel;



public class ApiRouteBuilder extends RouteBuilder{
    protected String LIFE_SPAN_TIME = "{{quarkus.openshift.env.vars.life-span-time}}";

    @Override
    public void configure() throws Exception {        

        //REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.json)
        	.component("platform-http")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("/").port(8080)
			.apiContextPath("/openapi")
			.apiProperty("api.title", "Camel Quarkus Oracle API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true");
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus with Oracle Database")
        .produces("application/json")
       
        .post("/keys")
                .consumes("application/json") 
                .type(Key.class)
				.description("Send Key to Oracle")
				.route().routeId("postKeySendOracle")
                .convertBodyTo(Key.class)
                .to("direct:sendToOracle")                   
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))                     
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))                    
                .endRest()
        .get("/keys/{key}")
                .description("Gets a Key from Oracle from code")
                .route().routeId("getKeyFromOracle")
                .log("Called findByCode API")
                .to("direct:getFromOracleDbById")                
                .endRest()
        .get("/getAll")
                .description("Gets all Keys from Oracle")
                .route().routeId("getAllKeyFromOracle")
                .log("Called getAll API")
                .to("direct:getAllFromOracleDb")                
                .endRest()
        ;        
        //Route that sends message to Oracle Database
        from("direct:sendToOracle").routeId("sendToOracle")      
            .process(new MapProcessor())
            .log("${body}")
            .to("sql:INSERT INTO ot.keys(key,value) VALUES(:#key,:#value)")
        .setBody(simple("Sucessifully Inserted"))
        ;

        //Route gets object from Oracle by Id
        from("direct:getFromOracleDbById").routeId("getFromOracleDbById")        
            .log("key searched: ${header.key}")
            .to("sql:SELECT * FROM ot.keys WHERE key = :#key AND rownum = 1")
            .log("Response Body: ${body}")
            .process(new RowProcessor())
            .setHeader("LifeSpanTimeSeconds",simple(LIFE_SPAN_TIME))//header for cache use
        .setBody(simple("${body}"))
        ;

        //Route gets all objects from Oracle
        from("direct:getAllFromOracleDb").routeId("getAllFromOracleDb")        
            .to("sql:SELECT * FROM ot.keys")
            .log("Response Body: ${body}")
            .process(new RowsProcessor())
        .setBody(simple("${body}"))
        ;
    }
}