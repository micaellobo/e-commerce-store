package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.example.orderservice.models.OrderProduct}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record OrderProductCreateDto(
        @NotNull Long productId,
        @Positive int quantity
) implements Serializable {
}