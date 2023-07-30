package com.example.apigateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config> {

    private final WebClient.Builder webClientBuilder;

    @Value("${api.auth-service}")
    private String authServiceUrl;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            var headers = exchange.getRequest()
                    .getHeaders();

            var bearerToken = headers.getFirst("Authorization");
            var correlationID = headers.getFirst("CorrelationID");

            return webClientBuilder.build()
                    .post()
                    .uri(authServiceUrl + "/validate")
                    .header("Authorization", bearerToken)
                    .header("CorrelationID", correlationID)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(getResponseEntityMonoFunction(exchange, chain))
                    .onErrorResume(error -> {
                        log.error(error.getMessage());
                        return onError(exchange);
                    });
        };
    }

    private Function<ResponseEntity<Void>, Mono<? extends Void>> getResponseEntityMonoFunction(
            final ServerWebExchange exchange,
            final GatewayFilterChain chain) {
        return response -> {
            var headers = response
                    .getHeaders();

            var userId = headers.getFirst("userId");
            var username = headers.getFirst("username");

            if (username == null || userId == null) {
                return onError(exchange);
            }

            var modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("userId", userId)
                    .header("username", username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private Mono<Void> onError(final ServerWebExchange exchange) {
        var response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }


    public static class Config {


    }
}
