package com.example.inventoryservice.controllers;

public class InventoryException extends RuntimeException {

    public static final String QUANTITY_LOWER_ZERO = "Quantity can't be lower than 0";
    public static final String PRODUCT_DOES_NOT_EXIST = "That product does not exists";
    public static final String ALREADY_EXISTS_USER = "Already exists an user with that name";

    public InventoryException(final String s) {
    }
}
