package com.miniecommerce.backend.category.dto;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;

import java.time.LocalDateTime;

public record AdminCategoryResponse(
        Long id,
        String name,
        String description,
        CategoryStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static AdminCategoryResponse fromEntity(Category category) {
        return new AdminCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
