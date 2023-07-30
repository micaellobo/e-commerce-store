package com.example.reviewsservice.services;

import com.example.reviewsservice.dtos.ProductAvgRatDto;
import com.example.reviewsservice.dtos.ReviewCreateDto;
import com.example.reviewsservice.dtos.ReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {
    ReviewDto add(final ReviewCreateDto reviewCreateDto);

    List<ReviewDto> getByProduct(final Long productId);

    List<ReviewDto> getByUserAndProduct(final Long userId, final Long productId);

    List<ReviewDto> getByUser(final Long userId);

    ReviewDto getOneByUserAndId(final Long userid, final Long reviewId);

    boolean delete(final Long reviewId);

    List<ProductAvgRatDto> getTopAvgRatedProducts(int max);
}
