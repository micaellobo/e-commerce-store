package com.example.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthControllerErrorHandling {

    @ExceptionHandler(AuthException.class)
    public ProblemDetail onAuthException(AuthException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        if (exception.statusCode != null) {
            status = exception.statusCode;
        }

        return ProblemDetail.forStatusAndDetail(status, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ProblemDetail> onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }


    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ProblemDetail> onMissingRequestHeaderException(MissingRequestHeaderException ex) {
        var httpStatus = HttpStatus.BAD_REQUEST;

        var problemDetail = ProblemDetail.forStatusAndDetail(httpStatus, ex.getMessage());

        return ResponseEntity.status(httpStatus).body(problemDetail);
    }
}
