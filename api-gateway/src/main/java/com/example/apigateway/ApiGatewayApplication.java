package com.example.apigateway;

import com.example.apigateway.filters.AuthenticationPrefilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder, AuthenticationPrefilter authFilter) {
        return builder.routes()
                .route("auth-service",
                        r -> r.path("/api/v1/auth/login")
                                .uri("lb://auth-service"))
                .route("user-service-with-auth",
                        r -> r.path("/api/v1/users/me/**")
                                .filters(f -> f.filter(authFilter.apply(new AuthenticationPrefilter.Config())))
                                .uri("lb://user-service"))
                .route("user-service-no-auth",
                        r -> r.path("/api/v1/users/**")
                                .uri("lb://user-service"))
                .route("inventory-service-with-auth",
                        r -> r.path("/api/v1/products/users/me/**")
                                .filters(f -> f.filter(authFilter.apply(new AuthenticationPrefilter.Config())))
                                .uri("lb://inventory-service"))
                .route("inventory-service-no-auth",
                        r -> r.path("/api/v1/products/**")
                                .uri("lb://inventory-service"))
                .route("reviews-service-with-auth",
                        r -> r.path("/api/v1/reviews/users/me/**")
                                .filters(f -> f.filter(authFilter.apply(new AuthenticationPrefilter.Config())))
                                .uri("lb://reviews-service"))
                .route("reviews-service-no-auth",
                        r -> r.path("/api/v1/reviews/**")
                                .uri("lb://reviews-service"))
                .route("orders-service-with-auth",
                        r -> r.path("/api/v1/orders/users/me/**")
                                .filters(f -> f.filter(authFilter.apply(new AuthenticationPrefilter.Config())))
                                .uri("lb://order-service"))
                .route("orders-service-no-auth",
                        r -> r.path("/api/v1/orders/**")
                                .uri("lb://order-service"))
                .build();
    }
}
