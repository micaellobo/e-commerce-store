package com.example.reviewsservice.dtos;

import com.example.reviewsservice.models.Review;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Review}
 */
@Builder
public record ReviewDto(
        Long id,
        Long userId,
        Long productId,
        Long orderId,
        String reviewText,
        LocalDateTime createdAt,
        int rating
)
        implements Serializable {
}