package com.miniecommerce.backend.product.controller;

import com.miniecommerce.backend.common.response.ApiResponse;
import com.miniecommerce.backend.common.response.PageResponse;
import com.miniecommerce.backend.product.dto.AdminProductDetailResponse;
import com.miniecommerce.backend.product.dto.AdminProductRequest;
import com.miniecommerce.backend.product.dto.AdminProductResponse;
import com.miniecommerce.backend.product.service.AdminProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    public AdminProductController(AdminProductService adminProductService) {
        this.adminProductService = adminProductService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<AdminProductResponse>>>
    getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String direction
    ) {
        PageResponse<AdminProductResponse> products =
                adminProductService.getProducts(
                        keyword,
                        categoryId,
                        status,
                        page,
                        size,
                        sort,
                        direction
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Products retrieved successfully",
                        products
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminProductDetailResponse>>
    getProductById(@PathVariable Long id) {
        AdminProductDetailResponse product =
                adminProductService.getProductById(id);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product retrieved successfully",
                        product
                )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AdminProductResponse>> createProduct(
            @Valid @RequestBody AdminProductRequest request
    ) {
        AdminProductResponse product =
                adminProductService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success(
                        "Product created successfully",
                        product
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AdminProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody AdminProductRequest request
    ) {
        AdminProductResponse product =
                adminProductService.updateProduct(id, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Product updated successfully",
                        product
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        adminProductService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }
}
