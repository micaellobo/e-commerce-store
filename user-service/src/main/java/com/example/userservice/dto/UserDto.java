package com.example.userservice.dto;

import com.example.userservice.models.User;

import java.io.Serializable;

/**
 * DTO for {@link User}}
 */
public record UserDto(
        Long id,
        String name,
        String email,
        String username
) implements Serializable {
}