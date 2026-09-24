# Requirement Specification

## 1. Information

| Item             | Description                            |
| ---------------- | -------------------------------------- |
| Project          | Mini E-commerce / Inventory Management |
| Company          | A-Software                             |
| Status           | Draft for Design                       |

---

# 2. System Overview

## 2.1. Purpose

Hệ thống **Mini E-commerce / Inventory Management** là một ứng dụng thương mại điện tử quy mô nhỏ, cho phép khách hàng xem sản phẩm, quản lý giỏ hàng và đặt hàng; đồng thời cung cấp cho Admin các chức năng quản lý sản phẩm, danh mục, tồn kho và đơn hàng.

Hệ thống được phát triển theo mô hình:

- **Backend:** Java Spring Boot
- **Frontend:** ReactJS
- **Database:** MySQL

Mục tiêu của hệ thống trong phạm vi MVP là hoàn thiện các luồng nghiệp vụ chính từ đầu đến cuối:

```text
Browse Product
      ↓
Search / Filter Product
      ↓
Login / Register
      ↓
Add to Cart
      ↓
Checkout
      ↓
Create Order
      ↓
Update Inventory
      ↓
Track Order
```

---

# 3. Actors

Hệ thống có 3 nhóm actor chính.

| Actor    | Description                                                                             |
| -------- | --------------------------------------------------------------------------------------- |
| Guest    | Người dùng chưa đăng nhập. Chỉ có thể xem sản phẩm trên trình duyệt.                        |
| Customer | Người dùng đã đăng nhập và có quyền mua hàng, quản lý giỏ hàng và xem lịch sử đơn hàng. |
| Admin    | Người quản trị hệ thống, có quyền quản lý Product, Category, Inventory và Order.        |

## 3.1. Actor Permission Summary

| Function                 | Guest | Customer | Admin |
| ------------------------ | :---: | :------: | :---: |
| View Product             |   ✓   |     ✓    |   ✓   |
| Search Product           |   ✓   |     ✓    |   ✓   |
| Filter / Sort Product    |   ✓   |     ✓    |   ✓   |
| View Product Detail      |   ✓   |     ✓    |   ✓   |
| Add to Cart              |   -   |     ✓    |   -   |
| Manage Personal Cart     |   -   |     ✓    |   -   |
| Register                 |   ✓   |     -    |   -   |
| Login                    |   ✓   |     ✓    |   ✓   |
| Checkout                 |   -   |     ✓    |   -   |
| Place Order              |   -   |     ✓    |   -   |
| View Own Orders          |   -   |     ✓    |   -   |
| Cancel Own Pending Order |   -   |     ✓    |   -   |
| Manage Products          |   -   |     -    |   ✓   |
| Manage Categories        |   -   |     -    |   ✓   |
| Manage Inventory         |   -   |     -    |   ✓   |
| View All Orders          |   -   |     -    |   ✓   |
| Update Order Status      |   -   |     -    |   ✓   |

---

# 4. Functional Requirements

Functional requirements được đánh mã theo nhóm để dễ trace sang Use Case, API và Test Case.

---

## FR-AUTH — Authentication & Account

### FR-AUTH-01 — Customer Registration

Hệ thống phải cho phép Guest đăng ký tài khoản Customer.

Thông tin đăng ký tối thiểu:

- Email
- Password
- Các thông tin bắt buộc khác nếu được xác định trong UI

Rules:

- Email phải đúng định dạng.
- Email phải là duy nhất.
- Password không được lưu dưới dạng plain text.
- Password phải được hash bằng cơ chế bảo mật như BCrypt hoặc Argon2.

---

### FR-AUTH-02 — Customer Login

Hệ thống phải cho phép Customer đăng nhập bằng:

- Email
- Password

Khi đăng nhập thành công:

- Backend xác thực thông tin tài khoản.
- Hệ thống cấp JWT.
- Frontend sử dụng JWT cho các API yêu cầu authentication.

---

### FR-AUTH-03 — Admin Login

Admin có thể đăng nhập bằng Email và Password.

Admin không được đăng ký thông qua public registration form.

---

### FR-AUTH-04 — Admin Account Initialization

Trong MVP, hệ thống phải tạo một tài khoản Admin mặc định thông qua Database Seed/Migration khi khởi tạo database.

