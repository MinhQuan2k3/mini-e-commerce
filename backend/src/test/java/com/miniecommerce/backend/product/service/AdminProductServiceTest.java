package com.miniecommerce.backend.product.service;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import com.miniecommerce.backend.category.repository.CategoryRepository;
import com.miniecommerce.backend.common.exception.BadRequestException;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.ResourceNotFoundException;
import com.miniecommerce.backend.product.dto.AdminProductRequest;
import com.miniecommerce.backend.product.entity.Product;
import com.miniecommerce.backend.product.entity.ProductStatus;
import com.miniecommerce.backend.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private AdminProductService adminProductService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);

        adminProductService = new AdminProductService(
                productRepository,
                categoryRepository
        );
    }

    private AdminProductRequest createRequest(
            Long categoryId,
            ProductStatus status
    ) {
        return new AdminProductRequest(
                "SKU-001",
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("500000.00"),
                20,
                "https://example.com/keyboard.jpg",
                categoryId,
                status
        );
    }

    private Category createActiveCategory(Long id) {
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(id);
        when(category.getStatus()).thenReturn(CategoryStatus.ACTIVE);
        when(category.getName()).thenReturn("Electronics");
        return category;
    }

    private Category createInactiveCategory(Long id) {
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(id);
        when(category.getStatus()).thenReturn(CategoryStatus.INACTIVE);
        when(category.getName()).thenReturn("Old Electronics");
        return category;
    }

    private Product createProduct(Category category) {
        return new Product(
                "SKU-001",
                "Keyboard",
                "Mechanical keyboard",
                new BigDecimal("500000.00"),
                20,
                "https://example.com/keyboard.jpg",
                category,
                ProductStatus.ACTIVE
        );
    }

    @Test
    void shouldThrowConflictExceptionWhenCreatingProductWithDuplicateSku() {
        AdminProductRequest request = createRequest(
                1L,
                ProductStatus.ACTIVE
        );

        when(productRepository.existsBySku("SKU-001"))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> adminProductService.createProduct(request)
        );

        verify(productRepository).existsBySku("SKU-001");
        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenCreatingProductWithMissingCategory() {
        AdminProductRequest request = createRequest(
                99L,
                ProductStatus.ACTIVE
        );

        when(productRepository.existsBySku("SKU-001"))
                .thenReturn(false);

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> adminProductService.createProduct(request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreatingProductWithInactiveCategory() {
        AdminProductRequest request = createRequest(
                1L,
                ProductStatus.ACTIVE
        );

        Category category = createInactiveCategory(1L);

        when(productRepository.existsBySku("SKU-001"))
                .thenReturn(false);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        assertThrows(
                BadRequestException.class,
                () -> adminProductService.createProduct(request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowConflictExceptionWhenUpdatingProductWithDuplicateSku() {
        Category category = createActiveCategory(1L);
        Product product = createProduct(category);

        AdminProductRequest request = createRequest(
                1L,
                ProductStatus.ACTIVE
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.existsBySkuAndIdNot("SKU-001", 1L))
                .thenReturn(true);

        assertThrows(
                ConflictException.class,
                () -> adminProductService.updateProduct(1L, request)
        );

        verify(productRepository, never()).save(any(Product.class));
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenChangingToInactiveCategory() {
        Category currentCategory = createActiveCategory(1L);
        Category inactiveCategory = createInactiveCategory(2L);
        Product product = createProduct(currentCategory);

        AdminProductRequest request = createRequest(
                2L,
                ProductStatus.ACTIVE
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.existsBySkuAndIdNot("SKU-001", 1L))
                .thenReturn(false);

        when(categoryRepository.findById(2L))
                .thenReturn(Optional.of(inactiveCategory));

        assertThrows(
                BadRequestException.class,
                () -> adminProductService.updateProduct(1L, request)
        );

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldAllowUpdatingProductWhileKeepingItsInactiveCategory() {
        Category inactiveCategory = createInactiveCategory(1L);
        Product product = createProduct(inactiveCategory);

        AdminProductRequest request = createRequest(
                1L,
                ProductStatus.ACTIVE
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.existsBySkuAndIdNot("SKU-001", 1L))
                .thenReturn(false);

        when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(inactiveCategory));

        when(productRepository.saveAndFlush(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        adminProductService.updateProduct(1L, request);

        assertEquals(inactiveCategory, product.getCategory());

        verify(productRepository).saveAndFlush(product);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldSoftDeleteActiveProduct() {
        Category category = createActiveCategory(1L);
        Product product = createProduct(category);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        adminProductService.deleteProduct(1L);

        assertEquals(ProductStatus.INACTIVE, product.getStatus());

        verify(productRepository).findById(1L);
        verify(productRepository).save(product);
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldNotSaveProductWhenDeletingAlreadyInactiveProduct() {
        Category category = createActiveCategory(1L);
        Product product = createProduct(category);
        product.setStatus(ProductStatus.INACTIVE);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        adminProductService.deleteProduct(1L);

        assertEquals(ProductStatus.INACTIVE, product.getStatus());

        verify(productRepository).findById(1L);
        verify(productRepository, never()).save(any(Product.class));
        verify(productRepository, never()).delete(any(Product.class));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenGettingMissingProduct() {
        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> adminProductService.getProductById(99L)
        );

        verify(productRepository).findById(99L);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenPageSizeExceedsMaximum() {
        assertThrows(
                BadRequestException.class,
                () -> adminProductService.getProducts(
                        null, null, null, 0, 51, null, null
                )
        );

        verifyNoInteractions(productRepository, categoryRepository);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenSortFieldIsInvalid() {
        assertThrows(
                BadRequestException.class,
                () -> adminProductService.getProducts(
                        null, null, null, 0, 10, "invalidField", "ASC"
                )
        );

        verifyNoInteractions(productRepository, categoryRepository);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenProductStatusIsInvalid() {
        assertThrows(
                BadRequestException.class,
                () -> adminProductService.getProducts(
                        null, null, "DELETED", 0, 10, null, null
                )
        );

        verifyNoInteractions(productRepository, categoryRepository);
    }

    @Test
    void shouldReturnPaginatedProductsWithDefaultSorting() {
        Page<Product> productPage = new PageImpl<>(
                List.of(),
                Pageable.ofSize(10),
                0
        );

        when(productRepository.findAll(
                any(Specification.class),
                any(Pageable.class)
        )).thenReturn(productPage);

        var result = adminProductService.getProducts(
                null, null, null, null, null, null, null
        );

        assertEquals(0, result.page());
        assertEquals(10, result.size());
        assertEquals(0, result.totalElements());
        assertEquals(0, result.totalPages());

        verify(productRepository).findAll(
                any(Specification.class),
                any(Pageable.class)
        );
    }
}
