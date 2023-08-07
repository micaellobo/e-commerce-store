package com.example.reviewsservice.services;

import com.example.reviewsservice.dtos.ProductAvgRatDto;
import com.example.reviewsservice.dtos.ReviewCreateDto;
import com.example.reviewsservice.dtos.ReviewDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {
    ReviewDto addOne(final ReviewCreateDto reviewCreateDto);

    List<ReviewDto> getAllByProduct(final Long productId);

    List<ReviewDto> getAllByUser();

    boolean deleteOne(final Long reviewId);

    List<ProductAvgRatDto> getTopAvgRatedProducts(int max);
}