Thông tin credential mặc định không được hard-code dưới dạng secret công khai trong source code.

---

### FR-AUTH-05 — Role-Based Authorization

Hệ thống phải phân quyền dựa trên role.

Các role chính:

```text
CUSTOMER
ADMIN
```

Backend phải kiểm tra role đối với các API yêu cầu quyền Admin.

Customer không được phép truy cập các Admin API.

---

### FR-AUTH-06 — Stateless Authentication

Backend phải sử dụng JWT-based stateless authentication.

Server không phụ thuộc vào session authentication để duy trì trạng thái đăng nhập.

---

## FR-PROD — Product Management

### FR-PROD-01 — View Product List

Hệ thống phải cho phép Guest và Customer xem danh sách Product.

Danh sách phải hiển thị tối thiểu:

- Product Name
- Price
- Primary Image
- Category
- Availability/Stock Status

---

### FR-PROD-02 — View Product Detail

Hệ thống phải cho phép người dùng xem thông tin chi tiết của Product.

Thông tin tối thiểu:

- Product ID
- SKU
- Product Name
- Description
- Price
- Stock
- Image
- Category
- Status

---

### FR-PROD-03 — Product Data

Mỗi Product phải có các thông tin:

| Field          | Requirement       |
| -------------- | ----------------- |
| Product ID     | Required, unique  |
| SKU            | Required, unique  |
| Product Name   | Required          |
| Description    | Optional          |
| Price          | Required, > 0     |
| Stock Quantity | Required, >= 0    |
| Image URL      | Primary image     |
| Category       | Required          |
| Status         | Active / Inactive |
| Created Date   | Required          |
| Updated Date   | Required          |

---

### FR-PROD-04 — Product Price Validation

Hệ thống phải từ chối Product có:

```text
Price <= 0
```

---

### FR-PROD-05 — Product SKU

Mỗi Product phải có một SKU duy nhất.

SKU có thể:

- Được Admin nhập.
- Hoặc được hệ thống tự động tạo.

Không được phép tồn tại hai Product có cùng SKU.

---

### FR-PROD-06 — Product Status

Product phải hỗ trợ ít nhất hai trạng thái:

```text
ACTIVE
INACTIVE
```

Product `INACTIVE` không được phép được mua.

---

### FR-PROD-07 — Product Soft Delete

Khi Admin xóa Product, hệ thống không được hard-delete Product nếu việc xóa có thể ảnh hưởng đến dữ liệu Order lịch sử.

Hệ thống phải sử dụng Soft Delete, ví dụ:

```text
Status = INACTIVE
```

hoặc cơ chế `deleted_at`.

---

### FR-PROD-08 — Out-of-Stock

Khi:

```text
stock_quantity = 0
```

Product phải được hiển thị là hết hàng.

Customer không được:

- Add Product vào Cart.
- Buy Now.
- Checkout Product đó.

---

### FR-PROD-09 — Product Filter

Hệ thống phải hỗ trợ filter Product theo:

- Category
- Price

---

### FR-PROD-10 — Product Sorting

Hệ thống phải hỗ trợ sort Product theo:

- Price
- Newest
- Name

---

### FR-PROD-11 — Product Search

Hệ thống phải hỗ trợ tìm kiếm Product theo:

- Product Name
- SKU
- Description

Search không phân biệt chữ hoa/chữ thường.

---

### FR-PROD-12 — Product Pagination

Product List phải hỗ trợ offset-based pagination.

Page size mặc định nằm trong khoảng:

```text
10–12 products/page
```

Giá trị cụ thể sẽ được thống nhất khi thiết kế API/UI.

---

## FR-CAT — Category Management

### FR-CAT-01 — Category Data

Category phải có:

- Category ID
- Category Name
- Description
- Status

---

### FR-CAT-02 — Product-Category Relationship

Mỗi Product phải thuộc chính xác một Category.

Relationship:

```text
Category 1 ─────── N Product
```

Product lưu Category foreign key.

---

### FR-CAT-03 — Flat Category Structure

Hệ thống không hỗ trợ Category cha/con.

Category được tổ chức dưới dạng flat list.

---

### FR-CAT-04 — Admin Create Category

Admin phải có thể tạo Category mới.

