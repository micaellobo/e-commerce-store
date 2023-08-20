package com.example.inventoryservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final ContextHolder context;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var correlationId = this.context.getCorrelationId();
        var userId = this.context.getUserId();
        var username = this.context.getUsername();

//        log.info("{} - {} - {} - {}", request.getMethod(), request.getURI().getPath(), correlationId, null);

        var headers = request.getHeaders();
        headers.add("CorrelationId", correlationId);

        if (this.context.isAuthenticated()) {
            headers.add("userId", userId.toString());
            headers.add("username", username);
        }

        return execution.execute(request, body);
    }
}
