# camel-quarkus-kafka-consumer-mongo Project

This project uses Quarkus, the Supersonic Subatomic Java Framework. This is an example consumer subscribing to a kafka topic and when messages arrive in that topic it saves the topic messages in mongoDB. Complementing the camel-quarkus-kafka-api project.

## Deploy on Openshift

* Create a namespace
  
  ```shell script
  oc new-project <your namespace>
  ```
* Install AMQ Streams Operator
  
  ![](/images/InstallAMQOperator.png)

* Deploy AMQ Streams Cluster
  
  ![](/images/DeployAMQCluster.png)

* Create Topic
  
  ![](/images/CreateTopic.png)

* Deploy Mongo DB
  ```shell script
  oc new-app \
    -e MONGODB_USER=<username> \
    -e MONGODB_PASSWORD=<password> \
    -e MONGODB_DATABASE=<database_name> \
    -e MONGODB_ADMIN_PASSWORD=<admin_password> \
    registry.redhat.io/rhscl/mongodb-26-rhel7
  ```
* Create Collection
  
  On mongoDb pod via rsh, execute:

  ```shell script
  mongo -u $MONGODB_USER -p $MONGODB_PASSWORD $MONGODB_DATABASE
  ```
  
  ```shell script
  db.createCollection("<your collection>")
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

You can then execute your native executable with: `./target/camel-quarkus-kafka-consumer-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.
