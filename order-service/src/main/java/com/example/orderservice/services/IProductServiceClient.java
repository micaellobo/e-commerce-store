package com.example.orderservice.services;


import com.example.orderservice.dtos.OrderProductCreateDto;
import com.example.orderservice.dtos.ProductDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IProductServiceClient {
    Optional<ProductDto> getProductById(Long id);

    Map<Long, ProductDto> getProductById(List<Long> ids);

    boolean updateStock(List<OrderProductCreateDto> ids);
}
