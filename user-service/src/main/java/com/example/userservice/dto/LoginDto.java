package com.example.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.io.Serializable;

@Builder
public record LoginDto(
        String username,
        @NotNull
        String password) implements Serializable {
}