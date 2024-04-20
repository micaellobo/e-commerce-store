FROM maven:3.8.5-openjdk-17 AS build
COPY . /usr/app
WORKDIR /usr/app
RUN mvn clean install -DskipTests package

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar order-service.jar
ENTRYPOINT ["java","-jar","/order-service.jar"]
EXPOSE 8084

