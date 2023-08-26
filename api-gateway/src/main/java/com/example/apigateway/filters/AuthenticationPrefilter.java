package com.example.apigateway.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

import static com.example.apigateway.filters.CorrelationIdFilter.CORRELATION_ID;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationPrefilter implements GatewayFilter {

    private final WebClient.Builder webClientBuilder;

    @Value("${api.auth-service}")
    private String authServiceUrl;

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

    @Override
    public Mono<Void> filter(final ServerWebExchange exchange, final GatewayFilterChain chain) {

        var headers = exchange.getRequest()
                .getHeaders();

        var bearerToken = headers.getFirst(HttpHeaders.AUTHORIZATION);
        var correlationID = headers.getFirst(CORRELATION_ID);

        if (bearerToken == null) {
            log.error("bearerToken NULL");
            return this.onError(exchange, HttpStatus.UNAUTHORIZED);
        }
        try {
            return this.webClientBuilder.build()
                    .post()
                    .uri(this.authServiceUrl + "/validate")
                    .header(HttpHeaders.AUTHORIZATION, bearerToken)
                    .header(CORRELATION_ID, correlationID)
                    .retrieve()
                    .toBodilessEntity()
                    .flatMap(this.getResponseEntityMonoFunction(exchange, chain))
                    .onErrorResume(WebClientResponseException.class, e -> {
                        log.error(e.getMessage());
                        return this.onError(exchange, e.getStatusCode());
                    });
        } catch (Exception e) {
            log.error(e.getMessage());
            return this.onError(exchange, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
