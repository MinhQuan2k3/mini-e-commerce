package com.miniecommerce.backend.auth.dto;

import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;

public record AuthUserResponse(
        Long id,
        String email,
        UserRole role
) {

    public static AuthUserResponse fromEntity(User user) {
        return new AuthUserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
