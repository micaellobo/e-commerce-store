package com.example.inventoryservice.controllers;

import org.springframework.http.HttpStatus;

public class AuthException extends RuntimeException {

    public final HttpStatus httpStatus;

    public AuthException(final HttpStatus httpStatus) {

        this.httpStatus = httpStatus;
    }
}
