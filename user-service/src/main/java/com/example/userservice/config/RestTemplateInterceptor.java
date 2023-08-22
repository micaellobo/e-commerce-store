package com.example.userservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.example.userservice.config.ContextHolder.CORRELATION_ID;

@Slf4j
@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final ContextHolder context;

    @Override
    public ClientHttpResponse intercept(
            final HttpRequest request,
            final byte[] body,
            final ClientHttpRequestExecution execution) throws IOException {

        var correlationId = this.context.getCorrelationId();
        var userId = this.context.getUserId();
        var username = this.context.getUsername();

        var headers = request.getHeaders();
        headers.add(CORRELATION_ID, correlationId);

        if (this.context.isAuthenticated()) {
            headers.add("userId", userId.toString());
            headers.add("username", username);
        }

        return execution.execute(request, body);
    }
}
