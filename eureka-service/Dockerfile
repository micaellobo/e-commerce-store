FROM openjdk:17 as build
COPY . /usr/app
WORKDIR /usr/app
RUN chmod +x mvnw && ./mvnw clean package

FROM openjdk:17
VOLUME /tmp
COPY --from=build /usr/app/target/*.jar eureka-service.jar
ENTRYPOINT ["java","-jar","/eureka-service.jar"]
EXPOSE 8761

