package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * DTO for {@link com.example.orderservice.models.Order}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreateDto(
        List<OrderProductCreateDto> products
) implements Serializable {
}