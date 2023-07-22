FROM openjdk:17
VOLUME /tmp
COPY target/*.jar user-service.jar
ENTRYPOINT ["java","-jar","/user-service.jar"]
EXPOSE 8081

