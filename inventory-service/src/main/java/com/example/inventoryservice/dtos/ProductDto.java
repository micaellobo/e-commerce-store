package com.example.inventoryservice.dtos;

import com.example.inventoryservice.models.Product;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
@Builder
public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        int quantity,
        String description) implements Serializable {
}