---

### FR-CAT-05 — Admin Update Category

Admin phải có thể cập nhật thông tin Category.

---

### FR-CAT-06 — Admin Delete Category

Admin có thể xóa Category nếu Category không còn Product nào tham chiếu.

Nếu Category vẫn có Product:

```text
Delete = Blocked
```

Hệ thống phải trả về lỗi phù hợp.

---

## FR-CART — Shopping Cart

### FR-CART-01 — Customer Cart

Sau khi Customer đăng nhập, Cart của Customer được lưu trong Database.

---

### FR-CART-02 — Add Product to Cart

Customer có thể thêm Product còn hàng vào Cart.

Điều kiện:

```text
Product.status = ACTIVE
AND
Product.stock_quantity > 0
```

---

### FR-CART-03 — Duplicate Product in Cart

Nếu Product đã tồn tại trong Cart, hệ thống không tạo CartItem mới.

Thay vào đó:

```text
existing_quantity += requested_quantity
```

---

### FR-CART-04 — CartItem Uniqueness

Database phải đảm bảo mỗi Cart chỉ có tối đa một CartItem cho cùng một Product.

Constraint:

```text
UNIQUE(cart_id, product_id)
```

---

### FR-CART-05 — Cart Quantity Validation

Quantity trong Cart không được vượt quá Stock hiện tại.

Nếu:

```text
cart_quantity > stock_quantity
```

hệ thống phải từ chối thao tác và hiển thị lỗi.

---

### FR-CART-06 — Update Cart Quantity

Customer phải có thể tăng hoặc giảm quantity của CartItem.

Hệ thống phải validate lại Stock sau mỗi thay đổi.

---

### FR-CART-07 — Remove CartItem

Customer phải có thể xóa Product khỏi Cart.

---

### FR-CART-08 — Unavailable Cart Item

Nếu Product trong Cart:

- bị INACTIVE; hoặc
- hết Stock,

UI phải hiển thị trạng thái `Không khả dụng`.

Customer không được Checkout khi Cart còn Product không khả dụng.

---

## FR-CHECKOUT — Checkout

### FR-CHECKOUT-01 — Checkout Authentication

Chỉ Customer đã đăng nhập mới được Checkout.

Guest phải đăng nhập trước khi hoàn tất Order.

---

### FR-CHECKOUT-02 — Shipping Information

Customer phải cung cấp:

- Full Name
- Phone Number
- Address
- Province/City
- District/Ward

Thông tin được nhập trực tiếp trong Checkout.

---

### FR-CHECKOUT-03 — Shipping Method

MVP sử dụng một phương thức giao hàng mặc định:

```text
Standard Delivery
```

Customer không cần lựa chọn Shipping Method.

---

### FR-CHECKOUT-04 — Checkout Validation

Trước khi tạo Order, hệ thống phải kiểm tra:

- Customer đã authenticated.
- Cart không rỗng.
- Product còn Active.
- Product còn đủ Stock.
- Quantity của từng CartItem hợp lệ.
- Shipping information hợp lệ.
- Product price hợp lệ.

Nếu một điều kiện không thỏa mãn, Checkout phải bị từ chối.

---

### FR-CHECKOUT-05 — Order Total Calculation

Hệ thống phải tính Total Amount dựa trên các OrderItem.

Công thức:

```text
Item Total = Product Price × Quantity

Order Total = Sum(Item Total)
```

Giá sử dụng trong Order phải là giá tại thời điểm Checkout.

---

### FR-CHECKOUT-06 — Stock Validation

Backend phải kiểm tra Stock tại thời điểm Checkout.

Không được chỉ dựa vào validation ở Frontend.

---

### FR-CHECKOUT-07 — Stock Deduction

Khi Checkout thành công, hệ thống phải giảm Stock tương ứng với quantity của các OrderItem.

Ví dụ:

```text
Stock trước Checkout = 10
Order Quantity       = 3
Stock sau Checkout   = 7
```

---

### FR-CHECKOUT-08 — Transactional Checkout

Các thao tác quan trọng trong Checkout phải được xử lý trong một database transaction để tránh trường hợp:

- Order được tạo nhưng Stock không giảm.
- Stock giảm nhưng Order không được tạo.
- OrderItem được tạo không đầy đủ.

