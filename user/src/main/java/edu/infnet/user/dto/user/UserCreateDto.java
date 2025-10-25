package edu.infnet.user.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank
        @Size(max = 64)
        String firstName,
        @NotBlank
        @Size(max = 64)
        String lastName,
        @Email
        @Size(max = 255)
        String email,
        @NotBlank
        String role,
        @NotBlank
        String password) {
}
