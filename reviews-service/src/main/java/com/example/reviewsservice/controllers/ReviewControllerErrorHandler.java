package com.example.reviewsservice.controllers;

import com.example.reviewsservice.models.Review;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReviewControllerErrorHandler {

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ProblemDetail> OnUserException(ReviewException exception) {

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> OnUserException(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> OnUserException(MissingRequestHeaderException ex) {

        var httpStatus = HttpStatus.BAD_REQUEST;
        var message = ex.getMessage();
        var headerName = ex.getHeaderName();

        if (headerName.equals("username")) {
            httpStatus = HttpStatus.UNAUTHORIZED;
            message = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);

        return ResponseEntity.status(httpStatus).body(problemDetail);
    }
}
