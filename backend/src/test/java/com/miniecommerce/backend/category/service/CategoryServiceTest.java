package com.miniecommerce.backend.category.service;

import com.miniecommerce.backend.category.dto.CategoryRequest;
import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import com.miniecommerce.backend.category.repository.CategoryRepository;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.product.repository.ProductRepository;
import com.miniecommerce.backend.category.dto.CategoryResponse;
import com.miniecommerce.backend.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);

        categoryService = new CategoryService(
                categoryRepository,
                productRepository
        );
    }

    @Test
    void shouldThrowConflictExceptionWhenCreatingCategoryWithDuplicateName() {
        CategoryRequest request = new CategoryRequest(
                "Electronics",
                "Electronic products",
                CategoryStatus.ACTIVE
        );

        when(categoryRepository.existsByName("Electronics"))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> categoryService.createCategory(request)
        );

        verify(categoryRepository).existsByName("Electronics");
        verify(categoryRepository, never()).save(any(Category.class));
        verifyNoInteractions(productRepository);
    }

    @Test
    void shouldThrowConflictExceptionWhenDeletingCategoryUsedByProducts() {
        Category category = new Category(
                "Electronics",
                "Electronic products",
                CategoryStatus.ACTIVE
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.existsByCategoryId(1L))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> categoryService.deleteCategory(1L)
        );

        verify(categoryRepository).findById(1L);
        verify(productRepository).existsByCategoryId(1L);
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @Test
    void shouldDeleteCategoryWhenNoProductsAreUsingIt() {
        Category category = new Category(
                "Electronics",
                "Electronic products",
                CategoryStatus.ACTIVE
        );

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        when(productRepository.existsByCategoryId(1L))
                .thenReturn(false);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).findById(1L);
        verify(productRepository).existsByCategoryId(1L);
        verify(categoryRepository).delete(category);
    }

    @Test
    void shouldReturnActiveCategoryWhenGettingCategoryById() {
        Category category = new Category(
                "Electronics",
                "Electronic products",
                CategoryStatus.ACTIVE
        );

        when(categoryRepository.findByIdAndStatus(
                1L,
                CategoryStatus.ACTIVE
        )).thenReturn(Optional.of(category));

        assertDoesNotThrow(
                () -> categoryService.getActiveCategoryById(1L)
        );

        verify(categoryRepository).findByIdAndStatus(
                1L,
                CategoryStatus.ACTIVE
        );
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGettingInactiveCategoryById() {
        when(categoryRepository.findByIdAndStatus(
                1L,
                CategoryStatus.ACTIVE
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getActiveCategoryById(1L)
        );

        verify(categoryRepository).findByIdAndStatus(
                1L,
                CategoryStatus.ACTIVE
        );
    }
}
