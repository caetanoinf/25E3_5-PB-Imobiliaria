package edu.infnet.user.controller;

import edu.infnet.user.dto.auth.LoginResponseDto;
import edu.infnet.user.dto.user.UserCreateDto;
import edu.infnet.user.dto.auth.UserLoginDto;
import edu.infnet.user.dto.user.UserReadDto;
import edu.infnet.user.dto.user.UserUpdateDto;
import edu.infnet.user.service.KeycloakService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakService keycloakService;
    private static final String X_REFRESH_TOKEN = "X-Refresh-Token";

    @PostMapping("/sign-up")
    public ResponseEntity<UserReadDto> register(@RequestBody @Valid UserCreateDto userCreateDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(keycloakService.createUser(userCreateDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto userLoginDto) {
        LoginResponseDto dto = keycloakService.authenticate(userLoginDto);
        String tokenTypeWithWhitespace = dto.tokenType() + " ";
        String cookie = String.format(X_REFRESH_TOKEN + "=%s; HttpOnly; Secure; Path=/; Max-Age=%d",
                tokenTypeWithWhitespace + dto.refreshToken(), dto.refreshExpiresIn());

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, tokenTypeWithWhitespace + dto.accessToken())
                .header(HttpHeaders.SET_COOKIE, cookie)
                .build();
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserReadDto> getMe() {
        return keycloakService.getCurrentUser()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserReadDto> getUser(@PathVariable("id") String id) {
        return ResponseEntity.ok(keycloakService.findUserById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserReadDto> updateUser(@PathVariable("id") String id,
                                                  @RequestBody @Valid UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(keycloakService.updateUser(id, userUpdateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") String id) {
        keycloakService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
