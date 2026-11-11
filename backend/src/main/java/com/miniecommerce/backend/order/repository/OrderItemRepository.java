package com.miniecommerce.backend.order.repository;

import com.miniecommerce.backend.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
