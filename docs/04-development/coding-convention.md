# Coding Convention

## 1. Mục đích

Tài liệu này quy định các quy ước viết code cho dự án **Hệ thống Quản lý Bán hàng Mini**.

Mục tiêu:

- Code dễ đọc và dễ bảo trì.
- Giảm sự khác biệt giữa các thành viên.
- Giúp review code dễ dàng.
- Hạn chế lỗi do naming hoặc cấu trúc không nhất quán.
- Giữ code phù hợp với quy mô MVP.

---

## 2. Nguyên tắc chung

Code phải ưu tiên:

```text
Readable
Simple
Consistent
Maintainable
Testable
```

Không viết code phức tạp khi bài toán chưa yêu cầu.

Ưu tiên:

```text
Simple solution
```

thay vì:

```text
Over-engineered solution
```

---

# 3. Backend Convention

## 3.1. Ngôn ngữ

Backend sử dụng:

```text
Java
Spring Boot
Spring Data JPA
Spring Security
```

---

## 3.2. Package Convention

Backend nên tổ chức theo module/layer rõ ràng.

Ví dụ:

```text
backend/src/main/java/.../

├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── mapper/
├── exception/
├── security/
└── config/
```

Không đặt toàn bộ class vào một package duy nhất.

---

## 3.3. Naming Convention

### Class

Sử dụng `PascalCase`.

Đúng:

```java
ProductService
OrderController
CartRepository
```

Sai:

```java
productService
order_controller
cartrepository
```

### Method

Sử dụng `camelCase`.

Ví dụ:

```java
getProductById()
createOrder()
calculateTotal()
updateStock()
```

### Variable

Sử dụng `camelCase`.

Ví dụ:

```java
productId
stockQuantity
totalAmount
customerEmail
```

### Constant

Sử dụng `UPPER_SNAKE_CASE`.

Ví dụ:

```java
MAX_CART_ITEM
DEFAULT_PAGE_SIZE
```

---

## 3.4. Entity Naming

Tên Entity sử dụng số ít:

```text
User
Category
Product
Cart
CartItem
Order
OrderItem
```

Tên bảng Database có thể sử dụng `snake_case`:

```text
users
categories
products
carts
cart_items
orders
order_items
```

---

## 3.5. DTO Convention

Không sử dụng Entity trực tiếp làm request/response API nếu không cần thiết.

Ví dụ:

```text
ProductCreateRequest
ProductUpdateRequest
ProductResponse
OrderResponse
```

Tên DTO phải thể hiện mục đích.

Không nên tạo class chung chung như:

```text
DataDTO
CommonDTO
RequestDTO
```

nếu không thể hiện rõ chức năng.

---

## 3.6. Controller Convention

Controller chịu trách nhiệm:

- Nhận HTTP request.
- Validate request cơ bản.
- Gọi Service.
- Trả HTTP response.

Không đặt business logic phức tạp trong Controller.

Không nên:

```java
@PostMapping
public OrderResponse createOrder(...) {

    // kiểm tra stock
    // tính total
    // tạo order
    // clear cart
    // ...
}
```

Nên:

```java
@PostMapping
public OrderResponse createOrder(...) {
    return orderService.createOrder(...);
}
```

Business logic thuộc Service.

---

## 3.7. Service Convention

Service chịu trách nhiệm xử lý business logic.

Ví dụ:

```java
createOrder()
cancelOrder()
confirmOrder()
updateProductStock()
calculateOrderTotal()
```

Các logic quan trọng như Checkout phải được xử lý transaction.

Ví dụ:

```java
@Transactional
public Order createOrder(...) {
    ...
}
```

---

## 3.8. Repository Convention

Repository chỉ chịu trách nhiệm truy cập dữ liệu.

Ví dụ:

```java
ProductRepository
OrderRepository
CartRepository
```

Không đặt business logic vào Repository.

---

## 3.9. Exception Handling

Không trả stack trace trực tiếp cho client.

Nên sử dụng exception có ý nghĩa:

