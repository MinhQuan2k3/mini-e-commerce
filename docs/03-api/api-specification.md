# API Specification

## Information

| Item | Value |
|---|---|
| Project | Mini E-commerce |
| Document | API Specification |
| Status | Revised after Review Round 2 |

---

## 1. Mục đích

Mô tả các REST API chính của **Hệ thống Quản lý Bán hàng Mini (Mini E-commerce)**.

Bao gồm:

- Danh sách endpoint.
- HTTP method.
- Quyền truy cập.
- Mô tả request.
- Mô tả response.
- Quy tắc validation.
- HTTP status code.
- Các business rule liên quan.

API Specification được xây dựng dựa trên:

- Requirement Specification.
- Use Case Specification.
- Entity Relationship Diagram.
- Class Diagram.

---

## 2. API Overview

### 2.1. Base URL

Trong môi trường local:

```text
http://localhost:8080/api
```

Các endpoint bên dưới được mô tả tương đối so với Base URL.

Ví dụ:

```text
GET /products
```

tương ứng với:

```text
http://localhost:8080/api/products
```

---

## 3. Authentication and Authorization

### 3.1. Authentication Method

Hệ thống sử dụng:

```text
Email + Password
JWT Bearer Token
```

Sau khi đăng nhập thành công, client sử dụng JWT trong HTTP Header:

```http
Authorization: Bearer <access_token>
```

### 3.2. Roles

| Role | Description |
|---|---|
| `GUEST` | Người dùng chưa đăng nhập |
| `CUSTOMER` | Khách hàng đã đăng nhập |
| `ADMIN` | Quản trị viên |

`GUEST` là trạng thái truy cập của người dùng, không phải giá trị role được lưu trong bảng `users`.

Database chỉ lưu:

```text
CUSTOMER
ADMIN
```

### 3.3. Authorization Rules

| Resource | Guest | Customer | Admin |
|---|---:|---:|---:|
| Xem sản phẩm | Có | Có | Có |
| Tìm kiếm sản phẩm | Có | Có | Có |
| Xem danh mục | Có | Có | Có |
| Quản lý Cart | Không | Có | Không áp dụng |
| Checkout | Không | Có | Không áp dụng |
| Xem đơn hàng của bản thân | Không | Có | Không áp dụng |
| Quản lý sản phẩm | Không | Không | Có |
| Quản lý danh mục | Không | Không | Có |
| Quản lý tất cả đơn hàng | Không | Không | Có |

---

# 4. Common API Conventions

## 4.1. Content Type

Request và response sử dụng JSON:

```http
Content-Type: application/json
```

## 4.2. Date-Time Format

Date-time sử dụng ISO 8601:

```text
2026-09-24T14:30:00
```

## 4.3. Money Format

Các giá trị tiền tệ được biểu diễn bằng số:

```json
{
  "price": 150000.00
}
```

Backend nên sử dụng kiểu dữ liệu chính xác cho tiền tệ, chẳng hạn:

```text
BigDecimal
```

Không sử dụng `float` hoặc `double` cho phép tính tiền quan trọng.

## 4.4. Pagination

Các API danh sách có thể sử dụng query parameters:

| Parameter | Type | Default | Description |
|---|---|---:|---|
| `page` | Integer | `0` | Số trang, bắt đầu từ 0 |
| `size` | Integer | `10` | Số phần tử mỗi trang, tối đa `50` |
| `sort` | String | Tùy API | Trường dùng để sắp xếp |
| `direction` | String | `ASC` | `ASC` hoặc `DESC` |

Quy tắc:

- `page` phải lớn hơn hoặc bằng `0`.
- `size` phải lớn hơn `0`.
- `size` không được vượt quá `50`.
- Nếu `size > 50`, API trả về `400 Bad Request`.

Ví dụ:

```http
GET /products?page=0&size=10&sort=name&direction=ASC
```

## 4.5. Common API Response

### Success Response

Các API trả về response thành công theo format thống nhất:

```json
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

`data` chứa resource hoặc dữ liệu tương ứng với từng API.

### Error Response

Các API trả về lỗi theo format:

```json
{
  "success": false,
  "message": "Invalid request data",
  "errors": []
}
```

Khi lỗi liên quan đến một hoặc nhiều field cụ thể:

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "price",
      "message": "must be greater than 0"
    }
  ]
}
```

Các API phải sử dụng thống nhất response format này.

---

# 5. API Summary

