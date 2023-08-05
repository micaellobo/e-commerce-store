package com.example.orderservice.controllers;

public class OrderException extends RuntimeException {

    public static final String PRODUCT_DOES_NOT_EXIST = "Product does not exist";
    public static final String STOCK_NOT_AVAILABLE = "There is not stock available for some products";
    public static final String ERROR_UPDATE_STOCK = "Error updating stock";

    public OrderException(final String s) {
        super(s);
    }
}
