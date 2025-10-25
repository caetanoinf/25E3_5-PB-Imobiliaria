package edu.infnet.user.dto.user;

import java.util.UUID;

public record UserReadDto(
        Long id,
        UUID keycloakUuid,
        String username,
        String firstName,
        String lastName,
        String email,
        String about,
        String role) {
}
