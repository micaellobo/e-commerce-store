package com.example.userservice.dto;

import com.example.userservice.models.User;
import lombok.Builder;

import java.io.Serializable;

/**
 * DTO for {@link User}}
 */
@Builder
public record UserDto(
        Long id,
        String name,
        String email,
        String username
) implements Serializable {
}