package com.miniecommerce.backend.category.service;

import com.miniecommerce.backend.category.dto.CategoryRequest;
import com.miniecommerce.backend.category.dto.CategoryResponse;
import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import com.miniecommerce.backend.category.repository.CategoryRepository;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.ResourceNotFoundException;
import com.miniecommerce.backend.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private static final Logger log =
            LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CategoryService(
            CategoryRepository categoryRepository,
            ProductRepository productRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getActiveCategories() {
        return categoryRepository.findAllByStatus(CategoryStatus.ACTIVE)
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryById(id);

        return CategoryResponse.fromEntity(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        String name = request.name().trim();

        if (categoryRepository.existsByName(name)) {
            throw new ConflictException("Category name already exists");
        }

        Category category = new Category(
                name,
                request.description(),
                request.status()
        );

        Category savedCategory = categoryRepository.save(category);
        log.info("Category created: id={}", savedCategory.getId());

        return CategoryResponse.fromEntity(savedCategory);
    }

    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategoryById(id);
        String name = request.name().trim();

        if (categoryRepository.existsByNameAndIdNot(name, id)) {
            throw new ConflictException("Category name already exists");
        }

        category.setName(name);
        category.setDescription(request.description());
        category.setStatus(request.status());

        Category updatedCategory = categoryRepository.save(category);
        log.info("Category updated: id={}", updatedCategory.getId());

        return CategoryResponse.fromEntity(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category category = findCategoryById(id);

        if (productRepository.existsByCategoryId(id)) {
            throw new ConflictException(
                    "Category is being used by one or more products and cannot be deleted"
            );
        }

        categoryRepository.delete(category);
        log.info("Category deleted: id={}", id);
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id
                ));
    }
}