Nếu Checkout thất bại, transaction phải rollback các thay đổi liên quan.

---

### FR-CHECKOUT-09 — Stock Concurrency

Backend phải kiểm tra Stock một cách an toàn tại thời điểm cập nhật để hạn chế tình trạng overselling khi có nhiều request Checkout đồng thời.

---

### FR-CHECKOUT-10 — Coupon

MVP không hỗ trợ:

- Coupon
- Promotion
- Discount Code

---

## FR-ORDER — Order Management

### FR-ORDER-01 — Create Order

Customer phải có thể tạo Order sau khi Checkout hợp lệ.

Order mới được tạo với:

```text
Order Status = PENDING
```

---

### FR-ORDER-02 — Order Data

Order phải lưu:

- Order ID
- Customer
- Order Items
- Total Amount
- Shipping Information
- Order Status
- Payment Status
- Created Date
- Updated Date

Payment Status chỉ được sử dụng để biểu diễn trạng thái thanh toán của Order:

- `UNPAID`
- `PAID`

MVP không triển khai Payment Entity riêng, Payment Gateway hoặc các phương thức thanh toán trực tuyến.

---

### FR-ORDER-03 — OrderItem Data

Mỗi OrderItem phải lưu tối thiểu:

- Order ID
- Product ID/reference
- Quantity
- Price at purchase time
- Item total

---

### FR-ORDER-04 — Historical Product Price

OrderItem phải lưu Product Price tại thời điểm mua.

Ví dụ:

```text
Product current price = 150,000
OrderItem price       = 120,000
```

Sau khi Order được tạo, việc Admin thay đổi Product Price không được làm thay đổi giá của OrderItem cũ.

---

### FR-ORDER-05 — Order Status

Order hỗ trợ các trạng thái:

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

---

### FR-ORDER-06 — Order Status Transition

Order phải tuân theo luồng trạng thái hợp lệ.

Luồng cơ bản:

```text
PENDING
   ↓
CONFIRMED
   ↓
SHIPPING
   ↓
DELIVERED
```

Ngoài ra:

```text
PENDING
   ↓
CANCELLED
```

Customer chỉ có thể Cancel Order ở trạng thái `PENDING`.

---

### FR-ORDER-07 — Customer Cancel Order

Customer được phép Cancel Order khi:

```text
Order Status = PENDING
```

Khi Cancel thành công:

```text
Order Status = CANCELLED
```

và Stock phải được hoàn lại tương ứng.

---

### FR-ORDER-08 — Admin Update Order Status

Admin có thể cập nhật Order Status theo các transition hợp lệ.

Admin không được tùy ý chuyển Order sang một trạng thái không hợp lệ.

---

### FR-ORDER-09 — Admin Cannot Modify Order Content

Sau khi Order được tạo, Admin không được chỉnh sửa:

- Product
- Quantity
- OrderItem Price
- Shipping Information

Admin chỉ được thay đổi Order Status theo quyền được cấp.

---

### FR-ORDER-10 — Inventory Restoration

Khi Order chuyển sang `CANCELLED`, hệ thống phải hoàn lại Stock tương ứng.

Ví dụ:

```text
Stock trước Order = 10
Order Quantity    = 3
Stock sau Order   = 7

Order Cancel
        ↓
Stock = 10
```

Việc hoàn Stock chỉ được thực hiện một lần cho mỗi Order.

---

### FR-ORDER-11 — Customer Order History

Customer phải có thể xem danh sách các Order của chính mình.

API dự kiến:

```http
GET /api/orders/my-orders
```

---

### FR-ORDER-12 — Customer Order Detail

Customer phải có thể xem chi tiết Order của chính mình.

API dự kiến:

```http
GET /api/orders/{id}
```

Backend phải kiểm tra ownership trước khi trả dữ liệu.

Customer không được xem Order của Customer khác.

---

### FR-ORDER-13 — Order History Filter

Customer có thể filter Order theo status.

Có thể xem các Order:

- Pending
- Confirmed
- Shipping
- Delivered
- Cancelled

---

## FR-ADMIN-PROD — Admin Product Management

### FR-ADMIN-PROD-01 — View Products

Admin phải có thể xem danh sách Product trong hệ thống.

---

