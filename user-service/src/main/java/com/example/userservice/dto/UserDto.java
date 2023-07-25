package com.example.userservice.dto;

import com.example.userservice.models.User;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

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