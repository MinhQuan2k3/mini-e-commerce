package com.miniecommerce.backend.product.dto;

import com.miniecommerce.backend.product.entity.Product;
import com.miniecommerce.backend.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminProductDetailResponse(

        Long id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        CategoryInfo category,
        ProductStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {

    public record CategoryInfo(
            Long id,
            String name
    ) {
    }

    public static AdminProductDetailResponse fromEntity(Product product) {
        return new AdminProductDetailResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                new CategoryInfo(
                        product.getCategory().getId(),
                        product.getCategory().getName()
                ),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
