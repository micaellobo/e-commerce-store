package com.example.inventoryservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.example.inventoryservice.models.Product}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record ProductStockQuantityDto(
        @NotNull
        Long productId,
        @NotNull
        @Positive
        int quantity
)
        implements Serializable {
}