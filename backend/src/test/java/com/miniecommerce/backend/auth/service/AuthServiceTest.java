package com.miniecommerce.backend.auth.service;

import com.miniecommerce.backend.auth.dto.RegisterRequest;
import com.miniecommerce.backend.auth.dto.RegisterResponse;
import com.miniecommerce.backend.auth.dto.LoginRequest;
import com.miniecommerce.backend.auth.dto.LoginResponse;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.UnauthorizedException;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private UserDetailsService userDetailsService;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userDetailsService = mock(UserDetailsService.class);
        passwordEncoder = new BCryptPasswordEncoder();
        jwtService = mock(JwtService.class);

        authService = new AuthService(
                userRepository,
                passwordEncoder,
                userDetailsService,
                jwtService
        );
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

        when(userDetailsService.loadUserByUsername("customer@example.com"))
                .thenReturn(new CustomUserDetails(user));

        User authenticatedUser = authService.authenticate(
                "customer@example.com",
                password
        );

        assertSame(user, authenticatedUser);
        assertEquals("customer@example.com", authenticatedUser.getEmail());

        verify(userDetailsService).loadUserByUsername("customer@example.com");
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenPasswordIsIncorrect() {
        String passwordHash = passwordEncoder.encode("password123");

        User user = new User(
                "customer@example.com",
                passwordHash,
                UserRole.CUSTOMER
        );

        when(userDetailsService.loadUserByUsername("customer@example.com"))
                .thenReturn(new CustomUserDetails(user));

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
        when(userDetailsService.loadUserByUsername("unknown@example.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));

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

    @Test
    void shouldThrowUnauthorizedExceptionWhenEmailIsBlank() {
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.authenticate(" ", "password123")
        );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldThrowUnauthorizedExceptionWhenPasswordIsNull() {
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authService.authenticate(
                        "customer@example.com",
                        null
                )
        );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verifyNoInteractions(userDetailsService);
    }

    @Test
    void shouldLoginSuccessfullyAndGenerateAccessToken() {
        String password = "password123";
        String passwordHash = passwordEncoder.encode(password);

        User user = new User(
                "customer@example.com",
                passwordHash,
                UserRole.CUSTOMER
        );

        when(userDetailsService.loadUserByUsername("customer@example.com"))
                .thenReturn(new CustomUserDetails(user));

        when(jwtService.generateToken(user))
                .thenReturn("test-access-token");

        when(jwtService.getExpiration())
                .thenReturn(3600L);

        LoginResponse response = authService.login(
                new LoginRequest("customer@example.com", password)
        );

        assertEquals("test-access-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(3600L, response.expiresIn());
        assertEquals("customer@example.com", response.user().email());
        assertEquals(UserRole.CUSTOMER, response.user().role());

        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldNotGenerateTokenWhenLoginFails() {
        when(userDetailsService.loadUserByUsername("unknown@example.com"))
                .thenThrow(new UsernameNotFoundException("User not found"));

        assertThrows(
                UnauthorizedException.class,
                () -> authService.login(
                        new LoginRequest("unknown@example.com", "password123")
                )
        );

        verify(jwtService, never()).generateToken(any(User.class));
    }
}
