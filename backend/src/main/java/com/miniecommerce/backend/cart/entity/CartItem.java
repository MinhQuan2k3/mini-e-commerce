package com.miniecommerce.backend.cart.entity;

import com.miniecommerce.backend.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "cart_items",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_cart_items_cart_product",
                        columnNames = {"cart_id", "product_id"}
                )
        }
)
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "cart_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_items_cart")
    )
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_cart_items_product")
    )
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    protected CartItem() {
    }

    public CartItem(Cart cart, Product product, Integer quantity) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}

/*
- CartItem – Cart: Quan hệ Many-to-One.
- CartItem – Product: Quan hệ Many-to-One.
- Một sản phẩm chỉ xuất hiện một lần trong cùng một giỏ hàng nhờ unique constraint.
- quantity sử dụng Integer, tương ứng với SQL INT.
*/
