# camel-quarkus-postgres-api Project

This project is an API that writes and queries an object (Key.java) in PostGres Database. This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Deploy on Openshift

* Create a namespace
  
  ```shell script
  oc new-project <your namespace>
  ```

* Deploy PostGres Database from Openshift Catalog 

* Preparing Database with these Psql Commmands:
  Login with sys;
  
  ```shell script
  psql --host=localhost --user=admin  sampledb
  ```
 
  ```shell script
  \dt+;
  ```
  
  
  ```shell script
  CREATE TABLE _keys(
      _id SERIAL NOT NULL,
      _key VARCHAR(50) NOT NULL,
      _value VARCHAR(50) NOT NULL,
      PRIMARY KEY ( _id )
  );
  ```
  
  ```shell script
  INSERT INTO _keys(_key,_value) VALUES('98715470130','omelo@osvaldormelo.com');
  ```
  
  ```shell script
  SELECT * FROM _keys WHERE _key = '98715470130';
  ```
* Deploy Camel Quarkus APP
  
  ```shell script
  ./mvnw clean package -Dquarkus.kubernetes.deploy=true
  ```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/camel-quarkus-oracle-api-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- OpenShift ([guide](https://quarkus.io/guides/deploying-to-openshift)): Generate OpenShift resources from annotations
- Camel Core ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-core)): Camel core functionality and basic Camel languages: Constant, ExchangeProperty, Header, Ref, Ref, Simple and Tokenize
- Camel SQL ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-sql)): Perform SQL queries
- Camel JDBC ([guide](https://camel.apache.org/camel-quarkus/latest/reference/extensions/jdbc.html)): Access databases through SQL and JDBC
- Camel Rest ([guide](https://access.redhat.com/documentation/en-us/red_hat_integration/2.latest/html/camel_extensions_for_quarkus_reference/extensions-rest)): Expose REST services and their OpenAPI Specification or call external REST services

## Provided Code

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)
