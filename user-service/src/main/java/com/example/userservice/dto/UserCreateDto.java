package com.example.userservice.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.example.userservice.models.User}
 */
@Builder
public record UserCreateDto(
        @NotNull @NotEmpty @NotBlank
        String name,
        @NotNull @Email
        String email,
        @NotNull @Size(min = 4) @NotEmpty @NotBlank
        String username,
        @NotNull @Size(min = 10) @NotEmpty @NotBlank
        String password)
        implements Serializable {
}