```text
ProductNotFoundException
OrderNotFoundException
InsufficientStockException
InvalidOrderStatusException
```

API nên trả response thống nhất.

Ví dụ:

```json
{
  "timestamp": "2026-09-24T10:00:00",
  "status": 400,
  "message": "Insufficient stock",
  "path": "/api/orders"
}
```

---

## 3.10. Validation

Input phải được validate tại Backend.

Ví dụ Product:

```text
SKU
Name
Price
Stock Quantity
Category
Status
```

Các quy tắc chính:

```text
Price > 0
Stock Quantity >= 0
SKU không trùng
Name không rỗng
```

Không chỉ dựa vào validation ở Frontend.

---

## 3.11. Security Convention

Authentication sử dụng:

```text
Email + Password
JWT
```

Authorization dựa trên:

```text
CUSTOMER
ADMIN
```

Không tạo class con:

```text
Customer extends User
Admin extends User
```

nếu không có yêu cầu nghiệp vụ đặc biệt.

Thay vào đó:

```java
user.getRole()
```

để kiểm tra quyền.

Ví dụ:

```text
CUSTOMER
ADMIN
```

---

## 3.12. Password

Không lưu password dạng plain text.

Sai:

```text
password = "123456"
```

Phải lưu password đã được hash bằng cơ chế bảo mật phù hợp.

---

## 3.13. Transaction

Các nghiệp vụ thay đổi nhiều dữ liệu phải đảm bảo tính nguyên tử.

Đặc biệt:

```text
Checkout
Cancel Order
```

Checkout phải đảm bảo:

```text
Validate Cart
↓
Validate Product
↓
Validate Stock
↓
Create Order
↓
Create Order Items
↓
Deduct Stock
↓
Clear Cart
```

Nếu một bước thất bại, transaction phải rollback.

---

# 4. Frontend Convention

## 4.1. Ngôn ngữ

Frontend sử dụng:

```text
ReactJS
JavaScript
```

hoặc TypeScript nếu project được thiết lập theo TypeScript.

Không trộn JavaScript và TypeScript tùy tiện trong cùng một module.

---

## 4.2. Component Naming

Component sử dụng `PascalCase`.

Ví dụ:

```text
ProductList
ProductCard
ProductDetail
CartItem
OrderDetail
AdminProductForm
```

File tương ứng:

```text
ProductList.jsx
ProductCard.jsx
ProductDetail.jsx
```

---

## 4.3. Function Naming

Function sử dụng `camelCase`.

Ví dụ:

```javascript
getProducts()
getProductDetail()
addToCart()
updateCartItem()
placeOrder()
cancelOrder()
```

---

## 4.4. Event Handler

Tên event handler nên bắt đầu bằng:

```text
handle
```

Ví dụ:

```javascript
handleSubmit()
handleDelete()
handleQuantityChange()
handleLogin()
```

---

## 4.5. Component Responsibility

Mỗi component nên có một trách nhiệm rõ ràng.

Không tạo một component chứa toàn bộ:

```text
API call
business logic
form
table
modal
navigation
```

trong cùng một file nếu module đã lớn.

Nên tách thành các component nhỏ hơn khi cần.

---

## 4.6. API Call

Không gọi API trực tiếp ở quá nhiều component theo nhiều cách khác nhau.

Nên có một tầng API/service.

Ví dụ:

```text
api/
├── authApi.js
├── productApi.js
├── cartApi.js
└── orderApi.js
```

Component:

```text
UI
 ↓
API Service
 ↓
Backend
```

---

## 4.7. State Management

Chỉ đưa dữ liệu vào global state khi thực sự cần.

Ví dụ dữ liệu có thể dùng global state:

```text
Current User
Authentication State
Cart State
```

Dữ liệu chỉ dùng trong một component nên giữ local state.

Không đưa mọi state vào global state.

---

## 4.8. Loading và Error State

API call nên xử lý ít nhất:

```text
Loading
Success
Error
```

Ví dụ:

