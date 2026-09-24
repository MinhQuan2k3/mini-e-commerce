# API Specification

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
| `size` | Integer | `10` | Số phần tử mỗi trang |
| `sort` | String | Tùy API | Trường dùng để sắp xếp |
| `direction` | String | `ASC` | `ASC` hoặc `DESC` |

Ví dụ:

```http
GET /products?page=0&size=10&sort=name&direction=ASC
```

## 4.5. Common Error Response

```json
{
  "timestamp": "2026-09-24T14:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request data",
  "path": "/api/products"
}
```

---

# 5. API Summary

| Module | Method | Endpoint | Access |
|---|---|---|---|
| Authentication | `POST` | `/auth/register` | Guest |
| Authentication | `POST` | `/auth/login` | Guest |
| Authentication | `GET` | `/auth/me` | Customer/Admin |
| Products | `GET` | `/products` | Public |
| Products | `GET` | `/products/{id}` | Public |
| Categories | `GET` | `/categories` | Public |
| Categories | `GET` | `/categories/{id}` | Public |
| Cart | `GET` | `/cart` | Customer |
| Cart | `POST` | `/cart/items` | Customer |
| Cart | `PUT` | `/cart/items/{productId}` | Customer |
| Cart | `DELETE` | `/cart/items/{productId}` | Customer |
| Cart | `DELETE` | `/cart` | Customer |
| Checkout | `POST` | `/orders/checkout` | Customer |
| Customer Orders | `GET` | `/orders/my` | Customer |
| Customer Orders | `GET` | `/orders/{id}` | Customer/Admin |
| Customer Orders | `PATCH` | `/orders/{id}/cancel` | Customer |
| Admin Products | `POST` | `/admin/products` | Admin |
| Admin Products | `PUT` | `/admin/products/{id}` | Admin |
| Admin Products | `DELETE` | `/admin/products/{id}` | Admin |
| Admin Categories | `POST` | `/admin/categories` | Admin |
| Admin Categories | `PUT` | `/admin/categories/{id}` | Admin |
| Admin Categories | `DELETE` | `/admin/categories/{id}` | Admin |
| Admin Orders | `GET` | `/admin/orders` | Admin |
| Admin Orders | `GET` | `/admin/orders/{id}` | Admin |
| Admin Orders | `PATCH` | `/admin/orders/{id}/status` | Admin |

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
| `password` | String | Yes | Không rỗng, đáp ứng password policy |

### Success Response

**HTTP 201 Created**

