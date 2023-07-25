package com.example.inventoryservice.dtos;

import com.example.inventoryservice.models.Product;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
public record ProductDto(
        Long id,
        String name,
        BigDecimal price,
        int quantity
) implements Serializable {
}