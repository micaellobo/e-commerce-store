package com.example.inventoryservice.services;

import com.example.inventoryservice.dtos.ProductCreateDto;
import com.example.inventoryservice.dtos.ProductStockQuantityDto;
import com.example.inventoryservice.models.Product;

import java.util.List;

public interface IProductService {

    Product add(ProductCreateDto productCreateDto);

    List<Product> getAll();

    void increaseStock(Long productId,
                       ProductStockQuantityDto productStockQuantityDto);

    void decreaseStock(Long productId,
                       ProductStockQuantityDto productStockQuantityDto);

    Product getOneBy(Long id);
}