```json
{
  "id": 1,
  "email": "customer@example.com",
  "role": "CUSTOMER",
  "createdAt": "2026-09-24T14:30:00"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Email/password không hợp lệ |
| `409` | Email đã tồn tại |

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
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "tokenType": "Bearer",
  "expiresIn": 3600,
  "user": {
    "id": 1,
    "email": "customer@example.com",
    "role": "CUSTOMER"
  }
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Request không hợp lệ |
| `401` | Email hoặc password không đúng |
| `403` | Tài khoản bị vô hiệu hóa nếu hệ thống hỗ trợ trạng thái tài khoản |

---

## 6.3. Get Current User

### Endpoint

```http
GET /auth/me
```

### Access

```text
Authenticated User
```

### Description

Lấy thông tin tài khoản hiện tại từ JWT.

### Request

Không có request body.

### Success Response

**HTTP 200 OK**

```json
{
  "id": 1,
  "email": "customer@example.com",
  "role": "CUSTOMER",
  "createdAt": "2026-09-24T14:30:00"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `401` | Thiếu hoặc JWT không hợp lệ |

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
| `keyword` | String | No | Tìm theo tên hoặc SKU |
| `categoryId` | Long | No | Lọc theo danh mục |
| `minPrice` | Decimal | No | Giá tối thiểu |
| `maxPrice` | Decimal | No | Giá tối đa |
| `sort` | String | No | `name`, `price`, `createdAt` |
| `direction` | String | No | `ASC` hoặc `DESC` |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang |

### Example Request

```http
GET /products?keyword=keyboard&categoryId=2&page=0&size=10
```

### Success Response

**HTTP 200 OK**

```json
{
  "content": [
    {
      "id": 1,
      "sku": "KB-001",
      "name": "Mechanical Keyboard",
      "description": "Mechanical keyboard for office use",
      "price": 850000.00,
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
```

### Business Rules

- Chỉ trả về sản phẩm có `status = ACTIVE` đối với public API.
- Không hiển thị sản phẩm đã bị vô hiệu hóa.
- `stockQuantity` không được âm.
- Không cho phép giá trị `size` quá lớn.

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

### Path Parameters

| Parameter | Type | Required | Description |
|---|---|---:|---|
| `id` | Long | Yes | Product ID |

### Success Response

**HTTP 200 OK**

```json
{
  "id": 1,
  "sku": "KB-001",
  "name": "Mechanical Keyboard",
  "description": "Mechanical keyboard for office use",
  "price": 850000.00,
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
```

### Error Cases

| Status | Condition |
|---|---|
| `404` | Không tìm thấy sản phẩm |
| `404` | Sản phẩm không hoạt động và không được phép hiển thị public |

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

Lấy danh sách danh mục sản phẩm đang hoạt động.

### Success Response

**HTTP 200 OK**

```json
[
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
```

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
  "id": 2,
  "name": "Accessories",
  "description": "Computer accessories",
  "status": "ACTIVE"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `404` | Không tìm thấy danh mục |

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
  "id": 1,
  "items": [
    {
      "id": 10,
      "product": {
        "id": 1,
        "sku": "KB-001",
        "name": "Mechanical Keyboard",
        "price": 850000.00,
        "stockQuantity": 20,
        "imageUrl": "https://example.com/images/keyboard.jpg",
        "status": "ACTIVE"
      },
      "quantity": 2,
      "subtotal": 1700000.00
    }
  ],
  "totalAmount": 1700000.00,
  "updatedAt": "2026-09-24T14:30:00"
}
```

### Business Rules

- Mỗi Customer chỉ có tối đa một Cart đang hoạt động.
- Cart được lưu trong Database.
- CartItem không được trùng Product trong cùng Cart.
- Guest không có Cart trong MVP.

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
  "productId": 1,
  "quantity": 2,
  "subtotal": 1700000.00,
  "message": "Product added to cart successfully"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Quantity không hợp lệ |
| `401` | Chưa đăng nhập |
| `404` | Không tìm thấy sản phẩm |
| `409` | Số lượng vượt quá tồn kho |
| `409` | Sản phẩm không hoạt động |

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
  "productId": 1,
  "quantity": 3,
  "subtotal": 2550000.00,
  "message": "Cart item updated successfully"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Quantity không hợp lệ |
| `404` | CartItem không tồn tại |
| `409` | Quantity vượt quá stock |

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

Xóa một sản phẩm khỏi Cart hiện tại.

### Success Response

**HTTP 204 No Content**

Không trả về response body.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `404` | CartItem không tồn tại |

---

## 9.5. Clear Cart

### Endpoint

```http
DELETE /cart
```

### Access

```text
CUSTOMER
```

### Description

Xóa toàn bộ CartItem trong Cart hiện tại.

### Success Response

**HTTP 204 No Content**

### Business Rules

- Chỉ xóa CartItem.
- Không xóa Cart record nếu không cần thiết.
- Không ảnh hưởng đến Order đã tạo trước đó.

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
3. Kiểm tra sản phẩm còn hoạt động.
4. Kiểm tra tồn kho.
5. Lấy thông tin giao hàng.
6. Tính tổng tiền.
7. Tạo Order.
8. Tạo OrderItem với snapshot thông tin sản phẩm.
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
  "id": 1001,
  "customerId": 1,
  "totalAmount": 1700000.00,
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
      "unitPrice": 850000.00,
      "quantity": 2,
      "itemTotal": 1700000.00
    }
  ],
  "createdAt": "2026-09-24T14:30:00"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Shipping information không hợp lệ |
| `401` | Chưa đăng nhập |
| `409` | Cart rỗng |
| `409` | Sản phẩm không còn hoạt động |
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
GET /orders/my
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
| `size` | Integer | No | Kích thước trang |
| `sort` | String | No | Trường sắp xếp |
| `direction` | String | No | `ASC` hoặc `DESC` |

### Success Response

**HTTP 200 OK**

```json
{
  "content": [
    {
      "id": 1001,
      "totalAmount": 1700000.00,
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
CUSTOMER hoặc ADMIN
```

### Description

Lấy thông tin chi tiết Order.

### Success Response

**HTTP 200 OK**

```json
{
  "id": 1001,
  "customerId": 1,
  "totalAmount": 1700000.00,
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
      "unitPrice": 850000.00,
      "quantity": 2,
      "itemTotal": 1700000.00
    }
  ],
  "createdAt": "2026-09-24T14:30:00",
  "updatedAt": "2026-09-24T14:30:00"
}
```

### Authorization Rules

- Customer chỉ được xem Order của bản thân.
- Admin được xem tất cả Order.
- Không trả về dữ liệu Order nếu người dùng không có quyền.

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
  "id": 1001,
  "orderStatus": "CANCELLED",
  "paymentStatus": "UNPAID",
  "paidAt": null,
  "message": "Order cancelled successfully"
}
```

### Business Rules

- Customer chỉ được hủy Order của chính mình.
- Chỉ Order có trạng thái `PENDING` mới được hủy.
- Khi hủy thành công, hệ thống hoàn lại stock đúng một lần.
- Không được hủy Order đã `CONFIRMED`, `SHIPPING` hoặc `DELIVERED`.
- Order đã `CANCELLED` không được hủy lại.

### Error Cases

| Status | Condition |
|---|---|
| `401` | Chưa đăng nhập |
| `403` | Không có quyền với Order |
| `404` | Không tìm thấy Order |
| `409` | Order không ở trạng thái PENDING |

---

# 12. Admin Product APIs

## 12.1. Create Product

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
  "id": 1,
  "sku": "KB-001",
  "name": "Mechanical Keyboard",
  "price": 850000.00,
  "stockQuantity": 20,
  "categoryId": 2,
  "status": "ACTIVE"
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

## 12.2. Update Product

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
  "id": 1,
  "sku": "KB-001",
  "name": "Mechanical Keyboard Updated",
  "price": 900000.00,
  "stockQuantity": 25,
  "categoryId": 2,
  "status": "ACTIVE",
  "message": "Product updated successfully"
}
```

### Business Rules

- Không cho phép SKU trùng với sản phẩm khác.
- Giá phải lớn hơn 0.
- Stock không được âm.
- Không thay đổi dữ liệu snapshot đã lưu trong OrderItem.
- Thay đổi giá Product không làm thay đổi `unitPrice` của OrderItem cũ.

---

## 12.3. Delete or Deactivate Product

### Endpoint

```http
DELETE /admin/products/{id}
```

### Access

```text
ADMIN
```

### Description

Vô hiệu hóa sản phẩm hoặc xóa sản phẩm nếu không vi phạm ràng buộc dữ liệu.

Để bảo toàn lịch sử Order, hệ thống nên ưu tiên chuyển:

```text
status = INACTIVE
```

thay vì xóa vật lý.

### Success Response

**HTTP 204 No Content**

### Business Rules

- Sản phẩm `INACTIVE` không xuất hiện trong public product list.
- Không xóa dữ liệu sản phẩm nếu đang được tham chiếu bởi lịch sử Order.
- Không làm mất thông tin snapshot trong OrderItem.

---

# 13. Admin Category APIs

## 13.1. Create Category

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
  "id": 2,
  "name": "Accessories",
  "description": "Computer accessories",
  "status": "ACTIVE"
}
```

