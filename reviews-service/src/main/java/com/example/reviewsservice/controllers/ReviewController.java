package com.example.reviewsservice.controllers;

import com.example.reviewsservice.dtos.*;
import com.example.reviewsservice.services.IReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping({ReviewController.CONTROLLER_PATH})
public class ReviewController {

    public static final String CONTROLLER_PATH = "api/v1/reviews";

    private final IReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> add(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @RequestHeader("username") String username,
            @Valid @RequestBody ReviewCreateDto reviewCreateDto) {

//        Permitir apenas 1 review por cada compra

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, reviewCreateDto);

        var review = reviewService.add(reviewCreateDto);

//        return ResponseEntity.created(URI.create("/" + CONTROLLER_PATH + review.getId())).build();
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("{reviewId}")
    public ResponseEntity<ReviewDto> getOneById(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @RequestHeader("userId") Long userId,
            @RequestHeader("username") String username,
            @PathVariable Long reviewId) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, null);

        var reviews = reviewService.getOneByUserAndId(userId, reviewId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("products/{productId}")
    public ResponseEntity<List<ReviewDto>> getByProduct(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @PathVariable Long productId) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, null, null);

        var reviews = reviewService.getByProduct(productId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("products/top")
    public ResponseEntity<Object> getTopByProduct(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @RequestParam(required = false, defaultValue = "5") int max) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, null, null);

        var reviews = reviewService.getTopAvgRatedProducts(max);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("users/{userId}")
    public ResponseEntity<List<ReviewDto>> getByUser(
            HttpServletRequest request,
            @RequestHeader("CorrelationId") String correlationId,
            @RequestHeader("username") String username,
            @PathVariable Long userId) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, null);

        var reviews = reviewService.getByUser(userId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("users/{userId}/products/{productId}")
    public ResponseEntity<List<ReviewDto>> getByUserAndProduct(
            final HttpServletRequest request,
            @RequestHeader("CorrelationId") final String correlationId,
            @RequestHeader("username") final String username,
            @PathVariable final Long userId,
            @PathVariable final Long productId) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, null);

        var reviews = reviewService.getByUserAndProduct(userId, productId);

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("{reviewId}")
    public ResponseEntity<List<ReviewDto>> delete(
            final HttpServletRequest request,
            @RequestHeader("CorrelationId") final String correlationId,
            @RequestHeader("username") final String username,
            @PathVariable final Long reviewId) {

        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), correlationId, username, null);

        var hasDeleted = reviewService.delete(reviewId);

        if (hasDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }
}