| Module | Method | Endpoint | Access | Related FR/UC |
|---|---|---|---|---|
| Authentication | `POST` | `/auth/register` | Guest | FR-AUTH-01 / UC-AUTH-01 |
| Authentication | `POST` | `/auth/login` | Guest | FR-AUTH-02 / UC-AUTH-02 |
| Products | `GET` | `/products` | Public | FR-PROD-01 / UC-PROD-01 |
| Products | `GET` | `/products/{id}` | Public | FR-PROD-02 / UC-PROD-02 |
| Categories | `GET` | `/categories` | Public | FR-CAT-01 |
| Categories | `GET` | `/categories/{id}` | Public | FR-CAT-02 |
| Cart | `GET` | `/cart` | Customer | FR-CART-01 / UC-CART-01 |
| Cart | `POST` | `/cart/items` | Customer | FR-CART-02 / UC-CART-02 |
| Cart | `PUT` | `/cart/items/{productId}` | Customer | FR-CART-03 |
| Cart | `DELETE` | `/cart/items/{productId}` | Customer | FR-CART-04 |
| Checkout | `POST` | `/orders/checkout` | Customer | FR-ORDER-01 / UC-ORDER-01 |
| Customer Orders | `GET` | `/orders/my-orders` | Customer | FR-ORDER-11, FR-ORDER-13 / UC-ORDER-02 |
| Customer Orders | `GET` | `/orders/{id}` | Customer | FR-ORDER-03 |
| Customer Orders | `PATCH` | `/orders/{id}/cancel` | Customer | FR-ORDER-04 |
| Admin Products | `GET` | `/admin/products` | Admin | FR-ADMIN-PROD-01 |
| Admin Products | `GET` | `/admin/products/{id}` | Admin | FR-ADMIN-PROD-02 |
| Admin Products | `POST` | `/admin/products` | Admin | FR-ADMIN-PROD-03 |
| Admin Products | `PUT` | `/admin/products/{id}` | Admin | FR-ADMIN-PROD-04 |
| Admin Products | `DELETE` | `/admin/products/{id}` | Admin | FR-ADMIN-PROD-07 / UC-ADMIN-PROD-01 |
| Admin Categories | `GET` | `/admin/categories` | Admin | FR-ADMIN-CAT-01 / UC-ADMIN-CAT-01 |
| Admin Categories | `POST` | `/admin/categories` | Admin | FR-ADMIN-CAT-02 / UC-ADMIN-CAT-01 |
| Admin Categories | `PUT` | `/admin/categories/{id}` | Admin | FR-ADMIN-CAT-03 / UC-ADMIN-CAT-01 |
| Admin Categories | `DELETE` | `/admin/categories/{id}` | Admin | FR-ADMIN-CAT-04 / UC-ADMIN-CAT-01 |
| Admin Orders | `GET` | `/admin/orders` | Admin | FR-ADMIN-ORDER-01, FR-ADMIN-ORDER-02, FR-ADMIN-ORDER-03 / UC-ADMIN-ORDER-01 |
| Admin Orders | `GET` | `/admin/orders/{id}` | Admin | FR-ADMIN-ORDER-04 / UC-ADMIN-ORDER-02 |
| Admin Orders | `PATCH` | `/admin/orders/{id}/status` | Admin | FR-ADMIN-ORDER-05 / UC-ADMIN-ORDER-03 |


---

# 6. Authentication APIs

## 6.1. Register Customer

### Endpoint

```http
POST /auth/register
```

### Access

```text
Public
```

### Description

Cho phép Guest tạo tài khoản Customer.

Admin không được đăng ký thông qua endpoint này.

### Request Body

```json
{
  "email": "customer@example.com",
  "password": "SecurePassword123"
}
```

### Request Fields

| Field | Type | Required | Validation |
|---|---|---:|---|
| `email` | String | Yes | Email hợp lệ, không trùng |
| `password` | String | Yes | Tối thiểu 8 ký tự |

### Success Response

**HTTP 201 Created**

```json
{
  "success": true,
  "message": "Customer registered successfully",
  "data": {
    "id": 1,
    "email": "customer@example.com",
    "role": "CUSTOMER",
    "createdAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Email/password không hợp lệ |
| `409` | Email đã tồn tại |

### Business Rules

- Chỉ tạo tài khoản với role `CUSTOMER`.
- Password không được lưu dưới dạng plain text.
- Password phải được hash trước khi lưu.
- Không có Email Verification trong MVP.
- Không có Password Reset trong MVP.
- `confirmPassword`, nếu có trên Frontend, chỉ dùng để validation và không được gửi/lưu như một trường Database.

---

## 6.2. Login

### Endpoint

```http
POST /auth/login
```

### Access

```text
Public
```

### Description

Xác thực người dùng bằng email và password.

### Request Body

```json
{
  "email": "customer@example.com",
  "password": "SecurePassword123"
}
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "email": "customer@example.com",
      "role": "CUSTOMER"
    }
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Request không hợp lệ |
| `401` | Email hoặc password không đúng |

MVP không có Account Status, do đó không sử dụng lỗi `403 Account Disabled`.

---

# 7. Product APIs

## 7.1. Get Product List

### Endpoint

```http
GET /products
```

### Access

```text
Public
```

### Description

Lấy danh sách sản phẩm đang hoạt động.

Guest có thể sử dụng API này mà không cần đăng nhập.

### Query Parameters

| Parameter | Type | Required | Description |
|---|---|---:|---|
| `keyword` | String | No | Tìm không phân biệt hoa thường theo Name, SKU hoặc Description |
| `categoryId` | Long | No | Lọc theo danh mục |
| `minPrice` | Decimal | No | Giá tối thiểu |
| `maxPrice` | Decimal | No | Giá tối đa |
| `sort` | String | No | `name`, `price`, `createdAt` |
| `direction` | String | No | `ASC` hoặc `DESC` |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang, tối đa `50` |

### Example Request

```http
GET /products?keyword=keyboard&categoryId=2&page=0&size=10
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "content": [
      {
        "id": 1,
        "sku": "KB-001",
        "name": "Mechanical Keyboard",
        "description": "Mechanical keyboard for office use",
        "price": 850000.0,
        "stockQuantity": 20,
        "imageUrl": "https://example.com/images/keyboard.jpg",
        "category": {
          "id": 2,
          "name": "Accessories"
        },
        "status": "ACTIVE"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Business Rules

- Chỉ trả về Product có `status = ACTIVE`.
- Keyword tìm kiếm trên `name`, `sku`, `description`.
- Tìm kiếm không phân biệt hoa thường.
- `stockQuantity` không được âm.
- `price > 0`.
- `size` tối đa `50`.

---

## 7.2. Get Product Detail

### Endpoint

```http
GET /products/{id}
```

### Access

```text
Public
```

### Description

Lấy thông tin chi tiết Product.

Product `INACTIVE` vẫn có thể được trả về để Frontend hiển thị trạng thái không khả dụng.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1,
    "sku": "KB-001",
    "name": "Mechanical Keyboard",
    "description": "Mechanical keyboard for office use",
    "price": 850000.0,
    "stockQuantity": 20,
    "imageUrl": "https://example.com/images/keyboard.jpg",
    "category": {
      "id": 2,
      "name": "Accessories"
    },
    "status": "ACTIVE",
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `404` | Không tìm thấy Product |

### Business Rules

- Product `INACTIVE` trả về `status = INACTIVE`.
- Frontend phải hiển thị Product không khả dụng.
- Frontend phải disable Add to Cart đối với Product `INACTIVE`.
- Product `INACTIVE` không được thêm vào Cart.

---

# 8. Category APIs

## 8.1. Get Category List

### Endpoint

```http
GET /categories
```

### Access

```text
Public
```

### Description

Lấy danh sách Category đang hoạt động.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": [
    {
      "id": 1,
      "name": "Electronics",
      "description": "Electronic products",
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "name": "Accessories",
      "description": "Computer accessories",
      "status": "ACTIVE"
    }
  ]
}
```

