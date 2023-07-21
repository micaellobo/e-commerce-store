package com.example.userservice.controler;

public class UserException extends RuntimeException {

    public static final String USER_NOT_FOUND = "The user was not found";
    public static final String USER_ALREADY_EXISTS = "The user already exists";

    public UserException(final String message) {
        super(message);
    }
}
