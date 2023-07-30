package com.example.reviewsservice.dtos;

import java.math.BigDecimal;

public record ProductAvgRatDto(Long productId, BigDecimal avg) {
}
