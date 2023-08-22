package com.example.orderservice.controllers;

import org.springframework.http.HttpStatusCode;

public class OrderException extends RuntimeException {

    public static final String PRODUCT_DOES_NOT_EXIST = "Product does not exist";
    public static final String STOCK_NOT_AVAILABLE = "There is not stock available for some products";
    public static final String ERROR_UPDATE_STOCK = "Error updating stock";
    public static final String ORDER_DOES_NOT_EXIST = "Order does not exist";
    public HttpStatusCode statusCode;

    public OrderException(final String s) {
        super(s);
        this.statusCode = null;
    }

    public OrderException(final HttpStatusCode statusCode) {
        super();
        this.statusCode = statusCode;
    }
}
