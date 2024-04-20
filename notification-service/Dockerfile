FROM maven:3.8.5-openjdk-17 AS build
COPY . /usr/app
WORKDIR /usr/app
RUN mvn clean package -DskipTests

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar notification-service.jar
ENTRYPOINT ["java","-jar","/notification-service.jar"]
EXPOSE 8084
