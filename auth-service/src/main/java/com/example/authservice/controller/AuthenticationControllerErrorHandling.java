package com.example.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthenticationControllerErrorHandling {

    @ExceptionHandler(AuthException.class)
    public ProblemDetail OnUserException(AuthException exception) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ProblemDetail> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> OnUserException(MissingRequestHeaderException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());

        return ResponseEntity.status(httpStatus).body(problemDetail);
    }
}
