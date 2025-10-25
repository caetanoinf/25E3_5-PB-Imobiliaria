package edu.infnet.user.dto.user;

import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(max = 64)
        String firstName,
        @Size(max = 64)
        String lastName,
        @Size(max = 255)
        String about) {
}
