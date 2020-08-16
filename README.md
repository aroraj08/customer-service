# Rapidkart-Customer-Service

[![rapidkart-customer-service](https://circleci.com/gh/aroraj08/rapidkart-customer-service.svg?style=svg)](https://app.circleci.com/pipelines/github/aroraj08/rapidkart-customer-service?branch=master)


This is a sample project using Spring Data JPA for persisting customer related data in in-memory H2 database (H2). It uses Redis backed cache for few of the operations. 
Project is associated with multiple profiles - 
 1. local -- use this profile when the application needs to be run without any dependency on cloud-config, eureka server.
 2. local-discovery -- use this profile when other Spring cloud components are up and running. 
 3. For deployment as swarm cluster, use the docker-compose.yml file in the resources folder.
 4. default -- same as local.
 
At this point, the application needs to connect to Redis server for Caching functionality. This can be customized and made profile specific. An issue has been created to enable application to only use redis when "redis" profile is available. 

#### Embedded database can be accessed at - 
http://localhost:8070/h2-console/

#### Integration Tests can be run using verify phase - 
`mvn verify`

#### Docker image (layered) can be built using below command - 

`mvn clean package -Dmaven.test.skip=true docker:build docker:push`

#### Using [JSON PATH] for parsing JSON in test class
(https://github.com/json-path/JsonPath)

#### Guide for writing DB tests - 
https://reflectoring.io/spring-boot-data-jpa-test/
https://www.baeldung.com/spring-boot-testcontainers-integration-test 

#### To run redis in a remote machine as a container - 

`docker pull redis`

`docker container run -d -p 6379:6379 redis`

`apt install redis-tools`

#### To run JMS broker in a remote machine as a container (used activemq-artemis-docker from https://github.com/vromero/activemq-artemis-docker)  

`docker run -it --rm -p 8161:8161 -p 61616:61616 vromero/activemq-artemis`
  
#### Running wiremock will create mappings folder, where mappings for mock response are stored

`java -jar wiremock-standalone-2.27.0.jar`  