### FR-ADMIN-PROD-02 — Search Products

Admin phải có thể tìm kiếm Product.

Search có thể sử dụng:

- Product Name
- SKU
- Description

---

### FR-ADMIN-PROD-03 — Create Product

Admin phải có thể tạo Product mới.

Hệ thống phải validate:

- Product Name
- SKU
- Price
- Stock
- Category
- Status

---

### FR-ADMIN-PROD-04 — Update Product

Admin phải có thể cập nhật Product.

Có thể cập nhật:

- Product Name
- Description
- Price
- SKU theo rule uniqueness
- Image
- Category
- Stock
- Status

---

### FR-ADMIN-PROD-05 — Manage Inventory

Admin phải có thể cập nhật `stock_quantity` thông qua Product Management.

---

### FR-ADMIN-PROD-06 — Activate / Deactivate Product

Admin phải có thể:

```text
ACTIVE → INACTIVE
INACTIVE → ACTIVE
```

Product INACTIVE không được phép đặt hàng.

---

### FR-ADMIN-PROD-07 — Soft Delete Product

Admin có thể thực hiện Delete Product.

Delete phải được xử lý dưới dạng Soft Delete.

---

## FR-ADMIN-CAT — Admin Category Management

### FR-ADMIN-CAT-01 — View Categories

Admin phải có thể xem danh sách Category.

---

### FR-ADMIN-CAT-02 — Create Category

Admin phải có thể tạo Category.

---

### FR-ADMIN-CAT-03 — Update Category

Admin phải có thể cập nhật Category.

---

### FR-ADMIN-CAT-04 — Delete Category

Admin có thể Delete Category nếu Category không có Product tham chiếu.

Nếu còn Product:

```text
Delete = Rejected
```

---

## FR-ADMIN-ORDER — Admin Order Management

### FR-ADMIN-ORDER-01 — View All Orders

Admin phải có thể xem tất cả Order.

API dự kiến:

```http
GET /api/admin/orders
```

---

### FR-ADMIN-ORDER-02 — Search Orders

Admin phải có thể tìm kiếm Order theo:

- Order ID
- Customer Name
- Customer Email
- Customer Phone

---

### FR-ADMIN-ORDER-03 — Filter Orders

Admin phải có thể filter Order theo Order Status.

---

### FR-ADMIN-ORDER-04 — View Order Detail

Admin phải có thể xem:

- Order information
- Customer/recipient information
- Order Items
- Quantity
- Product price at purchase
- Total Amount
- Shipping information
- Payment Status
- Order Status

---

### FR-ADMIN-ORDER-05 — Update Order Status

Admin phải có thể cập nhật Order Status theo transition được phép.

---

# 5. User Interface Requirements

## FR-UI-01 — Responsive Design

Frontend phải hỗ trợ Responsive UI cho:

- Desktop
- Tablet
- Mobile

---

## FR-UI-02 — Product List

Product List phải hỗ trợ:

- Product display
- Search
- Filter
- Sort
- Pagination

---

## FR-UI-03 — Product Detail

Product Detail phải hiển thị đầy đủ thông tin cần thiết và cung cấp chức năng Add to Cart khi Product khả dụng.

---

## FR-UI-04 — Shopping Cart

Cart UI phải hiển thị:

- Product
- Price
- Quantity
- Item Total
- Cart Total
- Availability
- Remove action

---

## FR-UI-05 — Checkout Page

Checkout UI phải cho phép Customer:

- Nhập Shipping Information.
- Kiểm tra lại thông tin Order.
- Xác nhận đặt hàng.

MVP không yêu cầu Customer lựa chọn Payment Method.

---

## FR-UI-06 — Order History

Customer Order History phải hiển thị:

- Order ID
- Date
- Total
- Status
- View Detail

---

## FR-UI-07 — Admin Dashboard

Admin UI phải cung cấp các khu vực quản lý:

```text
Product Management
Category Management
Order Management
Inventory Management
```

---

## FR-UI-08 — Loading State

Các màn hình có thao tác API phải có trạng thái Loading phù hợp.

---

## FR-UI-09 — Empty State

Các danh sách rỗng phải có Empty State.

Ví dụ:

```text
Your cart is empty.
No products found.
No orders found.
```

---

