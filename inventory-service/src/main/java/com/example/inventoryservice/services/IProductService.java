package com.example.inventoryservice.services;

import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;

import java.util.List;

public interface IProductService {

    ProductDto addOne(ProductCreateDto productCreateDto);

    List<ProductDto> getAll();

    void increaseStock(List<ProductStockQuantityDto> productsQuantities);

    void decreaseStock(List<ProductStockQuantityDto> productsQuantities);

    ProductDto getOneById(Long id);

    List<ProductDto> getAllByIds(List<Long> ids);
}
