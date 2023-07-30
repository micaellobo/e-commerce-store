package com.example.reviewsservice.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        String description) implements Serializable {
}