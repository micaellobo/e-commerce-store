FROM openjdk:17-jdk AS build
VOLUME /tmp
COPY target/*.jar inventory-service.jar
ENTRYPOINT ["java","-jar","/inventory-service.jar"]
