package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO for {@link com.example.orderservice.models.Order}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDto(
        Long id,
        Long userId,
        Set<OrderProductDto> products,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}