package com.example.userservice.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record LoginDto(
        String username,
        @NotNull
        String password) implements Serializable {
}