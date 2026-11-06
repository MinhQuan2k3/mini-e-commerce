package com.miniecommerce.backend.user.repository;

import com.miniecommerce.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}
