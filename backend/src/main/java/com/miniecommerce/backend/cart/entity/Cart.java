package com.miniecommerce.backend.cart.entity;

import com.miniecommerce.backend.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "customer_id",
            nullable = false,
            unique = true
    )
    private User customer;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected Cart() {
    }

    public Cart(User customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

/*
- Quan hệ Cart – User là One-to-One, vì mỗi khách hàng chỉ có tối đa một giỏ hàng.
- unique = true trên customer_id tương ứng với constraint uk_carts_customer_id.
- FetchType.LAZY tránh tải thông tin User khi không cần thiết.
- Không khai báo CartItem collection ở đây. Quan hệ ngược có thể bổ sung khi có nhu cầu nghiệp vụ cụ thể.
*/
