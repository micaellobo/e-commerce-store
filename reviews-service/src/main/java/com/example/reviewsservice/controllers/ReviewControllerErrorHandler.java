package com.example.reviewsservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ReviewControllerErrorHandler {

    @ExceptionHandler(ReviewException.class)
    public ResponseEntity<ProblemDetail> onReviewException(ReviewException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        if (exception.statusCode != null) {
            status = exception.statusCode;
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

        return ResponseEntity.status(status)
                             .body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> onMethodArgumentNotValidException(
            HttpServletRequest request,
            MethodArgumentNotValidException ex
    ) {
        var fieldError = ex.getBindingResult()
                           .getFieldErrors()
                           .get(0);
        var message = fieldError.getDefaultMessage();
        var field = fieldError.getField();

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, field + " " + message);

        problemDetail.setTitle("Validation error");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(problemDetail);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> onMissingRequestHeaderException(MissingRequestHeaderException ex) {

        var httpStatus = HttpStatus.BAD_REQUEST;
        var message = ex.getMessage();
        var headerName = ex.getHeaderName();

        if (headerName.equals("username")) {
            httpStatus = HttpStatus.UNAUTHORIZED;
            message = HttpStatus.UNAUTHORIZED.getReasonPhrase();
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, message);

        return ResponseEntity.status(httpStatus)
                             .body(problemDetail);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> onAuthException(AuthException ex) {
        return ResponseEntity.status(ex.httpStatus)
                             .build();
    }
}
