package com.miniecommerce.backend.auth.controller;

import com.miniecommerce.backend.auth.dto.AuthUserResponse;
import com.miniecommerce.backend.auth.dto.LoginRequest;
import com.miniecommerce.backend.auth.dto.LoginResponse;
import com.miniecommerce.backend.auth.dto.RegisterRequest;
import com.miniecommerce.backend.auth.dto.RegisterResponse;
import com.miniecommerce.backend.auth.service.AuthService;
import com.miniecommerce.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "Registration successful",
                        response
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthUserResponse>> getCurrentUser(
            @AuthenticationPrincipal Jwt jwt
    ) {
        AuthUserResponse response = authService.getCurrentUser(
                jwt.getSubject()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Current user retrieved successfully", response)
        );
    }
}
