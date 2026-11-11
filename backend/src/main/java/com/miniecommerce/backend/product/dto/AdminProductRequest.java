package com.miniecommerce.backend.product.dto;

import com.miniecommerce.backend.product.entity.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;

public record AdminProductRequest(

        @NotBlank(message = "SKU is required")
        @Size(max = 100, message = "SKU must not exceed 100 characters")
        String sku,

        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name must not exceed 255 characters")
        String name,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", inclusive = false,
                message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @PositiveOrZero(message = "Stock quantity must be greater than or equal to 0")
        Integer stockQuantity,

        @URL(message = "Image URL must be a valid URL")
        @Size(max = 2048, message = "Image URL must not exceed 2048 characters")
        String imageUrl,

        @NotNull(message = "Category ID is required")
        @Positive(message = "Category ID must be greater than 0")
        Long categoryId,

        @NotNull(message = "Product status is required")
        ProductStatus status

) {
}
