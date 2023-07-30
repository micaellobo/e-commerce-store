package com.example.inventoryservice.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Column(precision = 20, scale = 2)
    private BigDecimal price;

    @NotNull
    private int quantity;

    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var user = (Product) o;
        return id.equals(user.id) || name.equals(user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
