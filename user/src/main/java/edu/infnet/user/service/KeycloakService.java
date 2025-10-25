package edu.infnet.user.service;

import edu.infnet.user.dto.auth.LoginResponseDto;
import edu.infnet.user.dto.user.UserCreateDto;
import edu.infnet.user.dto.auth.UserLoginDto;
import edu.infnet.user.mapper.KeycloakMapper;
import edu.infnet.user.property.KeycloakProperties;
import edu.infnet.user.dto.user.UserReadDto;
import edu.infnet.user.dto.user.UserUpdateDto;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeycloakService {

    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;
    private final KeycloakMapper keycloakMapper;
    private final RestTemplate restTemplate;

    public UserReadDto createUser(UserCreateDto userCreateDto) {
        UserRepresentation user = keycloakMapper.toKeycloakUser(userCreateDto);
        RealmResource realmResource = keycloak.realm(keycloakProperties.realm());
        UsersResource usersResource = realmResource.users();

        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == 409) {
                throw new RuntimeException("User already exists");
            }

            String userId = CreatedResponseUtil.getCreatedId(response);
            RoleRepresentation roleRepresentation = realmResource
                    .roles()
                    .get(userCreateDto.role())
                    .toRepresentation();

            usersResource
                    .get(userId)
                    .roles()
                    .realmLevel()
                    .add(Collections.singletonList(roleRepresentation));

            return findUserById(userId);
        }
    }

    public LoginResponseDto authenticate(UserLoginDto userLoginDto) {
        String tokenUrl = String.format("%s/realms/%s/protocol/openid-connect/token",
                keycloakProperties.authServerUrl(), keycloakProperties.realm());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> form = keycloakMapper.toForm(keycloakProperties, userLoginDto);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        form.forEach(map::add);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<LoginResponseDto> response = restTemplate.postForEntity(tokenUrl, request, LoginResponseDto.class);
        return response.getBody();
    }

    public UserReadDto findUserById(String userId) {
        UserRepresentation userRepresentation = keycloak.realm(keycloakProperties.realm())
                .users()
                .get(userId)
                .toRepresentation();
        return keycloakMapper.toUserReadDto(userRepresentation);
    }

    public UserReadDto updateUser(String userId, UserUpdateDto userUpdateDto) {
        UserResource userResource = keycloak.realm(keycloakProperties.realm())
                .users()
                .get(userId);

        UserRepresentation userRepresentation = userResource.toRepresentation();
        keycloakMapper.updateUserFromDto(userUpdateDto, userRepresentation);
        userResource.update(userRepresentation);

        return findUserById(userId);
    }

    public void deleteUser(String userId) {
        keycloak.realm(keycloakProperties.realm())
                .users()
                .delete(userId);
    }

    public Optional<UserReadDto> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return Optional.empty();
        }

        String userId = jwt.getSubject();
        return Optional.of(this.findUserById(userId));
    }
}
