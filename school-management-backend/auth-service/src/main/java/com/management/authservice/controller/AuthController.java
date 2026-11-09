package com.management.authservice.controller;



import com.management.authservice.dto.external.request.ExternalLoginRequest;
import com.management.authservice.dto.login.request.LoginRequest;
import com.management.authservice.dto.login.response.AuthResponse;
import com.management.authservice.dto.signup.request.SignUpRequest;
import com.management.authservice.dto.signup.response.SignUpResponse;
import com.management.authservice.dto.token.response.TokenValidationResponse;
import com.management.authservice.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signup(@Valid @RequestBody SignUpRequest req) {
        log.info("SignUp Request: {}", req.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        log.info("Login Request: {}", req);
        return ResponseEntity.ok(authService.login(req));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody Map<String, String> body) {
        log.info("Refresh Request: {}", body);
        String refresh = body.get("refreshToken");
        return ResponseEntity.ok(authService.refresh(refresh));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> body) {
        authService.logout(body.get("token"));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/validate")
    public ResponseEntity<TokenValidationResponse> validate(@RequestHeader("Authorization") String authHeader) {
        log.info("Validate Auth Request: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(TokenValidationResponse.builder()
                    .valid(false)
                    .message("Missing or invalid Authorization header")
                    .build());
        }
        String token = authHeader.substring(7);
        log.info("Validating token for user");
        return ResponseEntity.ok(authService.validateToken(token));
    }

    @PostMapping("/external")
    public ResponseEntity<AuthResponse> external(@RequestBody ExternalLoginRequest req) {
        // implement external provider validation, then create tokens same as login
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
