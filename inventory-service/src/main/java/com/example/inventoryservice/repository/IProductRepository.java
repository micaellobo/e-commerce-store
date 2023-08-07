package com.example.inventoryservice.repository;

import com.example.inventoryservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);


}