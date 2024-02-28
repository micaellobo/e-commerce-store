package com.example.reviewsservice.dtos;

import java.io.Serializable;

public record UserDto(
        Long id,
        String name,
        String email,
        String username
)
        implements Serializable {
}