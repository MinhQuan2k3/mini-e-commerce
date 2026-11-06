package com.miniecommerce.backend.category.repository;

import com.miniecommerce.backend.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
}
