package com.example.reviewsservice.dtos;

import com.example.reviewsservice.models.Review;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link Review}
 */
public record ReviewDto(
        Long id,
        Long userId,
        Long productId,
        String reviewText,
        int helpFulCount,
        LocalDateTime createdAt,
        int rating
) implements Serializable {
}