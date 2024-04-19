package com.example.config;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.testcontainers.containers.PostgreSQLContainer;


@Lazy
@TestConfiguration(proxyBeanMethods = false)
public class TestsConfig {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres:latest");
    }

    @Bean
    public RequestSpecification requestSpec(@Value("${local.server.port}") int port) {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setPort(port)
                .build();
    }
}
