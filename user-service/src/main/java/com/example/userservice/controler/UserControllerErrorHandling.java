package com.example.userservice.controler;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

@ControllerAdvice
public class UserControllerErrorHandling {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ProblemDetail> OnUserException(UserException exception) {
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        if (exception.statusCode != null) {
            status = exception.statusCode;
        }

        var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

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

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ProblemDetail> OnAuthException(AuthException ex) {
        return ResponseEntity.status(ex.httpStatus).build();
    }

}
