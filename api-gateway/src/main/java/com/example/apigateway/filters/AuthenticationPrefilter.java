package com.example.apigateway.filters;

import lombok.extern.slf4j.Slf4j;
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

    public AuthenticationPrefilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            log.info("URL is - " + request.getURI().getPath());
            String bearerToken = request.getHeaders().getFirst("Authorization");
            log.info("Bearer Token: " + bearerToken);

            return webClientBuilder.build().post().uri("lb://auth-service/api/v1/auth/validate").header("Authorization", bearerToken).retrieve().toBodilessEntity().flatMap(responseEntity -> {

                String username = responseEntity.getHeaders().getFirst("username");

                if (username == null) {
                    log.warn("username null");
                    return onError(exchange, "", HttpStatus.UNAUTHORIZED);
                }

                ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().header("username", username).build();

                return chain.filter(exchange.mutate().request(modifiedRequest).build());
            }).onErrorResume(throwable -> {
                log.error(throwable.getMessage());
                return onError(exchange, "", HttpStatus.UNAUTHORIZED);
            });
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }


    public static class Config {


    }
}
