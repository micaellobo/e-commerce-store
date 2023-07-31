package com.example.reviewsservice.controllers;

public class ReviewException extends RuntimeException {

    public static final String REVIEW_DOES_NOT_EXISTS = "Review does not exists";
    public static final String PRODUCT_DOES_NOT_EXISTS = "Product does not exists";

    public ReviewException(final String s) {
        super(s);
    }
}