## FR-UI-10 — Error State

Frontend phải hiển thị Error State khi API hoặc thao tác nghiệp vụ thất bại.

---

## FR-UI-11 — Notification

Các thao tác thành công/thất bại phải cung cấp thông báo bằng Toast hoặc Alert.

---

# 6. API & Error Handling Requirements

## FR-API-01 — REST API

Backend phải cung cấp RESTful API cho các nghiệp vụ chính.

API phải được tổ chức theo resource:

```text
/api/auth
/api/products
/api/categories
/api/cart
/api/orders
/api/admin/products
/api/admin/categories
/api/admin/orders
```

---

## FR-API-02 — Authentication

Các API protected phải yêu cầu JWT hợp lệ.

---

## FR-API-03 — Authorization

Backend phải kiểm tra role trước khi thực hiện Admin operation.

Không được chỉ dựa vào việc ẩn UI Admin ở Frontend.

---

## FR-API-04 — Resource Ownership

Các API Customer phải kiểm tra ownership đối với resource cá nhân.

Ví dụ:

```text
GET /api/orders/{id}
```

Customer A không được truy cập Order thuộc Customer B.

---

## FR-API-05 — Standard Error Response

API phải sử dụng format lỗi thống nhất:

```json
{
  "success": false,
  "message": "Error description",
  "errors": []
}
```

---

## FR-API-06 — HTTP Status Codes

API phải sử dụng HTTP Status Code phù hợp.

Ví dụ:

| Status                    | Usage                                 |
| ------------------------- | ------------------------------------- |
| 200 OK                    | Request thành công                    |
| 201 Created               | Resource được tạo                     |
| 400 Bad Request           | Request không hợp lệ                  |
| 401 Unauthorized          | Chưa authenticated / JWT không hợp lệ |
| 403 Forbidden             | Không có quyền                        |
| 404 Not Found             | Resource không tồn tại                |
| 409 Conflict              | Conflict, ví dụ SKU trùng             |
| 500 Internal Server Error | Lỗi server                            |

---

# 7. Non-Functional Requirements

## NFR-01 — Performance

Các API thông thường có mục tiêu thời gian phản hồi:

```text
< 500 ms
```

Mục tiêu này áp dụng cho các tác vụ backend thông thường và không tính riêng độ trễ của hệ thống bên thứ ba như Payment Gateway.

---

## NFR-02 — Scalability

MVP hướng tới quy mô nhỏ:

- < 1,000 Products
- < 100 concurrent users

---

## NFR-03 — Security

Hệ thống phải:

- Hash Password.
- Không lưu Password plain text.
- Sử dụng JWT.
- Kiểm tra authorization ở Backend.
- Không expose Admin API cho Customer.
- Validate input ở Backend.
- Không tin tưởng validation chỉ từ Frontend.

---

## NFR-04 — Data Integrity

Hệ thống phải đảm bảo:

- SKU unique.
- Email unique.
- CartItem `(cart_id, product_id)` unique.
- Product phải thuộc Category hợp lệ.
- OrderItem phải thuộc Order hợp lệ.
- Stock không được âm.
- Order history không bị thay đổi khi Product được cập nhật.

---

## NFR-05 — Transaction Integrity

Các nghiệp vụ liên quan đồng thời đến nhiều database records phải sử dụng transaction phù hợp.

Đặc biệt:

```text
Checkout
    ↓
Create Order
    ↓
Create OrderItems
    ↓
Deduct Stock
    ↓
Clear/Update Cart
```

Phải đảm bảo tính nhất quán dữ liệu.

---

## NFR-06 — Browser Compatibility

Frontend phải hỗ trợ các trình duyệt hiện đại:

- Google Chrome
- Mozilla Firefox
- Microsoft Edge
- Safari

---

## NFR-07 — Responsive UI

Giao diện phải hoạt động trên Desktop, Tablet và Mobile.

---

## NFR-08 — Maintainability

Codebase phải được tổ chức rõ ràng theo:

- Backend layers/modules.
- Frontend components/pages.
- Database entities.
- API resources.

Naming convention và Git convention phải được thống nhất trong tài liệu Development.

---

## NFR-09 — Logging

Server phải có Application Logging cho:

- Error
- Exception
- Các sự kiện cần thiết để debug hệ thống