```javascript
const [loading, setLoading] = useState(false);
const [error, setError] = useState(null);
```

Không để UI đứng im mà không có feedback khi API đang xử lý.

---

# 5. Database Convention

## 5.1. Table Naming

Tên bảng dùng `snake_case` và số nhiều:

```text
users
categories
products
carts
cart_items
orders
order_items
```

---

## 5.2. Column Naming

Dùng `snake_case`.

Ví dụ:

```text
customer_id
stock_quantity
total_amount
created_at
updated_at
order_status
payment_status
```

---

## 5.3. Primary Key

Mỗi bảng có một Primary Key.

Convention:

```text
id
```

Ví dụ:

```text
users.id
products.id
orders.id
```

---

## 5.4. Foreign Key

Tên Foreign Key:

```text
<entity>_id
```

Ví dụ:

```text
customer_id
category_id
cart_id
product_id
order_id
```

---

## 5.5. Status

Các status phải có giá trị rõ ràng và giới hạn.

Ví dụ:

```text
ProductStatus:
ACTIVE
INACTIVE
```

```text
OrderStatus:
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

```text
PaymentStatus:
UNPAID
PAID
```

Không dùng các string tùy ý như:

```text
"done"
"finish"
"completed"
"ok"
```

---

# 6. Comment Convention

Chỉ comment khi comment giúp giải thích:

- business rule,
- lý do của một đoạn code,
- workaround,
- logic khó hiểu.

Không comment những đoạn code hiển nhiên.

Không nên:

```java
// Tăng i lên 1
i++;
```

Nên:

```java
// Only PENDING orders can be cancelled.
if (order.isPending()) {
    ...
}
```

---

# 7. Logging Convention

Log phải phục vụ debugging và monitoring.

Nên log:

```text
Request quan trọng
Business event
Error
Exception
Unexpected state
```

Không log:

```text
Password
JWT secret
API key
Thông tin nhạy cảm
```

Không dùng `System.out.println()` làm logging chính trong Backend.

---

# 8. Code Quality

Trước khi commit:

```text
Code phải compile.
Không có lỗi.
Không có warning quan trọng.
Không để code debug tạm thời.
Không để TODO không cần thiết.
```

Đặc biệt không commit:

```javascript
console.log("test");
```

nếu không còn cần thiết.

---

# 9. Testing Convention

Business logic quan trọng phải có test phù hợp.

Ưu tiên test:

```text
Authentication
Product validation
Cart quantity
Checkout
Stock deduction
Order total
Cancel Order
Order status transition
```

Ví dụ:

```text
Checkout khi cart rỗng
Checkout khi stock không đủ
Checkout thành công
Cancel order PENDING
Không cancel order SHIPPING
```

---

# 10. Simplicity Rule

Project là MVP nên không thêm abstraction chỉ để "đẹp kiến trúc".

Trước khi thêm:

```text
New Service
New Interface
New Design Pattern
New Entity
New Library
```

phải xác định rằng nó giải quyết một vấn đề thực tế của project.

Không thêm:

```text
Payment Entity
Payment Gateway
Coupon Engine
Multi-address
Multi-image
Microservice
Redis
Kafka
```

khi các tính năng đó chưa thuộc phạm vi MVP.

---

# 11. Quy tắc Review Code

Reviewer cần kiểm tra:

- Code có đúng requirement không?
- Business rule có đúng không?
- Naming có thống nhất không?
- Có validation chưa?
- Có xử lý error chưa?
- Có ảnh hưởng transaction không?
- Có tạo duplicate logic không?
- Có hard-code dữ liệu nhạy cảm không?
- Có test cho logic quan trọng không?

---

# 12. Checklist trước khi hoàn thành một tính năng

```text
[ ] Đúng requirement
[ ] Đúng design
[ ] Naming đúng convention
[ ] Validation đầy đủ
[ ] Error handling
[ ] Transaction nếu cần
[ ] Không hard-code secret
[ ] Test logic quan trọng
[ ] Không còn debug code
[ ] Code dễ đọc
```
