package com.example.inventoryservice.repository;

import com.example.inventoryservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);


}