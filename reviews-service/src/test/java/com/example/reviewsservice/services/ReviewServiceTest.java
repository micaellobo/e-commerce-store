package com.example.reviewsservice.services;

import com.example.reviewsservice.config.ContextHolder;
import com.example.reviewsservice.controllers.ReviewException;
import com.example.reviewsservice.dtos.*;
import com.example.reviewsservice.models.Review;
import com.example.reviewsservice.repository.IReviewRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    IReviewMapper reviewMapper;
    @Mock
    IReviewRepository reviewRepository;
    @Mock
    IOrderServiceClient orderServiceClient;
    @Mock
    ContextHolder contextHolder;
    @InjectMocks
    ReviewService reviewService;
    private OrderProductDto orderProduct;
    private ReviewCreateDto reviewCreateDto;
    private ReviewDto reviewDto;
    private Review review;

    @BeforeEach
    void beforeEach() {
        var productDto = ProductDto.builder()
                                   .id(1L)
                                   .name("Trackpad")
                                   .description("Apple Magic Trackpad")
                                   .price(BigDecimal.valueOf(99.99))
                                   .quantity(100)
                                   .build();

        this.reviewCreateDto = ReviewCreateDto.builder()
                                              .orderId(1L)
                                              .productId(productDto.id())
                                              .reviewText("textReview")
                                              .rating(5)
                                              .build();

        this.review = Review.builder()
                            .id(1L)
                            .productId(this.reviewCreateDto.productId())
                            .rating(this.reviewCreateDto.rating())
                            .orderId(this.reviewCreateDto.orderId())
                            .reviewText(this.reviewCreateDto.reviewText())
                            .build();

        this.reviewDto = ReviewDto.builder()
                                  .id(this.review.getId())
                                  .orderId(this.review.getOrderId())
                                  .productId(this.review.getProductId())
                                  .reviewText(this.review.getReviewText())
                                  .rating(this.review.getRating())
                                  .build();

        this.orderProduct = OrderProductDto.builder()
                                           .productId(productDto.id())
                                           .price(productDto.price())
                                           .quantity(10)
                                           .build();

        lenient().when(this.contextHolder.getUserId())
                 .thenReturn(1L);
        lenient().when(this.contextHolder.getUsername())
                 .thenReturn("JhonDoe");
    }

    @Test
    void addOne_WhenValidReview_ShouldSaveReview() {
        //Arrange
        var orderDto = OrderDto.builder()
                               .userId(this.contextHolder.getUserId())
                               .products(List.of(this.orderProduct))
                               .build();

        when(this.reviewRepository.existsByUserIdAndOrderIdAndProductId(anyLong(), anyLong(), anyLong()))
                .thenReturn(false);
        when(this.orderServiceClient.getOrder(anyLong()))
                .thenReturn(Optional.of(orderDto));
        when(this.reviewMapper.toReview(any(ReviewCreateDto.class), anyLong()))
                .thenReturn(new Review());
        when(this.reviewRepository.save(any(Review.class)))
                .thenReturn(new Review());
        when(this.reviewMapper.toDto(any(Review.class)))
                .thenReturn(ReviewDto.builder()
                                     .build());

        //Act
        var reviewCreated = this.reviewService.addOne(this.reviewCreateDto);

        //Assert
        Assertions.assertNotNull(reviewCreated);
    }

    @Test
    void addOne_WhenReviewAlreadyExists_ShouldThrowReviewException() {
        // Arrange
        when(this.reviewRepository.existsByUserIdAndOrderIdAndProductId(anyLong(), anyLong(), anyLong()))
                .thenReturn(true);

        // Act and Assert
        Assertions.assertThrows(
                ReviewException.class,
                () -> this.reviewService.addOne(this.reviewCreateDto),
                ReviewException.REVIEW_ALREADY_EXISTS
        );
    }

    @Test
    void addOne_WhenOrderDoesNotExist_ShouldThrowReviewException() {
        // Arrange
        when(this.reviewRepository.existsByUserIdAndOrderIdAndProductId(anyLong(), anyLong(), anyLong()))
                .thenReturn(false);
        when(this.orderServiceClient.getOrder(anyLong()))
                .thenReturn(Optional.empty());

        // Act and Assert
        Assertions.assertThrows(
                ReviewException.class,
                () -> this.reviewService.addOne(this.reviewCreateDto),
                ReviewException.ORDER_DOES_NOT_EXIST
        );
    }

    @Test
    void addOne_WhenProductNotPresentInOrder_ShouldThrowReviewException() {
        // Arrange
        var orderDto = OrderDto.builder()
                               .userId(this.contextHolder.getUserId())
                               .products(new ArrayList<>())
                               .build();

        when(this.reviewRepository.existsByUserIdAndOrderIdAndProductId(anyLong(), anyLong(), anyLong()))
                .thenReturn(false);
        when(this.orderServiceClient.getOrder(anyLong()))
                .thenReturn(Optional.of(orderDto));

        // Act & Assert
        Assertions.assertThrows(
                ReviewException.class,
                () -> this.reviewService.addOne(this.reviewCreateDto),
                ReviewException.PRODUCT_NOT_PRESENT_IN_ORDER
        );
    }

    @Test
    void getAllByUser() {
        //Arrange
        var expectedReviews = List.of(this.reviewDto);

        when(this.reviewRepository.findByUserId(anyLong()))
                .thenReturn(new ArrayList<>());
        when(this.reviewMapper.toDto(anyList()))
                .thenReturn(expectedReviews);

        //Act
        var actualReviews = this.reviewService.getAllByUser();

        //Assert
        Assertions.assertEquals(expectedReviews, actualReviews);
    }

    @Test
    void getAllByProduct_WhenSingleReview_ShouldReturnList() {
        //Arrange
        var expectedReviews = List.of(this.reviewDto);

        when(this.reviewRepository.findByProductId(anyLong()))
                .thenReturn(List.of(this.review));
        when(this.reviewMapper.toDto(anyList()))
                .thenReturn(expectedReviews);

        //Act
        var actualReviews = this.reviewService.getAllByProduct(anyLong());

        //Assert
        Assertions.assertEquals(expectedReviews, actualReviews);
    }

    @Test
    void deleteOne_WhenReviewExists_ShouldDeleteReview() {
        //Arrange
        when(this.reviewRepository.deleteOneById(anyLong()))
                .thenReturn(1);
        //Act
        var hasDeleted = this.reviewService.deleteOne(this.reviewDto.id());

        //Assert
        Assertions.assertTrue(hasDeleted);
    }

    @Test
    void deleteOne_WhenReviewDoesNotExists_ShouldThrowReviewException() {
        //Arrange
        when(this.reviewRepository.deleteOneById(anyLong()))
                .thenReturn(0);

        var hasDeleted = this.reviewService.deleteOne(this.reviewDto.id());

        //Assert
        Assertions.assertFalse(hasDeleted);
    }

    @Test
    void getTopAvgRatedProducts() {
        //Arrange

        var review1 = Review.builder()
                            .rating(5)
                            .productId(1L)
                            .orderId(1L)
                            .build();
        var review2 = Review.builder()
                            .rating(4)
                            .productId(1L)
                            .orderId(1L)
                            .build();
        var review3 = Review.builder()
                            .rating(3)
                            .productId(2L)
                            .orderId(1L)
                            .build();

        var expectedTopRatedProducts = List.of(
                new ProductAvgRatDto(1L, BigDecimal.valueOf(4.5)),
                new ProductAvgRatDto(2L, BigDecimal.valueOf(3.0))
        );

        when(this.reviewRepository.findAll())
                .thenReturn(List.of(review1, review2, review3));

        //Act
        var actualTopRatedProducts = this.reviewService.getTopAvgRatedProducts(5);

        //Assert
        Assertions.assertEquals(expectedTopRatedProducts, actualTopRatedProducts);
    }
}