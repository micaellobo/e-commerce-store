package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link com.example.orderservice.models.OrderProduct}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderProductCreateDto(
        @NotNull Long productId,
        @Positive int quantity
) implements Serializable {
}