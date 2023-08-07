package com.example.reviewsservice.services;

import com.example.reviewsservice.dtos.ProductAvgRatDto;
import com.example.reviewsservice.dtos.ReviewCreateDto;
import com.example.reviewsservice.dtos.ReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {
    ReviewDto add(final ReviewCreateDto reviewCreateDto);

    List<ReviewDto> getAllByProduct(final Long productId);

    List<ReviewDto> getByUser();

    boolean delete(final Long reviewId);

    List<ProductAvgRatDto> getTopAvgRatedProducts(int max);
}
