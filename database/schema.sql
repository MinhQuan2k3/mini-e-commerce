
-- =====================================================
-- Mini E-commerce / Inventory Management
-- Database Schema
-- =====================================================

CREATE DATABASE IF NOT EXISTS mini_ecommerce
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE mini_ecommerce;

-- =====================================================
-- 1. USERS
-- =====================================================

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL,

                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT uk_users_email UNIQUE (email),

                       CONSTRAINT chk_users_role
                           CHECK (role IN ('CUSTOMER', 'ADMIN'))
) ENGINE=InnoDB;

-- =====================================================
-- 2. CATEGORIES
-- =====================================================

CREATE TABLE categories (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            description TEXT,
                            status VARCHAR(20) NOT NULL,

                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,

                            CONSTRAINT uk_categories_name UNIQUE (name),

                            CONSTRAINT chk_categories_status
                                CHECK (status IN ('ACTIVE', 'INACTIVE'))
) ENGINE=InnoDB;

-- =====================================================
-- 3. PRODUCTS
-- =====================================================

CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sku VARCHAR(100) NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          price DECIMAL(15, 2) NOT NULL,
                          stock_quantity INT NOT NULL,
                          image_url VARCHAR(2048),
                          category_id BIGINT NOT NULL,
                          status VARCHAR(20) NOT NULL,

                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                              ON UPDATE CURRENT_TIMESTAMP,

                          CONSTRAINT uk_products_sku UNIQUE (sku),

                          CONSTRAINT chk_products_price
                              CHECK (price > 0),

                          CONSTRAINT chk_products_stock_quantity
                              CHECK (stock_quantity >= 0),

                          CONSTRAINT chk_products_status
                              CHECK (status IN ('ACTIVE', 'INACTIVE')),

                          CONSTRAINT fk_products_category
                              FOREIGN KEY (category_id)
                                  REFERENCES categories(id)
                                  ON UPDATE CASCADE
                                  ON DELETE RESTRICT,

                          INDEX idx_products_category_id (category_id),
                          INDEX idx_products_status (status),
                          INDEX idx_products_created_at (created_at),
                          INDEX idx_products_price (price)
) ENGINE=InnoDB;

-- =====================================================
-- 4. CARTS
-- =====================================================

CREATE TABLE carts (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       customer_id BIGINT NOT NULL,

                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                           ON UPDATE CURRENT_TIMESTAMP,

                       CONSTRAINT uk_carts_customer_id UNIQUE (customer_id),

                       CONSTRAINT fk_carts_customer
                           FOREIGN KEY (customer_id)
                               REFERENCES users(id)
                               ON UPDATE CASCADE
                               ON DELETE RESTRICT
) ENGINE=InnoDB;

-- =====================================================
-- 5. CART_ITEMS
-- =====================================================

CREATE TABLE cart_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            cart_id BIGINT NOT NULL,
                            product_id BIGINT NOT NULL,
                            quantity INT NOT NULL,

                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                                ON UPDATE CURRENT_TIMESTAMP,

                            CONSTRAINT uk_cart_items_cart_product
                                UNIQUE (cart_id, product_id),

                            CONSTRAINT chk_cart_items_quantity
                                CHECK (quantity > 0),

                            CONSTRAINT fk_cart_items_cart
                                FOREIGN KEY (cart_id)
                                    REFERENCES carts(id)
                                    ON UPDATE CASCADE
                                    ON DELETE RESTRICT,

                            CONSTRAINT fk_cart_items_product
                                FOREIGN KEY (product_id)
                                    REFERENCES products(id)
                                    ON UPDATE CASCADE
                                    ON DELETE RESTRICT,

                            INDEX idx_cart_items_product_id (product_id)
) ENGINE=InnoDB;

-- =====================================================
-- 6. ORDERS
-- =====================================================

CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        customer_id BIGINT NOT NULL,
                        total_amount DECIMAL(15, 2) NOT NULL,

                        recipient_name VARCHAR(150) NOT NULL,
                        phone VARCHAR(30) NOT NULL,
                        address VARCHAR(500) NOT NULL,
                        province_city VARCHAR(100) NOT NULL,
                        district VARCHAR(100) NOT NULL,
                        ward VARCHAR(100) NOT NULL,

                        order_status VARCHAR(20) NOT NULL,
                        payment_status VARCHAR(20) NOT NULL,
                        paid_at DATETIME,

                        created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
                            ON UPDATE CURRENT_TIMESTAMP,

                        CONSTRAINT chk_orders_total_amount
                            CHECK (total_amount >= 0),

                        CONSTRAINT chk_orders_order_status
                            CHECK (
                                order_status IN (
                                                 'PENDING',
                                                 'CONFIRMED',
                                                 'SHIPPING',
                                                 'DELIVERED',
                                                 'CANCELLED'
                                    )
                                ),

                        CONSTRAINT chk_orders_payment_status
                            CHECK (payment_status IN ('UNPAID', 'PAID')),

                        CONSTRAINT fk_orders_customer
                            FOREIGN KEY (customer_id)
                                REFERENCES users(id)
                                ON UPDATE CASCADE
                                ON DELETE RESTRICT,

                        INDEX idx_orders_customer_id (customer_id),
                        INDEX idx_orders_order_status (order_status),
                        INDEX idx_orders_created_at (created_at)
) ENGINE=InnoDB;

-- =====================================================
-- 7. ORDER_ITEMS
-- =====================================================

CREATE TABLE order_items (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id BIGINT NOT NULL,
                             product_id BIGINT NOT NULL,

                             product_name_snapshot VARCHAR(255) NOT NULL,
                             sku_snapshot VARCHAR(100) NOT NULL,
                             unit_price DECIMAL(15, 2) NOT NULL,
                             quantity INT NOT NULL,
                             item_total DECIMAL(15, 2) NOT NULL,

                             CONSTRAINT chk_order_items_unit_price
                                 CHECK (unit_price > 0),

                             CONSTRAINT chk_order_items_quantity
                                 CHECK (quantity > 0),

                             CONSTRAINT chk_order_items_item_total
                                 CHECK (item_total >= 0),

                             CONSTRAINT fk_order_items_order
                                 FOREIGN KEY (order_id)
                                     REFERENCES orders(id)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT,

                             CONSTRAINT fk_order_items_product
                                 FOREIGN KEY (product_id)
                                     REFERENCES products(id)
                                     ON UPDATE CASCADE
                                     ON DELETE RESTRICT,

                             INDEX idx_order_items_product_id (product_id)
) ENGINE=InnoDB;

-- =====================================================
-- END OF DATABASE SCHEMA
-- =====================================================
