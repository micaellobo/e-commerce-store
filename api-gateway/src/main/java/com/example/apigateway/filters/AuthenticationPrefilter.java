package com.example.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Value("${api.user-service}")
    private String userServiceUrl;

    public AuthenticationPrefilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            var headers = exchange.getRequest()
                    .getHeaders();

            var bearerToken = headers.getFirst("Authorization");
            var correlationID = headers.getFirst("CorrelationID");

            return webClientBuilder.build()
                    .post()
                    .uri(userServiceUrl + "validate")
                    .header("Authorization", bearerToken)
                    .header("CorrelationID", correlationID)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(response -> {
                        String username = response
                                .getHeaders()
                                .getFirst("username");

                        if (username == null) {
                            log.warn("username null");
                            return onError(exchange);
                        }

                        ServerHttpRequest modifiedRequest = exchange.getRequest()
                                .mutate()
                                .header("username", username)
                                .build();

                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    }).onErrorResume(error -> {
                        log.error(error.getMessage());
                        return onError(exchange);
                    });
        };
    }

    private Mono<Void> onError(final ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    public static class Config {


    }
}
