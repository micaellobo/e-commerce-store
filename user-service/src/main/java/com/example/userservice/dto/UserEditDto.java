package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link com.example.userservice.models.User}
 */
@Builder
public record UserEditDto(
        Long id,
        @NotNull @NotEmpty @NotBlank
        String name,
        @NotNull @Size(min = 10) @NotEmpty @NotBlank String password
) implements Serializable {
}