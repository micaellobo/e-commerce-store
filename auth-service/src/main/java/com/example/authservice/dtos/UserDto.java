package com.example.authservice.dtos;


import java.io.Serializable;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username
) implements Serializable {
}