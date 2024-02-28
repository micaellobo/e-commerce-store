package com.example.reviewsservice.services;

import com.example.reviewsservice.config.ContextHolder;
import com.example.reviewsservice.controllers.ReviewException;
import com.example.reviewsservice.dtos.*;
import com.example.reviewsservice.models.Review;
import com.example.reviewsservice.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService
        implements IReviewService {

    private final IReviewMapper reviewMapper;
    private final IReviewRepository reviewRepository;
    private final IOrderServiceClient orderServiceClient;
    private final ContextHolder context;

    private static boolean isProductPresentInOrder(
            final Long productId,
            final OrderDto order
    ) {
        return order.products()
                    .stream()
                    .anyMatch(orderProductDto -> orderProductDto.productId()
                                                                .equals(productId));
    }

    @Override
    public ReviewDto addOne(final ReviewCreateDto reviewCreateDto) {
        var existsReview = this.reviewRepository.existsByUserIdAndOrderIdAndProductId(
                this.context.getUserId(),
                reviewCreateDto.orderId(),
                reviewCreateDto.productId()
        );

        if (existsReview) {
            throw new ReviewException(ReviewException.REVIEW_ALREADY_EXISTS);
        }

        var order = this.orderServiceClient.getOrder(reviewCreateDto.orderId())
                                           .orElseThrow(() -> new ReviewException(ReviewException.ORDER_DOES_NOT_EXIST));

        if (!isProductPresentInOrder(reviewCreateDto.productId(), order)) {
            throw new ReviewException(ReviewException.PRODUCT_NOT_PRESENT_IN_ORDER);
        }

        var review = this.reviewMapper.toReview(reviewCreateDto, this.context.getUserId());

        var reviewSaved = this.reviewRepository.save(review);

        return this.reviewMapper.toDto(reviewSaved);
    }

    @Override
    public List<ReviewDto> getAllByUser() {
        var repositoryByUser = this.reviewRepository.findByUserId(this.context.getUserId());
        return this.reviewMapper.toDto(repositoryByUser);
    }

    @Override
    public List<ReviewDto> getAllByProduct(final Long productId) {
        var reviewsByProduct = this.reviewRepository.findByProductId(productId);

        return this.reviewMapper.toDto(reviewsByProduct);
    }

    @Override
    public boolean deleteOne(final Long reviewId) {
        return this.reviewRepository.deleteOneById(reviewId) == 1;
    }

    @Override
    public List<ProductAvgRatDto> getTopAvgRatedProducts(final int max) {
        return this.reviewRepository.findAll()
                                    .stream()
                                    .collect(Collectors.groupingBy(
                                            Review::getProductId,
                                            Collectors.averagingDouble(Review::getRating)
                                    ))
                                    .entrySet()
                                    .stream()
                                    .sorted((entry1, entry2) -> Double.compare(entry2.getValue(), entry1.getValue()))
                                    .limit(max)
                                    .map(longDoubleEntry ->
                                                 new ProductAvgRatDto(
                                                         longDoubleEntry.getKey(),
                                                         BigDecimal.valueOf(longDoubleEntry.getValue())
                                                 ))
                                    .toList();
    }
}
