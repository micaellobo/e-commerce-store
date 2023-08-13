package com.example.inventoryservice.controllers;

import org.springframework.http.HttpStatusCode;

public class ProductException extends RuntimeException {

    public static final String QUANTITY_LOWER_ZERO = "Quantity can't be lower than 0";
    public static final String PRODUCT_DOES_NOT_EXIST = "That product does not exists";
    public static final String ALREADY_EXISTS_PRODUCT = "Already exists an product with that name";
    public static final String SOME_IDS_DOES_NOT_EXIST = "Some id's does not exist";
    public HttpStatusCode statusCode;

    public ProductException(final String s) {
        super(s);
    }

    public ProductException(final HttpStatusCode statusCode) {
        this.statusCode = statusCode;
    }
}
