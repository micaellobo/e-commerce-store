package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.orderservice.models.Order}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderCreateDto(
        List<OrderProductCreateDto> products
) implements Serializable {
}