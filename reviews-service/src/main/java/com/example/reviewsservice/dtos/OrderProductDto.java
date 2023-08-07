package com.example.reviewsservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderProductDto(
        Long productId,
        int quantity,
        BigDecimal price
) implements Serializable {
}