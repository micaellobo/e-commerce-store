package com.example.orderservice.dtos;

import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        String description) implements Serializable {
}