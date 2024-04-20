FROM maven:3.8.5-openjdk-17 AS build
COPY . /usr/app
WORKDIR /usr/app
RUN mvn clean package -DskipTests

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar auth-service.jar
ENTRYPOINT ["java","-jar","/auth-service.jar"]
EXPOSE 8082

