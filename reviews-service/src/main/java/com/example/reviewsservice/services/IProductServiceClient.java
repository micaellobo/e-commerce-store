package com.example.reviewsservice.services;

import com.example.reviewsservice.dtos.ProductDto;

import java.util.Optional;

public interface IProductServiceClient {
    Optional<ProductDto> getProductById(Long id);
}
