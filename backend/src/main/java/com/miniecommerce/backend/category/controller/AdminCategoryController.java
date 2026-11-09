package com.miniecommerce.backend.category.controller;

import com.miniecommerce.backend.category.dto.CategoryRequest;
import com.miniecommerce.backend.category.dto.CategoryResponse;
import com.miniecommerce.backend.category.service.CategoryService;
import com.miniecommerce.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories =
                categoryService.getAllCategories();

        return ResponseEntity.ok(
                ApiResponse.success("Categories retrieved successfully", categories)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse category = categoryService.createCategory(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.success("Category created successfully", category)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse category =
                categoryService.updateCategory(id, request);

        return ResponseEntity.ok(
                ApiResponse.success("Category updated successfully", category)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        return ResponseEntity.noContent().build();
    }
}
