package com.example.reviewsservice.services;

import com.example.reviewsservice.config.CustomContextHolder;
import com.example.reviewsservice.controllers.ReviewException;
import com.example.reviewsservice.dtos.*;
import com.example.reviewsservice.models.Review;
import com.example.reviewsservice.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final IReviewMapper reviewMapper;
    private final IReviewRepository reviewRepository;
    private final IOrderServiceClient orderServiceClient;
    private final CustomContextHolder context;

    @Override
    public ReviewDto addOne(final ReviewCreateDto reviewCreateDto) {

        var existsReview = reviewRepository.existsByUserIdAndOrderIdAndProductId(
                context.getUserId(),
                reviewCreateDto.orderId(),
                reviewCreateDto.productId()
        );

        if (existsReview)
            throw new ReviewException(ReviewException.REVIEW_ALREADY_EXISTS);

        var order = orderServiceClient.getOrder(
                        reviewCreateDto.orderId(),
                        context.getUserId(),
                        context.getUsername())
                .orElseThrow(() -> new ReviewException(ReviewException.ORDER_DOES_NOT_EXIST));

        if (!isProductPresentInOrder(reviewCreateDto, order))
            throw new ReviewException(ReviewException.PRODUCT_NOT_PRESENT_IN_ORDER);

        var review = reviewMapper.toReview(reviewCreateDto);

        var reviewSaved = reviewRepository.save(review);

        return reviewMapper.toDto(reviewSaved);
    }

    private static boolean isProductPresentInOrder(final ReviewCreateDto reviewCreateDto, final OrderDto order) {
        return order.products()
                .stream()
                .anyMatch(orderProductDto -> orderProductDto.productId().equals(reviewCreateDto.productId()));
    }

    @Override
    public List<ReviewDto> getAllByUser() {
        var repositoryByUser = reviewRepository.findByUserId(context.getUserId());
        return reviewMapper.toDto(repositoryByUser);
    }

    @Override
    public List<ReviewDto> getAllByProduct(final Long productId) {
        var reviewsByProduct = reviewRepository.findByProductId(productId);

        return reviewMapper.toDto(reviewsByProduct);
    }

    @Override
    public boolean deleteOne(final Long reviewId) {
        reviewRepository.findByUserIdAndId(context.getUserId(), reviewId)
                .orElseThrow(() -> new ReviewException(ReviewException.REVIEW_DOES_NOT_EXISTS));

        return reviewRepository.deleteOneById(reviewId) == 1;
    }

    @Override
    public List<ProductAvgRatDto> getTopAvgRatedProducts(final int max) {
        return reviewRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Review::getProductId, Collectors.averagingDouble(Review::getRating)))
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                .limit(max)
                .map(longDoubleEntry ->
                        new ProductAvgRatDto(longDoubleEntry.getKey(), BigDecimal.valueOf(longDoubleEntry.getValue())))
                .toList();
    }
}
