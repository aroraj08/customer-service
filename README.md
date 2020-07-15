# Rapidkart-Customer-Service

This is a sample project using Spring Data JPA for persisting customer related data in in-memory H2 database (H2)

#### Embedded database can be accessed at - 
http://localhost:8070/h2-console/

#### Test cases can be run using test profile - mvn clean install -Dspring.profiles.active=test

#### Steps to deploying in Docker Swarm Cluster are available in file "ClusterDeploymentSteps"

#### Docker image (layered) can be built using below command - 

`mvn clean package -Dmaven.test.skip=true docker:build docker:push`

### Pending enhancements -

5. Add actuator dependency
7. Creating Bill of Materials (BOM) for this project
9. Adding Spring Sleuth for tracing

#### Using [JSON PATH] for parsing JSON in test class
(https://github.com/json-path/JsonPath)

#### Guide for writing DB tests - 
https://reflectoring.io/spring-boot-data-jpa-test/
https://www.baeldung.com/spring-boot-testcontainers-integration-test 

### Contains Docker-Compose file for running all the services
