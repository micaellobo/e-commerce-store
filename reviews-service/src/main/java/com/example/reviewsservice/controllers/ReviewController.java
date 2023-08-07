package com.example.reviewsservice.controllers;

import com.example.reviewsservice.config.CustomContextHolder;
import com.example.reviewsservice.config.RequiresAuthentication;
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
@RequestMapping({"api/v1/reviews"})
public class ReviewController {

    private final IReviewService reviewService;
    private final CustomContextHolder contextHolder;

    @PostMapping("users/me")
    @RequiresAuthentication
    public ResponseEntity<ReviewDto> add(
            final HttpServletRequest request,
            @Valid @RequestBody ReviewCreateDto reviewCreateDto) {

        logRequest(request, reviewCreateDto);

        var review = reviewService.add(reviewCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @GetMapping("users/me")
    @RequiresAuthentication
    public ResponseEntity<List<ReviewDto>> getAllByUser(final HttpServletRequest request) {

        logRequest(request, null);

        var reviews = reviewService.getByUser();

        return ResponseEntity.ok(reviews);
    }

    @DeleteMapping("users/me/{reviewId}")
    @RequiresAuthentication
    public ResponseEntity<List<ReviewDto>> deleteOne(
            final HttpServletRequest request,
            @PathVariable final Long reviewId) {

        logRequest(request, null);

        var hasDeleted = reviewService.delete(reviewId);

        if (hasDeleted)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("products/{productId}")
    public ResponseEntity<List<ReviewDto>> getAllByProduct(
            final HttpServletRequest request,
            @PathVariable Long productId) {

        logRequest(request, null);

        var reviews = reviewService.getAllByProduct(productId);

        return ResponseEntity.ok(reviews);
    }

    @GetMapping("products/top")
    public ResponseEntity<Object> getTopByProduct(
            final HttpServletRequest request,
            @RequestParam(required = false, defaultValue = "5") int max) {

        logRequest(request, null);

        var reviews = reviewService.getTopAvgRatedProducts(max);

        return ResponseEntity.ok(reviews);
    }


    private void logRequest(final HttpServletRequest request, final Object obj) {
        log.info("{} - {} - {} - {} - {}", request.getMethod(), request.getRequestURI(), contextHolder.getCorrelationId(), contextHolder.getUsername(), obj);
    }
}
