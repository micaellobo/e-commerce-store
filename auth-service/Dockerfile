FROM openjdk:17
VOLUME /tmp
COPY target/*.jar auth-service.jar
ENTRYPOINT ["java","-jar","/auth-service.jar"]
EXPOSE 8082

