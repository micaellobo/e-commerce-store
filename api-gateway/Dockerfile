FROM maven:3.8.5-openjdk-17 as build
COPY . /usr/app
WORKDIR /usr/app
RUN mvn clean package

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar api-gateway.jar
ENTRYPOINT ["java","-jar","/api-gateway.jar"]
EXPOSE 8080

