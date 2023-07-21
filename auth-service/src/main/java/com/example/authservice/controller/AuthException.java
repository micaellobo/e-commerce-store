package com.example.authservice.controller;

public class AuthException extends RuntimeException{
    public static final String USERNAME_AND_EMAIL_NULL = "You must specify either an email or an username";
    public static final String NOT_FOUND = "User not found";
    public static final String GENERIC_LOGIN_FAIL = "User not found or password is wrong";

    public AuthException() {
    }

    public AuthException(String message) {
        super(message);
    }
}
