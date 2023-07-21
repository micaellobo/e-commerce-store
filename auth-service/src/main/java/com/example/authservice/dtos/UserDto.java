package com.example.authservice.dtos;

import com.example.authservice.models.User;

import java.io.Serializable;

/**
 * DTO for {@link User}}
 */
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String username
) implements Serializable {
}