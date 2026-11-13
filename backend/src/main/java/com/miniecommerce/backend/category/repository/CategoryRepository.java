package com.miniecommerce.backend.category.repository;

import com.miniecommerce.backend.category.entity.Category;
import com.miniecommerce.backend.category.entity.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    Optional<Category> findByName(String name);

    List<Category> findAllByStatus(CategoryStatus status);

    Optional<Category> findByIdAndStatus(Long id, CategoryStatus status);
/*
 - Phương thức findAllByStatus() là derived query method của Spring Data JPA,
 tương ứng với truy vấn các Category theo trạng thái.
 - Phương thức Optional<Category> findByName(String name) cho phép lấy Category
 đã tồn tại mà không phải tạo bản ghi trùng.
*/
}
