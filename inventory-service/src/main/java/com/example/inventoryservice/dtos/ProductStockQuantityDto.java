package com.example.inventoryservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;

import java.io.Serializable;

/**
 * DTO for {@link com.example.inventoryservice.models.Product}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProductStockQuantityDto(
        @Positive
        int quantity
) implements Serializable {
}