### Business Rules

- Chỉ trả về Category có `status = ACTIVE`.
- Category `INACTIVE` không được hiển thị trong public Category List.

---

## 8.2. Get Category Detail

### Endpoint

```http
GET /categories/{id}
```

### Access

```text
Public
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 2,
    "name": "Accessories",
    "description": "Computer accessories",
    "status": "ACTIVE"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `404` | Không tìm thấy Category |

---

# 9. Cart APIs

## 9.1. Get Current Cart

### Endpoint

```http
GET /cart
```

### Access

```text
CUSTOMER
```

### Description

Lấy Cart đang hoạt động của Customer hiện tại.

Guest không được sử dụng Cart.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1,
    "items": [
      {
        "id": 10,
        "product": {
          "id": 1,
          "sku": "KB-001",
          "name": "Mechanical Keyboard",
          "price": 850000.0,
          "stockQuantity": 20,
          "imageUrl": "https://example.com/images/keyboard.jpg",
          "status": "ACTIVE"
        },
        "quantity": 2,
        "subtotal": 1700000.0,
        "available": true,
        "unavailableReason": null
      }
    ],
    "totalAmount": 1700000.0,
    "checkoutAllowed": true,
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Availability Values

`unavailableReason` có thể nhận:

```text
INACTIVE
OUT_OF_STOCK
EXCEEDS_STOCK
```

### Business Rules

- Mỗi Customer chỉ có tối đa một Cart đang hoạt động.
- Cart được lưu trong Database.
- CartItem không được trùng Product trong cùng Cart.
- Guest không có Cart trong MVP.
- `available = true` khi Product ACTIVE và quantity không vượt quá stock.
- `available = false` khi Product INACTIVE, hết stock hoặc quantity vượt stock.
- `checkoutAllowed = true` chỉ khi Cart không rỗng và tất cả CartItem đều available.
- Frontend phải disable Checkout khi `checkoutAllowed = false`.

---

## 9.2. Add Product to Cart

### Endpoint

```http
POST /cart/items
```

### Access

```text
CUSTOMER
```

### Request Body

```json
{
  "productId": 1,
  "quantity": 2
}
```

### Request Fields

| Field | Type | Required | Validation |
|---|---|---:|---|
| `productId` | Long | Yes | Product phải tồn tại và ACTIVE |
| `quantity` | Integer | Yes | Lớn hơn 0, không vượt quá stock |

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Product added to cart successfully",
  "data": {
    "productId": 1,
    "quantity": 2,
    "subtotal": 1700000.0
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Quantity không hợp lệ |
| `401` | Chưa đăng nhập |
| `404` | Không tìm thấy Product |
| `409` | Số lượng vượt quá tồn kho |
| `409` | Product không hoạt động |

---

## 9.3. Update Cart Item

### Endpoint

```http
PUT /cart/items/{productId}
```

### Access

```text
CUSTOMER
```

### Request Body

```json
{
  "quantity": 3
}
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Cart item updated successfully",
  "data": {
    "productId": 1,
    "quantity": 3,
    "subtotal": 2550000.0
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Quantity không hợp lệ |
| `401` | Chưa đăng nhập |
| `404` | CartItem không tồn tại |
| `409` | Quantity vượt quá stock |
| `409` | Product không hoạt động |

---

## 9.4. Remove Cart Item

### Endpoint

```http
DELETE /cart/items/{productId}
```

### Access

```text
CUSTOMER
```

### Description

Xóa một Product khỏi Cart hiện tại.

### Success Response

**HTTP 204 No Content**

Không trả về response body.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `404` | CartItem không tồn tại |

---

# 10. Checkout and Order Creation APIs

## 10.1. Checkout and Create Order

### Endpoint

```http
POST /orders/checkout
```

### Access

```text
CUSTOMER
```

### Description

Tạo Order từ Cart hiện tại của Customer.

Checkout bao gồm:

1. Kiểm tra authentication.
2. Kiểm tra Cart không rỗng.
3. Kiểm tra Product còn hoạt động.
4. Kiểm tra tồn kho.
5. Lấy thông tin giao hàng.
6. Tính tổng tiền.
7. Tạo Order.
8. Tạo OrderItem với snapshot thông tin Product.
9. Trừ tồn kho.
10. Xóa CartItem.
11. Commit transaction.

### Request Body

```json
{
  "recipientName": "Nguyen Minh Quan",
  "phone": "0912345678",
  "address": "12 Example Street",
  "provinceCity": "Ha Noi",
  "district": "Hoan Kiem",
  "ward": "Hang Trong"
}
```

### Request Fields

| Field | Type | Required | Validation |
|---|---|---:|---|
| `recipientName` | String | Yes | Không rỗng |
| `phone` | String | Yes | Định dạng số điện thoại hợp lệ |
| `address` | String | Yes | Không rỗng |
| `provinceCity` | String | Yes | Không rỗng |
| `district` | String | Yes | Không rỗng |
| `ward` | String | Yes | Không rỗng |

### Payment Behavior

MVP không có:

- Payment Method selection.
- Payment Gateway.
- Online Payment Provider.
- Bank Transfer.
- E-Wallet.
- Payment Entity/Table.

Khi Order được tạo:

```text
orderStatus = PENDING
paymentStatus = UNPAID
paidAt = NULL
```

### Success Response

**HTTP 201 Created**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1001,
    "customerId": 1,
    "totalAmount": 1700000.0,
    "recipientName": "Nguyen Minh Quan",
    "phone": "0912345678",
    "address": "12 Example Street",
    "provinceCity": "Ha Noi",
    "district": "Hoan Kiem",
    "ward": "Hang Trong",
    "orderStatus": "PENDING",
    "paymentStatus": "UNPAID",
    "paidAt": null,
    "items": [
      {
        "id": 5001,
        "productId": 1,
        "productNameSnapshot": "Mechanical Keyboard",
        "skuSnapshot": "KB-001",
        "unitPriceSnapshot": 850000.0,
        "quantity": 2,
        "itemTotal": 1700000.0
      }
    ],
    "createdAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Shipping information không hợp lệ |
| `401` | Chưa đăng nhập |
| `409` | Cart rỗng |
| `409` | Product không còn hoạt động |
| `409` | Không đủ tồn kho |
| `500` | Transaction thất bại |

### Transaction Rule

Nếu bất kỳ bước nào trong Checkout thất bại:

```text
Không tạo Order hoàn chỉnh
Không trừ stock
Không xóa CartItem
```

Toàn bộ transaction phải được rollback.

---

# 11. Customer Order APIs

## 11.1. Get My Orders

### Endpoint

```http
GET /orders/my-orders
```

### Access

```text
CUSTOMER
```

### Query Parameters

| Parameter | Type | Required | Description |
|---|---|---:|---|
| `status` | String | No | Lọc theo Order Status |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang, tối đa `50` |
| `sort` | String | No | Trường sắp xếp |
| `direction` | String | No | `ASC` hoặc `DESC` |

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "content": [
      {
        "id": 1001,
        "totalAmount": 1700000.0,
        "orderStatus": "PENDING",
        "paymentStatus": "UNPAID",
        "paidAt": null,
        "createdAt": "2026-09-24T14:30:00"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Business Rules

- Customer chỉ xem được Order của chính mình.
- Không được xem Order của Customer khác.
- Có thể lọc theo Order Status.

---

## 11.2. Get Order Detail

### Endpoint

```http
GET /orders/{id}
```

### Access

```text
CUSTOMER
```

### Description

Lấy thông tin chi tiết Order của Customer hiện tại.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1001,
    "customerId": 1,
    "totalAmount": 1700000.0,
    "recipientName": "Nguyen Minh Quan",
    "phone": "0912345678",
    "address": "12 Example Street",
    "provinceCity": "Ha Noi",
    "district": "Hoan Kiem",
    "ward": "Hang Trong",
    "orderStatus": "PENDING",
    "paymentStatus": "UNPAID",
    "paidAt": null,
    "items": [
      {
        "id": 5001,
        "productId": 1,
        "productNameSnapshot": "Mechanical Keyboard",
        "skuSnapshot": "KB-001",
        "unitPriceSnapshot": 850000.0,
        "quantity": 2,
        "itemTotal": 1700000.0
      }
    ],
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Authorization Rules

- Customer chỉ được xem Order của bản thân.
- Customer không được xem Order của Customer khác.
- Admin sử dụng endpoint riêng `GET /admin/orders/{id}`.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không có quyền với Order |
| `404` | Không tìm thấy Order |

---

## 11.3. Cancel Order

### Endpoint

```http
PATCH /orders/{id}/cancel
```

### Access

```text
CUSTOMER
```

### Description

Customer hủy Order của chính mình.

### Request Body

Không yêu cầu request body.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Order cancelled successfully",
  "data": {
    "id": 1001,
    "orderStatus": "CANCELLED",
    "paymentStatus": "UNPAID",
    "paidAt": null
  }
}
```

### Business Rules

- Customer chỉ được hủy Order của chính mình.
- Chỉ Order có trạng thái `PENDING` mới được hủy.
- Khi hủy thành công, hệ thống hoàn lại stock đúng một lần.
- Việc cập nhật trạng thái và hoàn stock phải nằm trong cùng một transaction.
- Order đã `CANCELLED` không được hủy lại.
- Không được hủy Order đã `CONFIRMED`, `SHIPPING` hoặc `DELIVERED`.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không có quyền với Order |
| `404` | Không tìm thấy Order |
| `409` | Order không ở trạng thái `PENDING` |

---

# 12. Admin Product APIs

## 12.1. Get Admin Product List

### Endpoint

```http
GET /admin/products
```

### Access

```text
ADMIN
```

### Description

Lấy danh sách Product dành cho Admin, bao gồm cả Product `ACTIVE` và `INACTIVE`.

### Query Parameters

| Parameter | Type | Required | Description |
|---|---|---:|---|
| `keyword` | String | No | Tìm không phân biệt hoa thường theo Name, SKU hoặc Description |
| `categoryId` | Long | No | Lọc theo Category |
| `status` | String | No | `ACTIVE` hoặc `INACTIVE` |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang, tối đa `50` |
| `sort` | String | No | Trường sắp xếp |
| `direction` | String | No | `ASC` hoặc `DESC` |

### Example Request

```http
GET /admin/products?keyword=keyboard&status=ACTIVE&page=0&size=10
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "content": [
      {
        "id": 1,
        "sku": "KB-001",
        "name": "Mechanical Keyboard",
        "description": "Mechanical keyboard for office use",
        "price": 850000.0,
        "stockQuantity": 20,
        "imageUrl": "https://example.com/images/keyboard.jpg",
        "categoryId": 2,
        "status": "ACTIVE",
        "createdAt": "2026-09-24T14:30:00",
        "updatedAt": "2026-09-24T14:30:00"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `400` | Query parameter không hợp lệ |

---

## 12.2. Get Admin Product Detail

### Endpoint

```http
GET /admin/products/{id}
```

### Access

```text
ADMIN
```

### Description

Lấy thông tin chi tiết Product dành cho Admin, bao gồm cả Product `ACTIVE` và `INACTIVE`.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1,
    "sku": "KB-001",
    "name": "Mechanical Keyboard",
    "description": "Mechanical keyboard for office use",
    "price": 850000.0,
    "stockQuantity": 20,
    "imageUrl": "https://example.com/images/keyboard.jpg",
    "category": {
      "id": 2,
      "name": "Accessories"
    },
    "status": "ACTIVE",
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Product |

---

## 12.3. Create Product

### Endpoint

```http
POST /admin/products
```

### Access

```text
ADMIN
```

### Request Body

```json
{
  "sku": "KB-001",
  "name": "Mechanical Keyboard",
  "description": "Mechanical keyboard for office use",
  "price": 850000.00,
  "stockQuantity": 20,
  "imageUrl": "https://example.com/images/keyboard.jpg",
  "categoryId": 2,
  "status": "ACTIVE"
}
```

### Request Fields

| Field | Type | Required | Validation |
|---|---|---:|---|
| `sku` | String | Yes | Unique, không rỗng |
| `name` | String | Yes | Không rỗng |
| `description` | String | No | Nội dung mô tả |
| `price` | Decimal | Yes | `> 0` |
| `stockQuantity` | Integer | Yes | `>= 0` |
| `imageUrl` | String | No | URL hợp lệ nếu có |
| `categoryId` | Long | Yes | Category phải tồn tại |
| `status` | Enum | Yes | `ACTIVE` hoặc `INACTIVE` |

### Success Response

**HTTP 201 Created**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1,
    "sku": "KB-001",
    "name": "Mechanical Keyboard",
    "description": "Mechanical keyboard for office use",
    "price": 850000.0,
    "stockQuantity": 20,
    "imageUrl": "https://example.com/images/keyboard.jpg",
    "categoryId": 2,
    "status": "ACTIVE",
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Dữ liệu không hợp lệ |
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Category không tồn tại |
| `409` | SKU đã tồn tại |

---

## 12.4. Update Product

### Endpoint

```http
PUT /admin/products/{id}
```

### Access

```text
ADMIN
```

### Request Body

```json
{
  "sku": "KB-001",
  "name": "Mechanical Keyboard Updated",
  "description": "Updated description",
  "price": 900000.00,
  "stockQuantity": 25,
  "imageUrl": "https://example.com/images/keyboard-new.jpg",
  "categoryId": 2,
  "status": "ACTIVE"
}
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1,
    "sku": "KB-001",
    "name": "Mechanical Keyboard Updated",
    "description": "Updated description",
    "price": 900000.0,
    "stockQuantity": 25,
    "imageUrl": "https://example.com/images/keyboard-new.jpg",
    "categoryId": 2,
    "status": "ACTIVE",
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T15:00:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Dữ liệu không hợp lệ |
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Product hoặc Category không tồn tại |
| `409` | SKU đã tồn tại |

### Business Rules

- Không cho phép SKU trùng với Product khác.
- Giá phải lớn hơn `0`.
- Stock không được âm.
- Không thay đổi dữ liệu snapshot đã lưu trong OrderItem.
- Thay đổi giá Product không làm thay đổi `unitPrice` của OrderItem cũ.

---

## 12.5. Delete Product

### Endpoint

```http
DELETE /admin/products/{id}
```

### Access

```text
ADMIN
```

### Description

Xóa Product theo cơ chế Soft Delete.

DELETE không xóa vật lý Product khỏi Database.

Hệ thống chuyển:

```text
status = INACTIVE
```

### Success Response

**HTTP 204 No Content**

Không trả về response body.

### Business Rules

- DELETE Product luôn thực hiện Soft Delete.
- Product sau khi DELETE có `status = INACTIVE`.
- Không xóa vật lý Product khỏi Database.
- Product `INACTIVE` không xuất hiện trong public Product List.
- Product `INACTIVE` không được thêm vào Cart.
- Không làm mất dữ liệu snapshot trong OrderItem.
- Nếu Product đã `INACTIVE`, DELETE tiếp tục không làm thay đổi dữ liệu.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Product |

---

# 13. Admin Category APIs

## 13.1. Get Admin Category List

### Endpoint

```http
GET /admin/categories
```

### Access

```text
ADMIN
```

### Description

Lấy danh sách Category dành cho Admin, bao gồm cả Category `ACTIVE` và `INACTIVE`.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Categories retrieved successfully",
  "data": []
}
```

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |

---

## 13.2. Create Category

### Endpoint

```http
POST /admin/categories
```

### Access

```text
ADMIN
```

### Request Body

```json
{
  "name": "Accessories",
  "description": "Computer accessories",
  "status": "ACTIVE"
}
```

### Success Response

**HTTP 201 Created**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 2,
    "name": "Accessories",
    "description": "Computer accessories",
    "status": "ACTIVE"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Dữ liệu không hợp lệ |
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `409` | Category name đã tồn tại |

---

## 13.3. Update Category

### Endpoint

```http
PUT /admin/categories/{id}
```

### Access

```text
ADMIN
```

### Request Body

```json
{
  "name": "Computer Accessories",
  "description": "Updated description",
  "status": "ACTIVE"
}
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Category updated successfully",
  "data": {
    "id": 2,
    "name": "Computer Accessories",
    "description": "Updated description",
    "status": "ACTIVE"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Dữ liệu không hợp lệ |
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Category |
| `409` | Category name đã tồn tại |

### Business Rules

- Admin có thể chuyển Category sang `ACTIVE` hoặc `INACTIVE`.
- Category `INACTIVE` không được hiển thị trong public Category List.
- Category `INACTIVE` không được sử dụng để tạo Product mới.

---

## 13.4. Delete Category

### Endpoint

```http
DELETE /admin/categories/{id}
```

### Access

```text
ADMIN
```

### Description

Xóa Category.

### Business Rules

- Nếu Category không được Product nào tham chiếu, hệ thống cho phép xóa.
- Nếu Category đang được Product tham chiếu, hệ thống phải từ chối thao tác xóa.
- Không tự động chuyển Category sang `INACTIVE` khi gọi DELETE.
- Nếu Admin muốn ẩn Category, sử dụng:

```http
PUT /admin/categories/{id}
```

với:

```json
{
  "status": "INACTIVE"
}
```

- Không làm mất dữ liệu Product hiện tại.

### Success Response

**HTTP 204 No Content**

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Category |
| `409` | Category đang được Product tham chiếu |

---

# 14. Admin Order APIs

## 14.1. Get All Orders

### Endpoint

```http
GET /admin/orders
```

### Access

```text
ADMIN
```

### Query Parameters

| Parameter | Type | Required | Description |
|---|---|---:|---|
| `keyword` | String | No | Tìm theo Order ID, Customer Email, Recipient Name hoặc Phone |
| `status` | String | No | Lọc theo Order Status |
| `paymentStatus` | String | No | `UNPAID` hoặc `PAID` |
| `customerId` | Long | No | Lọc theo Customer |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang, tối đa `50` |
| `sort` | String | No | Trường sắp xếp |
| `direction` | String | No | `ASC` hoặc `DESC` |

### Example Request

```http
GET /admin/orders?keyword=quan&status=PENDING&page=0&size=10
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "content": [
      {
        "id": 1001,
        "customerId": 1,
        "customerEmail": "customer@example.com",
        "recipientName": "Nguyen Minh Quan",
        "phone": "0912345678",
        "totalAmount": 1700000.0,
        "orderStatus": "PENDING",
        "paymentStatus": "UNPAID",
        "paidAt": null,
        "createdAt": "2026-09-24T14:30:00"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### Search Rules

`keyword` được đối chiếu với:

- Order ID.
- Customer Email.
- `recipientName`.
- `phone`.

Tên và số điện thoại sử dụng thông tin giao hàng được snapshot trong Order; không yêu cầu thêm `name` hoặc `phone` vào User chỉ để phục vụ tìm kiếm.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `400` | Query parameter không hợp lệ |

---

## 14.2. Get Admin Order Detail

### Endpoint

```http
GET /admin/orders/{id}
```

### Access

```text
ADMIN
```

### Description

Lấy toàn bộ thông tin chi tiết của một Order.

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Request successful",
  "data": {
    "id": 1001,
    "customerId": 1,
    "customerEmail": "customer@example.com",
    "totalAmount": 1700000.0,
    "recipientName": "Nguyen Minh Quan",
    "phone": "0912345678",
    "address": "12 Example Street",
    "provinceCity": "Ha Noi",
    "district": "Hoan Kiem",
    "ward": "Hang Trong",
    "orderStatus": "PENDING",
    "paymentStatus": "UNPAID",
    "paidAt": null,
    "items": [
      {
        "id": 5001,
        "productId": 1,
        "productNameSnapshot": "Mechanical Keyboard",
        "skuSnapshot": "KB-001",
        "unitPriceSnapshot": 850000.0,
        "quantity": 2,
        "itemTotal": 1700000.0
      }
    ],
    "createdAt": "2026-09-24T14:30:00",
    "updatedAt": "2026-09-24T14:30:00"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Order |

---

## 14.3. Update Order Status

### Endpoint

```http
PATCH /admin/orders/{id}/status
```

### Access

```text
ADMIN
```

### Request Body

```json
{
  "status": "CONFIRMED"
}
```

### Supported Status Values

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

### Success Response

**HTTP 200 OK**

```json
{
  "success": true,
  "message": "Order status updated successfully",
  "data": {
    "id": 1001,
    "orderStatus": "CONFIRMED",
    "paymentStatus": "UNPAID",
    "paidAt": null
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Status không hợp lệ |
| `401` | Chưa đăng nhập |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Order |
| `409` | Chuyển trạng thái không hợp lệ |

---

## 14.4. Order Status Transition Rules

Các chuyển đổi hợp lệ:

```text
PENDING → CONFIRMED
CONFIRMED → SHIPPING
SHIPPING → DELIVERED
PENDING → CANCELLED
```

Các chuyển đổi không hợp lệ phải bị từ chối.

Ví dụ:

```text
DELIVERED → PENDING
CANCELLED → CONFIRMED
SHIPPING → PENDING
CONFIRMED → CANCELLED
```

### Admin Cancellation and Stock Restoration

Khi Admin chuyển Order từ:

```text
PENDING → CANCELLED
```

hệ thống phải:

1. Kiểm tra trạng thái hiện tại là `PENDING`.
2. Chuyển Order sang `CANCELLED`.
3. Hoàn lại stock tương ứng với tất cả OrderItem.
4. Commit trong cùng một transaction.

Chỉ được hoàn stock khi chuyển trạng thái từ `PENDING` sang `CANCELLED` thành công.

Nếu Order đã `CANCELLED`, không được hoàn stock lần thứ hai.

### Automatic Payment Update

Khi Admin chuyển Order sang:

```text
DELIVERED
```

hệ thống tự động thực hiện:

```text
orderStatus = DELIVERED
paymentStatus = PAID
paidAt = current timestamp
```

Ví dụ response:

```json
{
  "id": 1001,
  "orderStatus": "DELIVERED",
  "paymentStatus": "PAID",
  "paidAt": "2026-09-24T16:45:00",
  "message": "Order delivered and payment status updated"
}
```

### Additional Rules

- Client không được tự gửi `paymentStatus = PAID` trong request thông thường.
- Client không được tự gửi `paidAt`.
- `paymentStatus` và `paidAt` được Backend kiểm soát.
- Khi Order chưa `DELIVERED`:

```text
paymentStatus = UNPAID
paidAt = NULL
```

- Không tạo Payment API riêng trong MVP.

---

# 15. HTTP Status Code Convention

| Status Code | Usage |
|---|---|
| `200 OK` | Request thành công |
| `201 Created` | Tạo resource thành công |
| `204 No Content` | Thành công nhưng không có response body |
| `400 Bad Request` | Request không hợp lệ |
| `401 Unauthorized` | Chưa xác thực |
| `403 Forbidden` | Không có quyền |
| `404 Not Found` | Không tìm thấy resource |
| `409 Conflict` | Vi phạm business rule hoặc conflict dữ liệu |
| `500 Internal Server Error` | Lỗi hệ thống không dự kiến |

---

# 16. API Validation Rules

## 16.1. Product

```text
SKU không được rỗng
SKU phải unique
Name không được rỗng
Price > 0
Stock Quantity >= 0
Category phải tồn tại
Status chỉ nhận ACTIVE hoặc INACTIVE
Keyword tìm kiếm trên Name, SKU, Description
```

## 16.2. Category

```text
Name không được rỗng
Name phải unique
Status chỉ nhận ACTIVE hoặc INACTIVE
Không xóa Category đang được Product tham chiếu
Category đang được sử dụng có thể chuyển sang INACTIVE
```

## 16.3. Cart

```text
Quantity > 0
Quantity không vượt quá stock
Mỗi Product chỉ xuất hiện một lần trong Cart
Guest không được sử dụng Cart
Product INACTIVE không được thêm vào Cart
Checkout chỉ được phép khi tất cả CartItem hợp lệ
```

## 16.4. Order

```text
Cart không được rỗng khi Checkout
Product phải ACTIVE tại thời điểm Checkout
Stock phải đủ
Tổng tiền được tính tại Backend
OrderItem lưu snapshot thông tin Product
Customer chỉ xem/hủy Order của chính mình
Customer chỉ hủy Order PENDING
Admin chỉ chuyển Order theo transition matrix
PENDING → CANCELLED phải hoàn stock đúng một lần
```

## 16.5. Payment

```text
Payment Status chỉ nhận UNPAID hoặc PAID
paidAt có thể NULL
Khi Order = DELIVERED:
    paymentStatus = PAID
    paidAt = current timestamp
Khi Order != DELIVERED:
    paymentStatus = UNPAID
    paidAt = NULL
Không có Payment Entity/Table
Không có Payment Method API
Không có Payment Gateway API
```

---

# 17. Security Requirements

## 17.1. Authentication

- Các API yêu cầu đăng nhập phải kiểm tra JWT.
- JWT không hợp lệ phải trả `401 Unauthorized`.
- Không lưu password dạng plain text.
- Password phải được hash trước khi lưu.

## 17.2. Authorization

- Customer không được truy cập Admin API.
- Customer chỉ được truy cập Cart của chính mình.
- Customer chỉ được xem và hủy Order của chính mình.
- Admin API phải yêu cầu role `ADMIN`.

## 17.3. Input Validation

Backend phải validate request dù Frontend đã có validation.

Không tin tưởng các giá trị sau do client gửi lên:

```text
customerId
totalAmount
paymentStatus
paidAt
orderStatus
```

Đối với `orderStatus`, Backend phải kiểm tra:

```text
- Role của client.
- Trạng thái hiện tại của Order.
- Transition matrix được phép.
```

Các giá trị quan trọng phải được xác định hoặc kiểm tra tại Backend.

## 17.4. Sensitive Data

Không trả về:

```text
passwordHash
JWT secret
database credentials
internal stack trace
```

trong API response.

---

# 18. Transactional Operations

Các nghiệp vụ sau cần được xử lý trong transaction.

## 18.1. Checkout

```text
Validate Cart
Validate Products
Validate Stock
Create Order
Create OrderItems
Deduct Stock
Clear Cart
Commit
```

Nếu thất bại:

```text
Rollback toàn bộ thay đổi
```

### Stock Concurrency

Khi trừ stock trong Checkout, hệ thống phải sử dụng thao tác cập nhật có điều kiện để tránh overselling khi có nhiều request đồng thời.

Ví dụ:

```sql
UPDATE products
SET stock_quantity = stock_quantity - :quantity
WHERE id = :productId
  AND stock_quantity >= :quantity;
```

Backend phải kiểm tra số dòng bị ảnh hưởng:

```text
1 = Trừ stock thành công
0 = Stock không đủ hoặc Product không còn hợp lệ
```

Nếu bất kỳ Product nào không thể trừ stock, toàn bộ Checkout transaction phải rollback.

---

## 18.2. Cancel Order

Customer hoặc Admin khi hủy Order:

```text
Validate Order Status
Restore Stock
Update Order Status
Commit
```

Chỉ được hoàn stock khi Order chuyển thành công:

```text
PENDING → CANCELLED
```

Không được hoàn stock nhiều lần nếu cùng một Order bị xử lý lại.

---

## 18.3. Mark Order as Delivered

```text
Validate Status Transition
Update orderStatus = DELIVERED
Update paymentStatus = PAID
Set paidAt = current timestamp
Commit
```

---

# 19. Out of Scope

Các API sau không thuộc phạm vi MVP:

```text
Payment API
Payment Method API
Payment Gateway API
Bank Transfer API
E-Wallet API
Online Payment Provider API
Coupon API
Promotion API
Guest Cart API
Merge Cart API
Email Verification API
Password Reset API
Multiple Address API
Shipping Method Selection API
```

---

# 20. API Testing Checklist

## Authentication

- [ ] Register Customer thành công.
- [ ] Không cho phép email trùng.
- [ ] Password dưới 8 ký tự bị từ chối.
- [ ] Login thành công.
- [ ] Login sai password bị từ chối.
- [ ] API yêu cầu authentication từ chối request không có JWT.
- [ ] Customer không thể truy cập Admin API.

## Products

- [ ] Guest xem được Product List.
- [ ] Guest tìm kiếm được theo Name.
- [ ] Guest tìm kiếm được theo SKU.
- [ ] Guest tìm kiếm được theo Description.
- [ ] Guest lọc được theo Category.
- [ ] Guest lọc được theo khoảng giá.
- [ ] Không hiển thị Product `INACTIVE` trong public Product List.
- [ ] Product Detail có thể trả Product `INACTIVE` với trạng thái không khả dụng.
- [ ] Admin xem được Product `ACTIVE` và `INACTIVE`.
- [ ] Admin tạo Product thành công.
- [ ] Không cho phép SKU trùng.
- [ ] Không cho phép Price `<= 0`.
- [ ] Không cho phép Stock âm.
- [ ] DELETE Product chuyển Product sang `INACTIVE`.

## Categories

- [ ] Guest xem được Category ACTIVE.
- [ ] Admin tạo Category thành công.
- [ ] Không cho phép Category name trùng.
- [ ] Admin cập nhật Category.
- [ ] Category đang được Product tham chiếu không thể DELETE.
- [ ] DELETE Category đang được tham chiếu trả `409`.
- [ ] Category đang được sử dụng có thể chuyển sang `INACTIVE`.

## Cart

- [ ] Guest không truy cập được Cart.
- [ ] Customer thêm Product vào Cart.
- [ ] Không thêm Product `INACTIVE`.
- [ ] Không thêm số lượng vượt stock.
- [ ] Không tạo CartItem trùng Product.
- [ ] Customer cập nhật quantity.
- [ ] Customer xóa CartItem.
- [ ] Customer clear Cart.
- [ ] Cart trả về trạng thái `available`.
- [ ] Cart trả về `unavailableReason` khi cần.
- [ ] Cart trả về `checkoutAllowed = false` khi Cart không hợp lệ.

## Checkout

- [ ] Không checkout khi Cart rỗng.
- [ ] Không checkout khi stock không đủ.
- [ ] Không checkout Product `INACTIVE`.
- [ ] Shipping information phải đầy đủ.
- [ ] Tổng tiền được tính đúng.
- [ ] OrderItem lưu đúng snapshot.
- [ ] Stock được trừ đúng.
- [ ] Cart được clear sau Checkout thành công.
- [ ] Transaction rollback khi có lỗi.
- [ ] Concurrent checkout không làm stock âm hoặc overselling.

## Customer Orders

- [ ] Customer xem được Order của mình.
- [ ] Customer không xem được Order của Customer khác.
- [ ] Customer chỉ hủy được Order `PENDING`.
- [ ] Hủy Order hoàn stock đúng một lần.
- [ ] Customer không thể hủy Order `CONFIRMED`.
- [ ] Customer không thể hủy Order `SHIPPING`.
- [ ] Customer không thể hủy Order `DELIVERED`.

## Admin Orders

- [ ] Admin xem được tất cả Order.
- [ ] Admin tìm kiếm Order theo Order ID.
- [ ] Admin tìm kiếm Order theo Customer Email.
- [ ] Admin tìm kiếm Order theo Recipient Name.
- [ ] Admin tìm kiếm Order theo Phone.
- [ ] Admin lọc được theo Order Status.
- [ ] Admin lọc được theo Payment Status.
- [ ] Admin xem được Order Detail.
- [ ] Admin chỉ chuyển Order theo transition matrix.
- [ ] Admin có thể chuyển `PENDING → CANCELLED`.
- [ ] Admin cancel Order hoàn stock đúng một lần.
- [ ] Khi Order `DELIVERED`, paymentStatus tự chuyển `PAID`.
- [ ] Khi Order `DELIVERED`, paidAt được gán timestamp.
- [ ] Client không thể tự cập nhật paymentStatus hoặc paidAt.

---

# 21. Implementation Notes

API Specification này mô tả contract ở mức nghiệp vụ và HTTP interface.

Trong quá trình triển khai, team cần thống nhất thêm:

- Tên package.
- Cấu trúc DTO.
- Cơ chế JWT.
- Cách format validation error.
- Cách xử lý exception.
- Chi tiết database migration.
- Cách cấu hình CORS.
- Cách version API nếu cần.

Các quyết định kỹ thuật quan trọng đã được xác định trong API Specification:

- Product DELETE sử dụng Soft Delete bằng `status = INACTIVE`.
- Category đang được Product tham chiếu không được DELETE.
- Stock Checkout phải xử lý concurrency để tránh overselling.
- Checkout và Order Cancellation phải được xử lý trong transaction.
- Admin Cancellation phải hoàn stock.
- `paymentStatus` và `paidAt` do Backend kiểm soát.
- `paymentStatus = PAID` chỉ được tự động thiết lập khi Order chuyển sang `DELIVERED`.
- `paidAt` là nullable.
- Guest không có Cart.
- Customer chỉ được xem/hủy Order của chính mình.
- Admin sử dụng Admin Order API riêng.

Mọi thay đổi API làm ảnh hưởng đến requirement hoặc data model phải được cập nhật đồng bộ trong:

```text
docs/01-requirements/
docs/02-design/
docs/03-api/
```

## Change Log

| Version | Date | Changes |
|---|---|---|
| Revised after Review Round 2 | 2026-10-07 | Chuẩn hóa Success Response theo `{ success, message, data }`; đổi `unitPrice` thành `unitPriceSnapshot`; cập nhật FR/UC mapping trong API Summary; bổ sung Admin Category List API; loại bỏ Clear Cart khỏi Testing Checklist; cập nhật Security Rule cho `orderStatus`; chuẩn hóa các response có `message` nằm trong `data`. |
