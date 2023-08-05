package com.example.inventoryservice.services;

import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.models.Product;

import java.util.List;

public interface IProductService {

    Product add(ProductCreateDto productCreateDto);

    List<Product> getAll();

    void increaseStock(List<ProductStockQuantityDto> productsQuantities);

    void decreaseStock(List<ProductStockQuantityDto> productsQuantities);

    Product getOneBy(Long id);

    List<Product> getByIds(List<Long> ids);
}
