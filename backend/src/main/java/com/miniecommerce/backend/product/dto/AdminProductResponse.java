package com.miniecommerce.backend.product.dto;

import com.miniecommerce.backend.product.entity.Product;
import com.miniecommerce.backend.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminProductResponse(

        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        Long categoryId,
        ProductStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {

    public static AdminProductResponse fromEntity(Product product) {
        return new AdminProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getId(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
