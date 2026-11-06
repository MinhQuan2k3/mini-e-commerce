package com.miniecommerce.backend.order.repository;

import com.miniecommerce.backend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
