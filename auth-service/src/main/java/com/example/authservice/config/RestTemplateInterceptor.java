package com.example.authservice.config;

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

    private final CustomContextHolder context;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        var correlationId = context.getCorrelationId();
        var userId = context.getUserId();
        var username = context.getUsername();

//        log.info("{} - {} - {} - {}", request.getMethod(), request.getURI().getPath(), correlationId, null);

        var headers = request.getHeaders();
        headers.add("CorrelationId", correlationId);

        if (context.isAuthenticated()) {
            headers.add("userId", userId.toString());
            headers.add("username", username);
        }

        return execution.execute(request, body);
    }
}
