package com.example.apigateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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

            if (bearerToken == null) {
                log.error("bearerToken NULL");
                return this.onError(exchange, HttpStatus.UNAUTHORIZED);
            }

            try {
                return this.webClientBuilder.build()
                        .post()
                        .uri(this.authServiceUrl + "/validate")
                        .header("Authorization", bearerToken)
                        .header("CorrelationID", correlationID)
                        .retrieve()
                        .toBodilessEntity()
                        .flatMap(this.getResponseEntityMonoFunction(exchange, chain));
            } catch (WebClientResponseException e) {
                log.error(e.getMessage());
                return this.onError(exchange, e.getStatusCode());
            } catch (Exception e) {
                log.error(e.getMessage());
                return this.onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        };
    }

    private Function<ResponseEntity<Void>, Mono<? extends Void>> getResponseEntityMonoFunction(
            final ServerWebExchange exchange,
            final GatewayFilterChain chain) {
        return response -> {

            if (!response.getStatusCode().is2xxSuccessful()) {
                return this.onError(exchange, response.getStatusCode());
            }

            var headers = response
                    .getHeaders();

            var userId = headers.getFirst("userId");
            var username = headers.getFirst("username");

            if (username == null || userId == null) {
                return this.onError(exchange, response.getStatusCode());
            }

            var modifiedRequest = exchange.getRequest()
                    .mutate()
                    .header("userId", userId)
                    .header("username", username)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }

    private Mono<Void> onError(final ServerWebExchange exchange, final HttpStatusCode statusCode) {
        var response = exchange.getResponse();
        response.setStatusCode(statusCode);
        return response.setComplete();
    }


    public static class Config {


    }
}
