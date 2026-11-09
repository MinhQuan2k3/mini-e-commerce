package com.miniecommerce.backend.category.dto;

import com.miniecommerce.backend.category.entity.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        String name,

        String description,

        @NotNull(message = "Category status is required")
        CategoryStatus status
// DTO này dùng chung cho cả thao tác tạo mới và cập nhật Category.
) {
}
