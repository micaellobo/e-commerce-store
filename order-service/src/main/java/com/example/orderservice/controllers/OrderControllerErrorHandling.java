package com.example.orderservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class OrderControllerErrorHandling {


    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ProblemDetail> OnUserException(OrderException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        if (exception.statusCode != null) {
            status = exception.statusCode;
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

        return buildResponseEntity(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> OnUserException(MethodArgumentNotValidException ex) {
        var message = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, message);

        return buildResponseEntity(problemDetail);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> OnAuthException(AuthException ex) {
        return ResponseEntity.status(ex.httpStatus).build();
    }

    private ResponseEntity<ProblemDetail> buildResponseEntity(ProblemDetail problemDetail) {
        return ResponseEntity.status(problemDetail.getStatus()).body(problemDetail);
    }

//    @ExceptionHandler(Exception.class)
//    ResponseEntity<ProblemDetail> onException(HttpServletRequest req, Exception exc) {
//        var problemDetail = ProblemDetail.forStatus(500);
//        problemDetail.setType(URI.create(req.getRequestURI()));
//        problemDetail.setTitle(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//        return buildResponseEntity(problemDetail);
//    }
}
