package com.example.reviewsservice.repository;

import com.example.reviewsservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByUserIdAndId(Long userId, Long reviewId);

    List<Review> findByUserId(Long userId);

    List<Review> findByUserIdAndProductId(Long userId, Long productId);
    List<Review> findByProductId(Long productId);
}
