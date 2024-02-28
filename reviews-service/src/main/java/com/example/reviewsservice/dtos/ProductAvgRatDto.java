package com.example.reviewsservice.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductAvgRatDto(
        Long productId,
        BigDecimal avg
)
        implements Serializable {
}