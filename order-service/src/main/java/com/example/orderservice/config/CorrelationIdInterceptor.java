package com.example.orderservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var correlationId = UUID.randomUUID().toString();
        var headers = request.getHeaders();

        log.info("{} - {} - {} - {}", request.getMethod(), request.getURI().getPath(), correlationId, null);

        headers.add("CorrelationId", correlationId);

        return execution.execute(request, body);
    }
}
