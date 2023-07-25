FROM openjdk:17
VOLUME /tmp
COPY target/*.jar inventory-service.jar
ENTRYPOINT ["java","-jar","/inventory-service.jar"]
EXPOSE 8083

