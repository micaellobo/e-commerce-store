package com.example.inventoryservice.dtos;

import com.example.inventoryservice.models.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link Product}
 */
@Builder
public record ProductCreateDto(
        @NotBlank
        String name,
        @NotNull
        @Positive(message = "Only positive number allowed")
        BigDecimal price,
        @Positive(message = "Only positive number allowed")
        int quantity,
        String description) implements Serializable {
}