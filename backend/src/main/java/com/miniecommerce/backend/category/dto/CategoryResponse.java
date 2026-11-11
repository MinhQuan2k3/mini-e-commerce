package com.miniecommerce.backend.category.dto;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;

public record CategoryResponse(
        Long id,
        String name,
        String description,
        CategoryStatus status
) {

    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus()
        );
    }
}
// DTO này giúp API chỉ trả về những trường được quy định trong API Specification, thay vì trả trực tiếp Entity.
