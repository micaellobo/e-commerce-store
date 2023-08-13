package com.example.orderservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.example.orderservice.models.Order}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record OrderCreateDto(
        List<OrderProductCreateDto> products
) implements Serializable {
}