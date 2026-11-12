package com.miniecommerce.backend.category.controller;

import com.miniecommerce.backend.category.dto.CategoryResponse;
import com.miniecommerce.backend.category.service.CategoryService;
import com.miniecommerce.backend.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getActiveCategories() {
        List<CategoryResponse> categories =
                categoryService.getActiveCategories();

        return ResponseEntity.ok(
                ApiResponse.success("Categories retrieved successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable Long id
    ) {
        CategoryResponse category = categoryService.getActiveCategoryById(id);

        return ResponseEntity.ok(
                ApiResponse.success("Category retrieved successfully", category)
        );
    }
}
