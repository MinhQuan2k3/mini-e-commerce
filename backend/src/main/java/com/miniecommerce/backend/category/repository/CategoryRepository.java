package com.miniecommerce.backend.category.repository;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, Long id);
    List<Category> findAllByStatus(CategoryStatus status);
/*
 Phương thức findAllByStatus() là derived query method của Spring Data JPA,
 tương ứng với truy vấn các Category theo trạng thái.
*/
}
