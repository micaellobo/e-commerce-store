package com.example.authservice.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;

/**
 * A DTO for the {@link com.example.authservice.models.User} entity
 */
public record LoginDto(
        @Pattern(regexp = "^[^@]+$")
        String username,
        @NotNull
        String password) implements Serializable {
}