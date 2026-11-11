package com.miniecommerce.backend.product.service;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import com.miniecommerce.backend.category.repository.CategoryRepository;
import com.miniecommerce.backend.common.exception.BadRequestException;
import com.miniecommerce.backend.common.exception.ConflictException;
import com.miniecommerce.backend.common.exception.ResourceNotFoundException;
import com.miniecommerce.backend.common.response.PageResponse;
import com.miniecommerce.backend.product.dto.AdminProductDetailResponse;
import com.miniecommerce.backend.product.dto.AdminProductRequest;
import com.miniecommerce.backend.product.dto.AdminProductResponse;
import com.miniecommerce.backend.product.entity.Product;
import com.miniecommerce.backend.product.entity.ProductStatus;
import com.miniecommerce.backend.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
@Transactional
public class AdminProductService {

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 50;
    private static final String DEFAULT_SORT = "createdAt";
    private static final String DEFAULT_DIRECTION = "DESC";

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "sku",
            "name",
            "price",
            "stockQuantity",
            "createdAt",
            "updatedAt"
    );

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public AdminProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public PageResponse<AdminProductResponse> getProducts(
            String keyword,
            Long categoryId,
            String status,
            Integer page,
            Integer size,
            String sort,
            String direction
    ) {
        int pageNumber = page == null ? DEFAULT_PAGE : page;
        int pageSize = size == null ? DEFAULT_SIZE : size;

        if (pageNumber < 0) {
            throw new BadRequestException("Page must be greater than or equal to 0");
        }

        if (pageSize <= 0 || pageSize > MAX_SIZE) {
            throw new BadRequestException("Size must be between 1 and 50");
        }

        if (categoryId != null && categoryId <= 0) {
            throw new BadRequestException("Category ID must be greater than 0");
        }

        String sortField = sort == null ? DEFAULT_SORT : sort.trim();

        if (!ALLOWED_SORT_FIELDS.contains(sortField)) {
            throw new BadRequestException("Invalid sort field");
        }

        String sortDirection = direction == null
                ? DEFAULT_DIRECTION
                : direction.trim().toUpperCase(Locale.ROOT);

        Sort.Direction sortOrder;

        try {
            sortOrder = Sort.Direction.valueOf(sortDirection);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException("Direction must be ASC or DESC");
        }

        ProductStatus productStatus = parseStatus(status);

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                Sort.by(sortOrder, sortField)
        );

        Specification<Product> specification = buildSpecification(
                keyword,
                categoryId,
                productStatus
        );

        Page<Product> productPage = productRepository.findAll(
                specification,
                pageable
        );

        return PageResponse.from(
                productPage.map(AdminProductResponse::fromEntity)
        );
    }

    @Transactional(readOnly = true)
    public AdminProductDetailResponse getProductById(Long id) {
        Product product = findProductById(id);
        return AdminProductDetailResponse.fromEntity(product);
    }

    public AdminProductResponse createProduct(AdminProductRequest request) {
        String sku = request.sku().trim();

        if (productRepository.existsBySku(sku)) {
            throw new ConflictException("Product SKU already exists");
        }

        Category category = findCategoryById(request.categoryId());
        validateCategoryForNewProduct(category);

        Product product = new Product(
                sku,
                request.name().trim(),
                request.description(),
                request.price(),
                request.stockQuantity(),
                request.imageUrl(),
                category,
                request.status()
        );

        Product savedProduct = productRepository.save(product);

        return AdminProductResponse.fromEntity(savedProduct);
    }

    public AdminProductResponse updateProduct(
            Long id,
            AdminProductRequest request
    ) {
        Product product = findProductById(id);

        String sku = request.sku().trim();

        if (productRepository.existsBySkuAndIdNot(sku, id)) {
            throw new ConflictException("Product SKU already exists");
        }

        Category category = findCategoryById(request.categoryId());

        boolean changingCategory = !product.getCategory()
                .getId()
                .equals(category.getId());

        if (changingCategory) {
            validateCategoryForNewProduct(category);
        }

        product.setSku(sku);
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        product.setCategory(category);
        product.setStatus(request.status());

        Product updatedProduct = productRepository.saveAndFlush(product);

        return AdminProductResponse.fromEntity(updatedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = findProductById(id);

        if (product.getStatus() == ProductStatus.INACTIVE) {
            return;
        }

        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    private Specification<Product> buildSpecification(
            String keyword,
            Long categoryId,
            ProductStatus status
    ) {
        return (root, query, criteriaBuilder) -> {
            var predicates = criteriaBuilder.conjunction();

            if (keyword != null && !keyword.isBlank()) {
                String searchKeyword = "%"
                        + keyword.trim().toLowerCase(Locale.ROOT)
                        + "%";

                var keywordPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("name")),
                                searchKeyword
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("sku")),
                                searchKeyword
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("description")),
                                searchKeyword
                        )
                );

                predicates = criteriaBuilder.and(
                        predicates,
                        keywordPredicate
                );
            }

            if (categoryId != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                categoryId
                        )
                );
            }

            if (status != null) {
                predicates = criteriaBuilder.and(
                        predicates,
                        criteriaBuilder.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            return predicates;
        };
    }

    private ProductStatus parseStatus(String status) {
        if (status == null || status.isBlank()) {
            return null;
        }

        try {
            return ProductStatus.valueOf(
                    status.trim().toUpperCase(Locale.ROOT)
            );
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(
                    "Status must be ACTIVE or INACTIVE"
            );
        }
    }

    private void validateCategoryForNewProduct(Category category) {
        if (category.getStatus() == CategoryStatus.INACTIVE) {
            throw new BadRequestException("Category is inactive");
        }
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with id: " + id
                ));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + id
                ));
    }
}
