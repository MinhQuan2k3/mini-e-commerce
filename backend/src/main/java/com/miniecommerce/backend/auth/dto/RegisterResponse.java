package com.miniecommerce.backend.auth.dto;

import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;

import java.time.LocalDateTime;

public record RegisterResponse(
        Long id,
        String email,
        UserRole role,
        LocalDateTime createdAt
) {

    public static RegisterResponse fromEntity(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