### Error Cases

| Status | Condition |
|---|---|
| `400` | Dữ liệu không hợp lệ |
| `403` | Không phải Admin |
| `409` | Category name đã tồn tại |

---

## 13.2. Update Category

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
  "id": 2,
  "name": "Computer Accessories",
  "description": "Updated description",
  "status": "ACTIVE",
  "message": "Category updated successfully"
}
```

---

## 13.3. Delete or Deactivate Category

### Endpoint

```http
DELETE /admin/categories/{id}
```

### Access

```text
ADMIN
```

### Description

Xóa hoặc vô hiệu hóa Category.

### Business Rules

- Không xóa Category đang được Product tham chiếu.
- Nếu Category đang có Product, hệ thống phải từ chối xóa hoặc chuyển Category sang `INACTIVE`.
- Không làm mất dữ liệu Product hiện tại.

### Success Response

**HTTP 204 No Content**

### Error Cases

| Status | Condition |
|---|---|
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
| `status` | String | No | Lọc theo Order Status |
| `paymentStatus` | String | No | `UNPAID` hoặc `PAID` |
| `customerId` | Long | No | Lọc theo Customer |
| `page` | Integer | No | Trang |
| `size` | Integer | No | Kích thước trang |
| `sort` | String | No | Trường sắp xếp |
| `direction` | String | No | `ASC` hoặc `DESC` |

### Example Request

```http
GET /admin/orders?status=PENDING&page=0&size=10
```

### Success Response

**HTTP 200 OK**

```json
{
  "content": [
    {
      "id": 1001,
      "customerId": 1,
      "totalAmount": 1700000.00,
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
```

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

Response có cùng cấu trúc với:

```http
GET /orders/{id}
```

Admin có thể xem:

- Customer ID.
- Thông tin giao hàng.
- Order Items.
- Tổng tiền.
- Order Status.
- Payment Status.
- Paid At.
- Thời gian tạo và cập nhật.

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
  "id": 1001,
  "orderStatus": "CONFIRMED",
  "paymentStatus": "UNPAID",
  "paidAt": null,
  "message": "Order status updated successfully"
}
```

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
```

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

### Error Cases

| Status | Condition |
|---|---|
| `400` | Status không hợp lệ |
| `403` | Không phải Admin |
| `404` | Không tìm thấy Order |
| `409` | Chuyển trạng thái không hợp lệ |

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
```

## 16.2. Category

```text
Name không được rỗng
Name phải unique
Status chỉ nhận ACTIVE hoặc INACTIVE
Không xóa Category đang được Product tham chiếu
```

## 16.3. Cart

```text
Quantity > 0
Quantity không vượt quá stock
Mỗi Product chỉ xuất hiện một lần trong Cart
Guest không được sử dụng Cart
```

## 16.4. Order

```text
Cart không được rỗng khi Checkout
Product phải ACTIVE tại thời điểm Checkout
Stock phải đủ
Tổng tiền được tính tại Backend
OrderItem lưu snapshot thông tin sản phẩm
Customer chỉ xem/hủy Order của chính mình
```

## 16.5. Payment

```text
Payment Status chỉ nhận UNPAID hoặc PAID
paidAt có thể NULL
Khi Order = DELIVERED:
    paymentStatus = PAID
    paidAt = current timestamp
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

Các nghiệp vụ sau cần được xử lý trong transaction:

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

## 18.2. Cancel Order

```text
Validate Order Status
Restore Stock
Update Order Status
Commit
```

Không được hoàn stock nhiều lần nếu cùng một Order bị xử lý lại.

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
- [ ] Login thành công.
- [ ] Login sai password bị từ chối.
- [ ] API yêu cầu authentication từ chối request không có JWT.

## Products

- [ ] Guest xem được Product List.
- [ ] Guest tìm kiếm được sản phẩm.
- [ ] Guest lọc được theo Category.
- [ ] Không hiển thị Product INACTIVE.
- [ ] Admin tạo Product thành công.
- [ ] Không cho phép SKU trùng.
- [ ] Không cho phép Price `<= 0`.
- [ ] Không cho phép Stock âm.

## Cart

- [ ] Guest không truy cập được Cart.
- [ ] Customer thêm Product vào Cart.
- [ ] Không thêm Product INACTIVE.
- [ ] Không thêm số lượng vượt stock.
- [ ] Không tạo CartItem trùng Product.
- [ ] Customer cập nhật quantity.
- [ ] Customer xóa CartItem.
- [ ] Customer clear Cart.

## Checkout

- [ ] Không checkout khi Cart rỗng.
- [ ] Không checkout khi stock không đủ.
- [ ] Không checkout Product INACTIVE.
- [ ] Tổng tiền được tính đúng.
- [ ] OrderItem lưu đúng snapshot.
- [ ] Stock được trừ đúng.
- [ ] Cart được clear sau Checkout thành công.
- [ ] Transaction rollback khi có lỗi.

## Orders

- [ ] Customer xem được Order của mình.
- [ ] Customer không xem được Order của Customer khác.
- [ ] Customer chỉ hủy được Order PENDING.
- [ ] Hủy Order hoàn stock đúng một lần.
- [ ] Admin xem được tất cả Order.
- [ ] Không cho phép chuyển Order Status sai quy trình.
- [ ] Khi Order DELIVERED, paymentStatus tự chuyển PAID.
- [ ] Khi Order DELIVERED, paidAt được gán timestamp.
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

Mọi thay đổi API làm ảnh hưởng đến requirement hoặc data model phải được cập nhật đồng bộ trong:

```text
docs/01-requirements/
docs/02-design/
docs/03-api/
```
