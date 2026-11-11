package com.miniecommerce.backend.cart.repository;

import com.miniecommerce.backend.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
