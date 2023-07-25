package com.example.inventoryservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @Column(precision = 20, scale = 2) // Define precision and scale as per your requirements
    private BigDecimal price;

    @NotNull
    private int quantity;

    private String description;

}
