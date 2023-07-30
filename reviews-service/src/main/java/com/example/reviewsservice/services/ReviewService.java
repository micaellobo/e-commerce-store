package com.example.reviewsservice.services;

import com.example.reviewsservice.controllers.ReviewException;
import com.example.reviewsservice.dtos.*;
import com.example.reviewsservice.models.Review;
import com.example.reviewsservice.repository.IReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService implements IReviewService {

    private final RestTemplate restTemplate;
    private final IReviewMapper reviewMapper;
    private final IReviewRepository reviewRepository;

    @Value("${api.inventory-service}")
    private String inventoryServiceUrl;

    @Value("${api.user-service}")
    private String userServiceUrl;

    @Override
    public ReviewDto add(final ReviewCreateDto reviewCreateDto) {

        getProductById(reviewCreateDto.productId())
                .orElseThrow(() -> new ReviewException(ReviewException.REVIEW_DOES_NOT_EXISTS));

        var review = reviewMapper.toReview(reviewCreateDto);

        var reviewSaved = reviewRepository.save(review);

        return reviewMapper.toDto(reviewSaved);
    }

    @Override
    public List<ReviewDto> getByProduct(final Long productId) {
        var reviewsByProduct = reviewRepository.findByProductId(productId);
        return reviewMapper.toDto(reviewsByProduct);
    }

    @Override
    public List<ReviewDto> getByUserAndProduct(final Long userId, final Long productId) {
        var reviewsByUserAndProduct = reviewRepository.findByUserIdAndProductId(userId, productId);
        return reviewMapper.toDto(reviewsByUserAndProduct);
    }

    @Override
    public List<ReviewDto> getByUser(final Long userId) {
        var repositoryByUser = reviewRepository.findByUserId(userId);
        return reviewMapper.toDto(repositoryByUser);
    }

    @Override
    public ReviewDto getOneByUserAndId(final Long userId,
                                       final Long reviewId) {

        var review = reviewRepository.findByUserIdAndId(userId, reviewId)
                .orElseThrow(() -> new ReviewException(ReviewException.REVIEW_DOES_NOT_EXISTS));
        return reviewMapper.toDto(review);
    }

    @Override
    public boolean delete(final Long reviewId) {
        reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(ReviewException.REVIEW_DOES_NOT_EXISTS));

        reviewRepository.deleteById(reviewId);

        return true;
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
                .map(longDoubleEntry -> new ProductAvgRatDto(longDoubleEntry.getKey(), BigDecimal.valueOf(longDoubleEntry.getValue())))
                .toList();
    }


    private Optional<ProductDto> getProductById(final Long id) {
        try {
            var url = inventoryServiceUrl + "/" + id;

            var response = restTemplate.getForEntity(url, ProductDto.class);

            return Optional.ofNullable(response.getBody());
        } catch (Exception ex) {
            log.info(ex.getMessage());
            return Optional.empty();
        }
    }

}
