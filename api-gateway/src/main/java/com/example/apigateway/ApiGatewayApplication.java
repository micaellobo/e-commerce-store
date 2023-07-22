package com.example.apigateway;

import com.example.apigateway.filters.AuthenticationPrefilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiGatewayApplication {

    private final AuthenticationPrefilter authenticationPrefilter;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, AuthenticationPrefilter authFilter) {
        return builder.routes()
                .route("user-service-with-auth",
                        r -> r.path("/api/v1/users/create")
                                .uri("lb://user-service"))
                .route("user-service-with-auth",
                        r -> r.path("/api/v1/users/**")
                                .filters(f -> f
                                        .filter(authFilter
                                                .apply(new AuthenticationPrefilter.Config()))
                                )
                                .uri("lb://user-service"))
                .route("auth-service",
                        r -> r.path("/api/v1/auth/login")
                                .uri("lb://auth-service"))
                .build();
    }
}
