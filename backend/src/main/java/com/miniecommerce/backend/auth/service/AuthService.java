package com.miniecommerce.backend.auth.service;

import com.miniecommerce.backend.auth.dto.AuthUserResponse;
import com.miniecommerce.backend.auth.dto.LoginRequest;
import com.miniecommerce.backend.auth.dto.LoginResponse;
import com.miniecommerce.backend.auth.dto.RegisterRequest;
import com.miniecommerce.backend.auth.dto.RegisterResponse;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.UnauthorizedException;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private static final Logger log =
            LoggerFactory.getLogger(AuthService.class);

    private static final String INVALID_CREDENTIALS_MESSAGE =
            "Invalid email or password";

    private static final String INVALID_TOKEN_MESSAGE =
            "Invalid access token";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim();

        if (userRepository.existsByEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                email,
                passwordHash,
                UserRole.CUSTOMER
        );

        User savedUser = userRepository.save(user);
        log.info("Customer registered: userId={}", savedUser.getId());

        return RegisterResponse.fromEntity(savedUser);
    }

    @Transactional(readOnly = true)
    public User authenticate(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
        }

        if (password == null) {
            throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
        }

        UserDetails userDetails;

        try {
            userDetails = userDetailsService.loadUserByUsername(email.trim());
        } catch (UsernameNotFoundException exception) {
            throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
        }

        if (!(userDetails instanceof CustomUserDetails customUserDetails)) {
            throw new IllegalStateException(
                    "UserDetailsService returned an unsupported UserDetails implementation"
            );
        }

        return customUserDetails.getUser();
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        User user = authenticate(
                request.email(),
                request.password()
        );

        String accessToken = jwtService.generateToken(user);

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtService.getExpiration(),
                AuthUserResponse.fromEntity(user)
        );
    }

    @Transactional(readOnly = true)
    public AuthUserResponse getCurrentUser(String subject) {
        if (subject == null || subject.isBlank()) {
            throw new UnauthorizedException(INVALID_TOKEN_MESSAGE);
        }

        Long userId;

        try {
            userId = Long.valueOf(subject);
        } catch (NumberFormatException exception) {
            throw new UnauthorizedException(INVALID_TOKEN_MESSAGE);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException(
                        INVALID_TOKEN_MESSAGE
                ));

        return AuthUserResponse.fromEntity(user);
    }
}
