package com.example.apigateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.RewritePathGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class SwaggerFilter extends RewritePathGatewayFilterFactory {
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();

            var modifiedRequest = request
                    .mutate()
                    .path("/api-docs")
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        };
    }
}
