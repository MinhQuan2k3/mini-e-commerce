package com.miniecommerce.backend.auth.service;

import com.miniecommerce.backend.auth.dto.RegisterRequest;
import com.miniecommerce.backend.auth.dto.RegisterResponse;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.UnauthorizedException;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        authService = new AuthService(userRepository);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void shouldRegisterCustomerSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "customer@example.com",
                "password123"
        );

        when(userRepository.existsByEmail("customer@example.com"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse response = authService.register(request);

        assertEquals("customer@example.com", response.email());
        assertEquals(UserRole.CUSTOMER, response.role());

        verify(userRepository).existsByEmail("customer@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenRegisteringDuplicateEmail() {
        RegisterRequest request = new RegisterRequest(
                "customer@example.com",
                "password123"
        );

        when(userRepository.existsByEmail("customer@example.com"))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> authService.register(request)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository).existsByEmail("customer@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldHashPasswordBeforeSavingUser() {
        RegisterRequest request = new RegisterRequest(
                "customer@example.com",
                "password123"
        );

        when(userRepository.existsByEmail("customer@example.com"))
                .thenReturn(false);

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        authService.register(request);

        verify(userRepository).save(argThat(user ->
                !user.getPasswordHash().equals("password123")
                        && passwordEncoder.matches(
                        "password123",
                        user.getPasswordHash()
                )
        ));
    }

    @Test
    void shouldAuthenticateWithCorrectCredentials() {
        String password = "password123";
        String passwordHash = passwordEncoder.encode(password);

        User user = new User(
                "customer@example.com",
                passwordHash,
                UserRole.CUSTOMER
        );

        when(userRepository.findByEmail("customer@example.com"))
                .thenReturn(Optional.of(user));

        User authenticatedUser = authService.authenticate(
                "customer@example.com",
                password
        );

        assertSame(user, authenticatedUser);
        assertEquals("customer@example.com", authenticatedUser.getEmail());
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenPasswordIsIncorrect() {
        String passwordHash = passwordEncoder.encode("password123");

        User user = new User(
                "customer@example.com",
                passwordHash,
                UserRole.CUSTOMER
        );

        when(userRepository.findByEmail("customer@example.com"))
                .thenReturn(Optional.of(user));

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.authenticate(
                        "customer@example.com",
                        "wrongpassword"
                )
        );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenEmailDoesNotExist() {
        when(userRepository.findByEmail("unknown@example.com"))
                .thenReturn(Optional.empty());

        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.authenticate(
                        "unknown@example.com",
                        "password123"
                )
        );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );
    }
}
