package com.example.reviewsservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record OrderProductDto(
        Long productId,
        int quantity,
        BigDecimal price
)
        implements Serializable {
}