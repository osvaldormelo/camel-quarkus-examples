# camel-quarkus-ws-to-event Project

This project uses Quarkus, the Supersonic Subatomic Java Framework. This is a project for a proof of concept using a websocket connection to an AMQ Broker(Apache Artemis) and a AMQ Streams(Kafka) topics. This module is a producer that produce messages on AMQ Broker and Kafka topic.

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

* Install AMQ Broker(Artemis) Operator
  
  ![](/images/InstallAMQBrokerOperator.png)

* Deploy AMQ Broker(Artemis)
  
  ![](/images/DeployAMQBrokerCluster.png)

* Create Topics raw and processed automatically when you publish first messages

* Deploy Camel Quarkus APP
  
  ```shell script
  ./mvnw clean package -Dquarkus.kubernetes.deploy=true
  ```

== Deploy as Camel-K application

    oc create secret generic chatbot --from-file src/main/resources/application.properties
    kamel run src/main/java/com/redhat/ChatControllerRoute.java --dev --config secret:chatbot

== Deploy as Camel Quarkus Application

Local with Quarkus CLI

    quarkus dev

Local with Maven

    mvn compile quarkus:dev

In Openshift

    ./mvnw clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true -DskipTests