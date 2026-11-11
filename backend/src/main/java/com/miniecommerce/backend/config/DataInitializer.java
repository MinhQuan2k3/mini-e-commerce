package com.miniecommerce.backend.config;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import com.miniecommerce.backend.category.repository.CategoryRepository;
import com.miniecommerce.backend.product.entity.Product;
import com.miniecommerce.backend.product.entity.ProductStatus;
import com.miniecommerce.backend.product.repository.ProductRepository;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    public DataInitializer(
            CategoryRepository categoryRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Starting initial data initialization...");

        seedCategories();
        seedProducts();
        seedAdmin();

        log.info("Initial data initialization completed.");
    }

    private void seedCategories() {
        getOrCreateCategory(
                "Computers",
                "Laptops, desktop computers and computer hardware",
                CategoryStatus.ACTIVE
        );

        getOrCreateCategory(
                "Smartphones",
                "Smartphones and mobile devices",
                CategoryStatus.ACTIVE
        );

        getOrCreateCategory(
                "Accessories",
                "Computer and mobile accessories",
                CategoryStatus.ACTIVE
        );

        getOrCreateCategory(
                "Office Equipment",
                "Office equipment and supplies",
                CategoryStatus.INACTIVE
        );

        log.info("Category seed data initialized.");
    }

    private Category getOrCreateCategory(
            String name,
            String description,
            CategoryStatus status
    ) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> {
                    Category category = new Category(
                            name,
                            description,
                            status
                    );

                    log.info("Creating category: {}", name);
                    return categoryRepository.save(category);
                });
    }

    private void seedProducts() {
        Category computers = getOrCreateCategory(
                "Computers",
                "Laptops, desktop computers and computer hardware",
                CategoryStatus.ACTIVE
        );

        Category smartphones = getOrCreateCategory(
                "Smartphones",
                "Smartphones and mobile devices",
                CategoryStatus.ACTIVE
        );

        Category accessories = getOrCreateCategory(
                "Accessories",
                "Computer and mobile accessories",
                CategoryStatus.ACTIVE
        );

        createProductIfNotExists(
                "SEED-LAPTOP-A",
                "Laptop A",
                "Laptop for everyday work and study",
                "25000000.00",
                10,
                "https://example.com/images/laptop-a.jpg",
                computers,
                ProductStatus.ACTIVE
        );

        createProductIfNotExists(
                "SEED-PHONE-B",
                "Phone B",
                "Smartphone for daily communication and entertainment",
                "12000000.00",
                0,
                "https://example.com/images/phone-b.jpg",
                smartphones,
                ProductStatus.ACTIVE
        );

        createProductIfNotExists(
                "SEED-MOUSE-C",
                "Mouse C",
                "Wireless computer mouse",
                "250000.00",
                50,
                "https://example.com/images/mouse-c.jpg",
                accessories,
                ProductStatus.ACTIVE
        );

        createProductIfNotExists(
                "SEED-KEYBOARD-D",
                "Keyboard D",
                "Mechanical computer keyboard",
                "1200000.00",
                15,
                "https://example.com/images/keyboard-d.jpg",
                accessories,
                ProductStatus.INACTIVE
        );

        log.info("Product seed data initialized.");
    }

    private void createProductIfNotExists(
            String sku,
            String name,
            String description,
            String price,
            Integer stockQuantity,
            String imageUrl,
            Category category,
            ProductStatus status
    ) {
        if (productRepository.existsBySku(sku)) {
            log.info("Product already exists, skipping: {}", sku);
            return;
        }

        Product product = new Product(
                sku,
                name,
                description,
                new BigDecimal(price),
                stockQuantity,
                imageUrl,
                category,
                status
        );

        productRepository.save(product);
        log.info("Creating product: {}", sku);
    }

    private void seedAdmin() {
        if (adminEmail == null || adminEmail.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {
            log.warn(
                    "ADMIN_EMAIL or ADMIN_PASSWORD is missing. "
                            + "Skipping initial Admin account creation."
            );
            return;
        }

        String email = adminEmail.trim();

        if (userRepository.existsByEmail(email)) {
            log.info("Admin account already exists. Skipping creation.");
            return;
        }

        BCryptPasswordEncoder passwordEncoder =
                new BCryptPasswordEncoder();

        String passwordHash = passwordEncoder.encode(adminPassword);

        User admin = new User(
                email,
                passwordHash,
                UserRole.ADMIN
        );

        userRepository.save(admin);

        log.info("Initial Admin account created with email: {}", email);
    }
}
