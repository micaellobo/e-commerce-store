package com.example.orderservice.config;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Data
@Component
@RequestScope
public class ContextData {

    private final String correlationId;
    private final Long userId;
    private final String username;
}
