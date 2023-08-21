package com.example.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class AuthException extends RuntimeException {
    public static final String USERNAME_AND_EMAIL_NULL = "You must specify either an email or an username";
    public static final String NOT_FOUND = "User not found";
    public static final String GENERIC_LOGIN_FAIL = "User not found or password is wrong";
    public HttpStatusCode statusCode;

    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }

    public AuthException(final HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

}