Admin auditing không nằm trong MVP.

---

## NFR-10 — Testing

Backend phải có Unit Test cơ bản cho các Business Logic quan trọng:

- Checkout
- Stock deduction
- Order total calculation

API phải có Postman Collection để hỗ trợ kiểm thử.

---

## NFR-11 — CI/CD

CI/CD không phải requirement bắt buộc của MVP.

Nếu có thời gian, có thể triển khai GitHub Actions ở mức đơn giản.

---

# 8. Business Rules

Các Business Rule quan trọng của hệ thống:

| ID    | Business Rule                                                                |
| ----- | ---------------------------------------------------------------------------- |
| BR-01 | Email của Account phải unique.                                               |
| BR-02 | Customer phải đăng nhập trước khi Checkout.                                  |
| BR-03 | Admin không được đăng ký public.                                             |
| BR-04 | Product Price phải > 0.                                                      |
| BR-05 | Product Stock không được âm.                                                 |
| BR-06 | SKU phải unique.                                                             |
| BR-07 | Product thuộc đúng một Category.                                             |
| BR-08 | Product INACTIVE không được mua.                                             |
| BR-09 | Product hết Stock không được mua.                                            |
| BR-10 | Cart quantity không được vượt Stock.                                         |
| BR-11 | Cart không được Checkout nếu chứa Product không khả dụng.                    |
| BR-12 | Order mới có trạng thái PENDING.                                             |
| BR-13 | Customer chỉ được Cancel Order ở PENDING.                                    |
| BR-14 | OrderItem phải lưu giá tại thời điểm mua.                                    |
| BR-15 | Checkout thành công phải trừ Stock.                                          |
| BR-16 | Cancel Order phải hoàn Stock.                                                |
| BR-17 | Customer chỉ được xem Order của chính mình.                                  |
| BR-18 | Admin có thể xem tất cả Order.                                               |
| BR-19 | Admin không được sửa Product/Quantity/Shipping Information của Order đã tạo. |
| BR-20 | Category không được xóa khi còn Product tham chiếu.                          |
| BR-21 | Product Delete phải sử dụng Soft Delete.                                     |
| BR-22 | Admin API phải được bảo vệ bằng role authorization.                          |
| BR-23 | Coupon/Promotion không nằm trong MVP.                                        |
| BR-24 | Admin auditing không nằm trong MVP.                                          |

---

# 9. Main Business Flows

## 9.1. Guest Browsing Flow

```text
Guest
  ↓
Product List
  ↓
Search / Filter / Sort
  ↓
Product Detail
  ↓
Add to Cart
```

Guest không cần đăng nhập cho các bước trên.

---

## 9.2. Customer Checkout Flow

```text
View Cart
    ↓
Checkout
    ↓
Enter Shipping Information
    ↓
Validate Cart & Stock
    ↓
Create Order
    ↓
Deduct Stock
    ↓
Order = PENDING
```

---

## 9.3. Customer Cancel Flow

```text
PENDING Order
      ↓
Customer Cancel
      ↓
Order = CANCELLED
      ↓
Restore Stock
```

Customer không được Cancel Order đã chuyển sang:

```text
CONFIRMED
SHIPPING
DELIVERED
```

---

## 9.4. Admin Order Flow

```text
Admin
  ↓
View Orders
  ↓
Search / Filter
  ↓
View Order Detail
  ↓
Update Status
  ↓
CONFIRMED
  ↓
SHIPPING
  ↓
DELIVERED
```

---

## 9.5. Admin Product Flow

```text
Admin
  ↓
Product Management
  ├── View
  ├── Search
  ├── Create
  ├── Update
  ├── Update Stock
  ├── Activate
  ├── Deactivate
  └── Soft Delete
```

---

# 10. Out of Scope

Các chức năng sau không thuộc MVP:

- Coupon / Promotion.
- Multiple shipping addresses.
- Multiple shipping methods.
- Category hierarchy.
- Multiple Product images.
- Admin self-registration.
- Admin auditing.
- Advanced analytics/reporting.
- Advanced recommendation system.
- Advanced search engine.
- Mandatory CI/CD.
- Các chức năng E-commerce nâng cao chưa được xác định trong Requirement Clarification.

---

