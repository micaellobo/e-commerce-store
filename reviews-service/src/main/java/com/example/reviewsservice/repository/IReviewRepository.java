package com.example.reviewsservice.repository;

import com.example.reviewsservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface IReviewRepository
        extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndId(
            Long userId,
            Long reviewId
    );

    List<Review> findByUserId(Long userId);

    List<Review> findByProductId(Long productId);

    boolean existsByUserIdAndOrderIdAndProductId(
            final Long userId,
            final Long orderId,
            final Long productId
    );

    Optional<Review> findByUserIdAndOrderIdAndProductId(
            final Long userId,
            final Long orderId,
            final Long productId
    );

    @Transactional
    @Modifying
    @Query("delete from reviews r where r.id = ?1")
    int deleteOneById(Long id);
}
