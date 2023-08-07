package com.example.reviewsservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDto(
        Long id,
        Long userId,
        List<OrderProductDto> products,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}