# 11. Requirement Traceability

Requirement Specification là cơ sở cho các tài liệu tiếp theo:

```text
Requirement Clarification
          ↓
Requirement Specification
          ↓
Use Cases
          ↓
ERD
          ↓
Class Diagram
          ↓
Wireframes
          ↓
API Specification
          ↓
Implementation
          ↓
Testing
```

Mỗi functional requirement quan trọng nên có thể trace đến ít nhất một Use Case và sau đó đến API/UI tương ứng.

Ví dụ:

| Requirement       | Use Case           | API/UI                       |
| ----------------- | ------------------ | ---------------------------- |
| FR-AUTH-02        | Login              | `POST /api/auth/login`       |
| FR-PROD-01        | View Product List  | Product List                 |
| FR-PROD-11        | Search Product     | `GET /api/products?keyword=` |
| FR-CART-03        | Add to Cart        | Cart UI / Cart API           |
| FR-CHECKOUT-01    | Checkout           | Checkout UI                  |
| FR-ORDER-01       | Create Order       | `POST /api/orders`           |
| FR-ORDER-11       | View Order History | `GET /api/orders/my-orders`  |
| FR-ADMIN-PROD-03  | Create Product     | Admin Product UI/API         |
| FR-ADMIN-CAT-02   | Create Category    | Admin Category UI/API        |
| FR-ADMIN-ORDER-01 | View All Orders    | `GET /api/admin/orders`      |

---

# 12. Acceptance Criteria Summary

Requirement Specification được xem là sẵn sàng để chuyển sang giai đoạn Design khi:

- [x] Actors đã được xác định.
- [x] Authentication requirements đã được xác định.
- [x] Authorization requirements đã được xác định.
- [x] Product requirements đã được xác định.
- [x] Category requirements đã được xác định.
- [x] Search/filter/sort requirements đã được xác định.
- [x] Shopping Cart requirements đã được xác định.
- [x] Checkout requirements đã được xác định.
- [x] Payment scope đã được xác định.
- [x] Order requirements đã được xác định.
- [x] Inventory requirements đã được xác định.
- [x] Customer Order History đã được xác định.
- [x] Admin Product Management đã được xác định.
- [x] Admin Category Management đã được xác định.
- [x] Admin Order Management đã được xác định.
- [x] Security requirements đã được xác định.
- [x] UI/UX requirements đã được xác định.
- [x] Non-functional requirements đã được xác định.
- [x] Testing requirements đã được xác định.
- [x] MVP Out-of-scope đã được xác định.

---

# 13. Open Technical Decisions

Một số chi tiết không còn là business requirement nhưng cần được quyết định trong Design/Implementation:

| ID    | Decision Needed                                     | Target Phase            |
| ----- | --------------------------------------------------- | ----------------------- |
| TD-01 | Chọn BCrypt hay Argon2                              | Backend Setup           |
| TD-02 | Chọn Tailwind CSS hay Bootstrap                     | Frontend Setup          |
| TD-03 | Chọn cơ chế Soft Delete: `status` hoặc `deleted_at` | ERD                     |
| TD-04 | Xác định chính xác Product page size: 10 hoặc 12    | API/UI Design           |
| TD-05 | Xác định format của SKU nếu auto-generated          | Database/API Design     |
| TD-06 | Xác định chi tiết Order Status Transition Matrix    | Use Case/API Design     |
| TD-07 | Xác định cơ chế concurrency control cho Stock       | Backend/Database Design |
| TD-08 | Thiết kế chi tiết Payment Status                    | ERD/API Design          |

Các Technical Decisions trên không làm thay đổi phạm vi nghiệp vụ chính đã được xác định trong Requirement Specification.

---

# 14. Conclusion

Requirement Specification xác định phạm vi chức năng của hệ thống Mini E-commerce / Inventory Management trong phạm vi MVP.

Các nghiệp vụ trọng tâm bao gồm:

1. Authentication & Authorization
2. Product Browsing
3. Product Search / Filter / Sort
4. Category Management
5. Shopping Cart
6. Checkout
7. Payment
8. Order Management
9. Inventory Management
10. Customer Order History
11. Admin Product Management
12. Admin Category Management
13. Admin Order Management
14. Error Handling
15. Basic Testing

