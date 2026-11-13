package com.miniecommerce.backend.auth.service;

import com.miniecommerce.backend.auth.dto.RegisterRequest;
import com.miniecommerce.backend.auth.dto.RegisterResponse;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.UnauthorizedException;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UnauthorizedException(
                        INVALID_CREDENTIALS_MESSAGE
                ));

        if (password == null || !passwordEncoder.matches(
                password,
                user.getPasswordHash()
        )) {
            throw new UnauthorizedException(INVALID_CREDENTIALS_MESSAGE);
        }

        return user;
    }
}
