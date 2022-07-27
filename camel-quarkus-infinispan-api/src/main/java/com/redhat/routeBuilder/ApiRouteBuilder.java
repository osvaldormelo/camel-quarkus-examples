package redhat.com.routeBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.component.infinispan.InfinispanConstants;
import org.apache.camel.component.infinispan.InfinispanOperation;
import com.redhat.models.Key;
import org.apache.camel.LoggingLevel;
import java.util.concurrent.TimeUnit;


public class ApiRouteBuilder extends RouteBuilder{
    protected String REST_API = "{{quarkus.openshift.env.vars.rest-api}}";
    protected String CACHE_NAME = "{{quarkus.openshift.env.vars.cache-name}}";
    

    @Override
    public void configure() throws Exception {       
        
        //REST and Open API configuration
        restConfiguration().bindingMode(RestBindingMode.json)
        	.component("platform-http")
			.dataFormatProperty("prettyPrint", "true")
			.contextPath("/").port(8080)
			.apiContextPath("/openapi")
			.apiProperty("api.title", "Camel Quarkus Infinispan API Demo")
			.apiProperty("api.version", "1.0.0-SNAPSHOT")
            .apiProperty("cors", "true")            
            ;
        
        //REST methods configuration
        rest().tag("API Demo using Camel and Quarkus with API or Infinispan Cache")
        .produces("application/json")

        .get("/keys/{key}")
                .description("Gets a Key from API or Cache from code")
                .route().routeId("getKey")
                .log("Called findByKey API")
                .to("direct:mainRoute")                
                .endRest()
        
        ;        
        // Route of Main flow
        from("direct:mainRoute").routeId("mainRoute").streamCaching()
			.to("direct:circuitbreaker");
        // Route with Circuit Breaker Pattern
        from("direct:circuitbreaker").routeId("circuitbreaker")
        			
                    .circuitBreaker()
        			    .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.CONTAINSKEY)
                        .setHeader(InfinispanConstants.KEY,simple("${header.key}"))	           
                        .to("infinispan:" + CACHE_NAME) 
                        .setHeader("InfinispanContainsKeyResult",simple("${body}"))
                        //.log("Request to circuit breaker: ${headers} ${body}")                       
        				.to("direct:cache")
        	        .onFallback()
        	        	.to("direct:getFromRestApi")
        	        .end()
        ;
        //if result is not present on cache, search on API and put on cache for futures requests
        from("direct:cache").routeId("cache")	               
                .choice()                  
                // if exists on cache
        		.when().simple("${header.InfinispanContainsKeyResult} == 'true'")
                    .setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.GET)
                    .setHeader(InfinispanConstants.KEY,simple("${header.key}"))
                   // .log("Request to choice: ${headers} ${body}")
                    .to("infinispan:" + CACHE_NAME) 
                    .unmarshal(new JacksonDataFormat(Key.class))
                    .setBody(simple("${body}"))
                .endChoice()
        		// if not exists on  cache
        		.otherwise()
        			.to("direct:getFromRestApi")
        			.setHeader(InfinispanConstants.OPERATION).constant(InfinispanOperation.PUT)
                    .setHeader(InfinispanConstants.KEY,simple("${header.key}"))
                    .marshal().json() 
                    .setHeader(InfinispanConstants.VALUE, simple("${body}"))
                    .setHeader(InfinispanConstants.LIFESPAN_TIME, simple("${header.lifespantimeseconds}"))
                    .setHeader(InfinispanConstants.LIFESPAN_TIME_UNIT,simple(TimeUnit.SECONDS.toString()))
                    .setHeader(InfinispanConstants.MAX_IDLE_TIME, simple("${header.lifespantimeseconds}"))
                    .setHeader(InfinispanConstants.MAX_IDLE_TIME_UNIT, simple(TimeUnit.SECONDS.toString()))
                   // .log("Request to PUT on Cache: ${headers} ${body}") 
                   .to("infinispan:" + CACHE_NAME)
                   // .log("Result to PUT on Cache: ${headers} ${body}") 
                    .setBody(simple("${header.CamelInfinispanValue}"))
                    .unmarshal(new JacksonDataFormat(Key.class))
                   .setBody(simple("${body}"))
        		.endChoice();
        
        //Route gets object from Rest API
        from("direct:getFromRestApi").routeId("getFromRestApi")
            .setHeader("key", simple("${header.key}"))
            //.log("Request to API : ${headers} ${body}")
            .to("rest:get:/keys/{key}?host=" + REST_API)
            //.log("Response from API: ${headers} ${body}")
            .unmarshal(new JacksonDataFormat(Key.class))
        .setBody(simple("${body}")) 
        ;
        
    }
}