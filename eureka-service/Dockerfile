FROM openjdk:17
VOLUME /tmp
COPY target/*.jar eureka-service.jar
ENTRYPOINT ["java","-jar","/eureka-service.jar"]
EXPOSE 8761

