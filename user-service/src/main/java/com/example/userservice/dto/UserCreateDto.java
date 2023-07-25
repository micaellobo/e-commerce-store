package com.example.userservice.dto;

import jakarta.validation.constraints.*;

import java.io.Serializable;

/**
 * DTO for {@link com.example.userservice.models.User}
 */
public record UserCreateDto(
        @NotNull @NotEmpty @NotBlank
        String name,
        @NotNull @NotEmpty @NotBlank
        String lastName,
        @NotNull @Email
        String email,
        @NotNull @Size(min = 4) @NotEmpty @NotBlank
        String username,
        @NotNull @Size(min = 10) @NotEmpty @NotBlank
        String password)
        implements Serializable {
}