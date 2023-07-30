FROM openjdk:17 as build
COPY . /usr/app
WORKDIR /usr/app
RUN chmod +x mvnw && ./mvnw clean package

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar reviews-service.jar
ENTRYPOINT ["java","-jar","/reviews-service.jar"]
EXPOSE 8084

