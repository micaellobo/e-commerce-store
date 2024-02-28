package com.example.reviewsservice.controllers;

import com.example.reviewsservice.config.ContextHolder;
import com.example.reviewsservice.config.RequiresAuthentication;
import com.example.reviewsservice.dtos.ProductAvgRatDto;
import com.example.reviewsservice.dtos.ReviewCreateDto;
import com.example.reviewsservice.dtos.ReviewDto;
import com.example.reviewsservice.services.IReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping({"api/v1/reviews"})
@Tag(name = "Review")
public class ReviewController {

    private final IReviewService reviewService;
    private final ContextHolder contextHolder;

    /**
     * Create a new review for the current user
     *
     * @param request         The HttpServletRequest.
     * @param reviewCreateDto The review to create.
     * @return The created review.
     */
    @Operation(
            summary = "Create a new review for the current user",
            responses = {
                    @ApiResponse(responseCode = "201",
                                 content = {
                                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                  schema = @Schema(implementation = ReviewDto.class))
                                 }),
                    @ApiResponse(responseCode = "400",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProblemDetail.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping("users/me")
    @RequiresAuthentication
    public ResponseEntity<ReviewDto> add(
            final HttpServletRequest request,
            @Valid
            @RequestBody
            ReviewCreateDto reviewCreateDto
    ) {

        this.logRequest(request, reviewCreateDto);

        var review = this.reviewService.addOne(reviewCreateDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(review);
    }

    /**
     * Get all reviews for the current user
     *
     * @param request The HttpServletRequest.
     * @return The list of reviews.
     */
    @Operation(
            summary = "Get all reviews for the current user",
            responses = {
                    @ApiResponse(responseCode = "200",
                                 content = {
                                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                  array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class)))
                                 }),
                    @ApiResponse(responseCode = "400",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProblemDetail.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("users/me")
    @RequiresAuthentication
    public ResponseEntity<List<ReviewDto>> getAllByUser(final HttpServletRequest request) {

        this.logRequest(request, null);

        var reviews = this.reviewService.getAllByUser();

        return ResponseEntity.ok(reviews);
    }

    /**
     * Get the review by id for the current user
     *
     * @param request  The HttpServletRequest.
     * @param reviewId The review id.
     * @return The review.
     */
    @Operation(
            summary = "Get the review by id for the current user",
            responses = {
                    @ApiResponse(responseCode = "204",
                                 content = {
                                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                  schema = @Schema(implementation = ReviewDto.class))
                                 }),
                    @ApiResponse(responseCode = "400",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProblemDetail.class)))
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("users/me/{reviewId}")
    @RequiresAuthentication
    public ResponseEntity<Object> deleteOne(
            final HttpServletRequest request,
            @PathVariable
            final Long reviewId
    ) {

        this.logRequest(request, null);

        var hasDeleted = this.reviewService.deleteOne(reviewId);

        if (hasDeleted) {
            return ResponseEntity.noContent()
                                 .build();
        } else {
            return ResponseEntity.notFound()
                                 .build();
        }
    }

    /**
     * Get all reviews for the product
     *
     * @param request   The HttpServletRequest.
     * @param productId The product id.
     * @return The list of reviews.
     */
    @Operation(
            summary = "Get all reviews for the product",
            responses = {
                    @ApiResponse(responseCode = "200",
                                 content = {
                                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                  array = @ArraySchema(schema = @Schema(implementation = ReviewDto.class)))
                                 }),
                    @ApiResponse(responseCode = "400",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("products/{productId}")
    public ResponseEntity<List<ReviewDto>> getAllByProduct(
            final HttpServletRequest request,
            @PathVariable
            Long productId
    ) {

        this.logRequest(request, null);

        var reviews = this.reviewService.getAllByProduct(productId);

        return ResponseEntity.ok(reviews);
    }

    /**
     * Get the top reviews for the product
     *
     * @param request The HttpServletRequest.
     * @param max     The max number of reviews.
     * @return The list of avg rating by product.
     */
    @Operation(
            summary = "Get the top reviews for the product",
            responses = {
                    @ApiResponse(responseCode = "200",
                                 content = {
                                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                  array = @ArraySchema(schema = @Schema(implementation = ProductAvgRatDto.class)))
                                 }),
                    @ApiResponse(responseCode = "400",
                                 content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ProblemDetail.class)))
            }
    )
    @GetMapping("products/top")
    public ResponseEntity<List<ProductAvgRatDto>> getTopByProduct(
            final HttpServletRequest request,
            @RequestParam(required = false,
                          defaultValue = "5")
            int max
    ) {

        this.logRequest(request, null);

        var reviews = this.reviewService.getTopAvgRatedProducts(max);

        return ResponseEntity.ok(reviews);
    }


    private void logRequest(
            final HttpServletRequest request,
            final Object obj
    ) {
        log.info(
                "{} - {} - {} - {} - {}",
                request.getMethod(),
                request.getRequestURI(),
                this.contextHolder.getCorrelationId(),
                this.contextHolder.getUsername(),
                obj
        );
    }
}
