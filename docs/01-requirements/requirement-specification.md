# Requirement Specification

## 1. Information

Item | Description
--- | ---
Project | Mini E-commerce / Inventory Management
Company | A-Software
Status | Revised after Review Round 2

* * *

# 2. System Overview

## 2.1. Purpose

Hệ thống Mini E-commerce / Inventory Management là một ứng dụng thương mại điện tử quy mô nhỏ, cho phép khách hàng xem sản phẩm, quản lý giỏ hàng và đặt hàng; đồng thời cung cấp cho Admin các chức năng quản lý sản phẩm, danh mục, tồn kho và đơn hàng.

Hệ thống được phát triển theo mô hình:

* Backend: Java Spring Boot
* Frontend: ReactJS
* Database: MySQL

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

* * *

# 3. Actors

Hệ thống có 3 nhóm actor chính.

Actor | Description
--- | ---
Guest | Người dùng chưa đăng nhập. Chỉ có thể xem sản phẩm trên trình duyệt.
Customer | Người dùng đã đăng nhập và có quyền mua hàng, quản lý giỏ hàng và xem lịch sử đơn hàng.
Admin | Người quản trị hệ thống, có quyền quản lý Product, Category, Inventory và Order.

## 3.1. Actor Permission Summary

Function | Guest | Customer | Admin
--- | --- | --- | ---
View Product | ✓ | ✓ | ✓
Search Product | ✓ | ✓ | ✓
Filter / Sort Product | ✓ | ✓ | ✓
View Product Detail | ✓ | ✓ | ✓
Add to Cart | - | ✓ | -
Manage Personal Cart | - | ✓ | -
Register | ✓ | - | -
Login | ✓ | ✓ | ✓
Checkout | - | ✓ | -
Place Order | - | ✓ | -
View Own Orders | - | ✓ | -
Cancel Own Pending Order | - | ✓ | -
Manage Products | - | - | ✓
Manage Categories | - | - | ✓
Manage Inventory | - | - | ✓
View All Orders | - | - | ✓
Update Order Status | - | - | ✓

* * *

# 4. Functional Requirements

Functional requirements được đánh mã theo nhóm để dễ trace sang Use Case, API và Test Case.

* * *

## FR-AUTH — Authentication & Account

### FR-AUTH-01 — Customer Registration

Hệ thống phải cho phép Guest đăng ký tài khoản Customer.

Thông tin đăng ký tối thiểu:

* Email
* Password
* Confirm Password (chỉ dùng để kiểm tra ở Frontend, không lưu vào Database).

Rules:

* Email phải đúng định dạng và là duy nhất.
* Password phải có tối thiểu 8 ký tự.
* Password không được lưu dưới dạng plain text.
* Password phải được hash bằng BCrypt hoặc Argon2 trước khi lưu.
* Hệ thống chỉ tạo tài khoản có role `CUSTOMER` thông qua public registration.
* Admin không được đăng ký thông qua public registration form.

* * *

### FR-AUTH-02 — Customer Login

Hệ thống phải cho phép Customer đăng nhập bằng:

* Email
* Password

Khi đăng nhập thành công:

* Backend xác thực thông tin tài khoản.
* Hệ thống cấp JWT.
* Frontend sử dụng JWT cho các API yêu cầu authentication.

* * *

### FR-AUTH-03 — Admin Login

Admin có thể đăng nhập bằng Email và Password.

Admin không được đăng ký thông qua public registration form.

* * *

### FR-AUTH-04 — Admin Account Initialization

Trong MVP, hệ thống phải tạo một tài khoản Admin mặc định thông qua Database Seed/Migration khi khởi tạo database.

Thông tin credential mặc định không được hard-code dưới dạng secret công khai trong source code.

* * *

### FR-AUTH-05 — Role-Based Authorization

Hệ thống phải phân quyền dựa trên role.

Các role chính:

```text
CUSTOMER
ADMIN
```

Backend phải kiểm tra role đối với các API yêu cầu quyền Admin.

Customer không được phép truy cập các Admin API.

* * *

### FR-AUTH-06 — Stateless Authentication

Backend phải sử dụng JWT-based stateless authentication.

Server không phụ thuộc vào session authentication để duy trì trạng thái đăng nhập.

* * *

## FR-PROD — Product Management

### FR-PROD-01 — View Product List

Hệ thống phải cho phép Guest và Customer xem danh sách Product.

Danh sách phải hiển thị tối thiểu:

* Product Name
* Price
* Primary Image
* Category
* Availability/Stock Status

Product `INACTIVE` không xuất hiện trong danh sách sản phẩm công khai.

* * *

### FR-PROD-02 — View Product Detail

Hệ thống phải cho phép người dùng xem thông tin chi tiết của Product.

Thông tin tối thiểu:

* Product ID
* SKU
* Product Name
* Description
* Price
* Stock
* Image
* Category
* Status

Product `INACTIVE` vẫn có thể được truy cập qua Product Detail nếu có URL/ID hợp lệ, nhưng phải hiển thị trạng thái không khả dụng và không được phép Add to Cart.

* * *

### FR-PROD-03 — Product Data

Mỗi Product phải có các thông tin:

Field | Requirement
--- | ---
Product ID | Required, unique
SKU | Required, unique
Product Name | Required
Description | Optional
Price | Required, > 0
Stock Quantity | Required, >= 0
Image URL | Primary image
Category | Required
Status | Active / Inactive
Created Date | Required
Updated Date | Required

* * *

### FR-PROD-04 — Product Price Validation

Hệ thống phải từ chối Product có:

```text
Price <= 0
```

* * *

### FR-PROD-05 — Product SKU

Mỗi Product phải có một SKU duy nhất.

SKU có thể:

* Được Admin nhập.
* Hoặc được hệ thống tự động tạo.

Không được phép tồn tại hai Product có cùng SKU.

* * *

### FR-PROD-06 — Product Status

Product phải hỗ trợ ít nhất hai trạng thái:

```text
ACTIVE
INACTIVE
```

Product `INACTIVE` không được phép được mua.

* * *

### FR-PROD-07 — Product Soft Delete

Khi Admin xóa Product, hệ thống phải thực hiện Soft Delete bằng cách chuyển trạng thái Product sang `INACTIVE`.

Rules:

* Không xóa vật lý Product khỏi Database.
* Product đã bị xóa theo cách này không được xuất hiện trong danh sách sản phẩm công khai và không được phép mua.
* Product vẫn được giữ lại để bảo toàn dữ liệu Order lịch sử.
* Nếu cần hiển thị lại Product, Admin có thể chuyển trạng thái từ `INACTIVE` sang `ACTIVE` thông qua chức năng quản lý Product, nếu Product đáp ứng các điều kiện hợp lệ.

Trong MVP, thao tác Delete và Deactivate có cùng kết quả dữ liệu:

```text
status = INACTIVE
```

* * *

### FR-PROD-08 — Out-of-Stock

Khi:

```text
stock_quantity = 0
```

Product phải được hiển thị là hết hàng.

Customer không được:

* Add Product vào Cart.
* Buy Now.
* Checkout Product đó.

* * *

### FR-PROD-09 — Product Filter

Hệ thống phải hỗ trợ filter Product theo:

* Category
* Price

* * *

### FR-PROD-10 — Product Sorting

Hệ thống phải hỗ trợ sort Product theo:

* Price
* Newest
* Name

* * *

### FR-PROD-11 — Product Search

Hệ thống phải hỗ trợ tìm kiếm Product theo:

* Product Name
* SKU
* Description

Search không phân biệt chữ hoa/chữ thường.

* * *

### FR-PROD-12 — Product Pagination

Product List phải hỗ trợ offset-based pagination.

Quy tắc:

* Page index mặc định: `0`.
* Page size mặc định: `10`.
* Page size tối đa: `50`.

* * *

## FR-CAT — Category Management

### FR-CAT-01 — Category Data

Category phải có:

* Category ID
* Category Name
* Description
* Status

* * *

### FR-CAT-02 — Product-Category Relationship

Mỗi Product phải thuộc chính xác một Category.

Relationship:

```text
Category 1 ─────── N Product
```

Product lưu Category foreign key.

* * *

### FR-CAT-03 — Flat Category Structure

Hệ thống không hỗ trợ Category cha/con.

Category được tổ chức dưới dạng flat list.

* * *

### FR-CAT-04 — Admin Create Category

Admin phải có thể tạo Category mới.

* * *

### FR-CAT-05 — Admin Update Category

Admin phải có thể cập nhật thông tin Category.

* * *

### FR-CAT-06 — Admin Delete Category

Admin có thể xóa Category nếu Category không còn Product nào tham chiếu.

Nếu vẫn còn Product tham chiếu:

* Hệ thống phải từ chối thao tác xóa.
* Trả về HTTP `409 Conflict`.
* Không thay đổi dữ liệu Category hoặc Product liên quan.

Nếu Admin muốn ẩn Category đang được sử dụng, Admin phải cập nhật Category sang:

```text
status = INACTIVE
```

Category `INACTIVE`:

* Không được hiển thị trong danh sách Category công khai.
* Không được sử dụng để tạo Product mới.
* Các Product đang tham chiếu Category đó vẫn được giữ lại trong Database.

* * *

## FR-CART — Shopping Cart

### FR-CART-01 — Customer Cart

Sau khi Customer đăng nhập, Cart của Customer được lưu trong Database.

Mỗi Customer chỉ có tối đa một active Cart.

* * *

### FR-CART-02 — Add Product to Cart

Customer có thể thêm Product còn hàng vào Cart.

Điều kiện:

```text
Product.status = ACTIVE
AND
Product.stock_quantity > 0
```

* * *

### FR-CART-03 — Duplicate Product in Cart

Nếu Product đã tồn tại trong Cart, hệ thống không tạo CartItem mới.

Thay vào đó:

```text
existing_quantity += requested_quantity
```

* * *

### FR-CART-04 — CartItem Uniqueness

Database phải đảm bảo mỗi Cart chỉ có tối đa một CartItem cho cùng một Product.

Constraint:

```text
UNIQUE(cart_id, product_id)
```

* * *

### FR-CART-05 — Cart Quantity Validation

Quantity trong Cart không được vượt quá Stock hiện tại.

Nếu:

```text
cart_quantity > stock_quantity
```

hệ thống phải từ chối thao tác và hiển thị lỗi.

* * *

### FR-CART-06 — Update Cart Quantity

Customer phải có thể tăng hoặc giảm quantity của CartItem.

Hệ thống phải validate lại Stock sau mỗi thay đổi.

* * *

### FR-CART-07 — Remove CartItem

Customer phải có thể xóa Product khỏi Cart.

* * *

### FR-CART-08 — Unavailable Cart Item

Nếu Product trong Cart:

* bị `INACTIVE`; hoặc
* hết Stock; hoặc
* Stock hiện tại nhỏ hơn quantity trong Cart,

UI phải hiển thị trạng thái `Không khả dụng` hoặc cảnh báo tương ứng.

Customer không được Checkout khi Cart còn Product không khả dụng.

Cart response phải cung cấp đủ thông tin để Frontend xác định:

* Product có khả dụng hay không.
* Lý do không khả dụng.
* Cart có được phép Checkout hay không.

Các lý do chính:

```text
INACTIVE
OUT_OF_STOCK
EXCEEDS_STOCK
```

* * *

## FR-CHECKOUT — Checkout

### FR-CHECKOUT-01 — Checkout Authentication

Chỉ Customer đã đăng nhập mới được Checkout.

Guest phải đăng nhập trước khi hoàn tất Order.

* * *

### FR-CHECKOUT-02 — Shipping Information

Customer phải cung cấp:

* Recipient Name
* Phone Number
* Address
* Province/City
* District
* Ward

Thông tin được nhập trực tiếp trong Checkout.

Shipping Information được lưu cùng Order để bảo toàn thông tin giao hàng tại thời điểm đặt hàng.

* * *

### FR-CHECKOUT-03 — Shipping Method

MVP sử dụng một phương thức giao hàng mặc định:

```text
Standard Delivery
```

Customer không cần lựa chọn Shipping Method.

* * *

### FR-CHECKOUT-04 — Checkout Validation

Trước khi tạo Order, hệ thống phải kiểm tra:

* Customer đã authenticated.
* Cart không rỗng.
* Product còn Active.
* Product còn đủ Stock.
* Quantity của từng CartItem hợp lệ.
* Shipping information hợp lệ.
* Product price hợp lệ.

Nếu một điều kiện không thỏa mãn, Checkout phải bị từ chối.

* * *

### FR-CHECKOUT-05 — Order Total Calculation

Hệ thống phải tính Total Amount dựa trên các OrderItem.

Công thức:

```text
Item Total = Product Price × Quantity

Order Total = Sum(Item Total)
```

Giá sử dụng trong Order phải là giá tại thời điểm Checkout.

* * *

### FR-CHECKOUT-06 — Stock Validation

Backend phải kiểm tra Stock tại thời điểm Checkout.

Không được chỉ dựa vào validation ở Frontend.

* * *

### FR-CHECKOUT-07 — Stock Deduction

Khi Checkout thành công, hệ thống phải giảm Stock tương ứng với quantity của các OrderItem.

Ví dụ:

```text
Stock trước Checkout = 10
Order Quantity       = 3
Stock sau Checkout   = 7
```

* * *

### FR-CHECKOUT-08 — Transactional Checkout

Các thao tác quan trọng trong Checkout phải được xử lý trong một database transaction để tránh trường hợp:

* Order được tạo nhưng Stock không giảm.
* Stock giảm nhưng Order không được tạo.
* OrderItem được tạo không đầy đủ.
* Cart bị clear nhưng Order không được tạo thành công.

Nếu Checkout thất bại, transaction phải rollback các thay đổi liên quan.

* * *

### FR-CHECKOUT-09 — Stock Concurrency

Backend phải kiểm tra Stock một cách an toàn tại thời điểm cập nhật để hạn chế tình trạng overselling khi có nhiều request Checkout đồng thời.

Cơ chế dự kiến:

```sql
UPDATE products
SET stock_quantity = stock_quantity - :quantity
WHERE id = :productId
  AND stock_quantity >= :quantity;
```

Backend phải kiểm tra số dòng bị ảnh hưởng. Nếu update không thành công do không đủ Stock, Checkout phải thất bại và transaction phải rollback.

* * *

### FR-CHECKOUT-10 — Coupon

MVP không hỗ trợ:

* Coupon
* Promotion
* Discount Code

* * *

## FR-ORDER — Order Management

### FR-ORDER-01 — Create Order

Customer phải có thể tạo Order sau khi Checkout hợp lệ.

Order mới được tạo với:

```text
Order Status = PENDING
Payment Status = UNPAID
Paid At = NULL
```

* * *

### FR-ORDER-02 — Order Data

Order phải lưu:

* Order ID
* Customer
* Order Items
* Total Amount
* Shipping Information
* Order Status
* Payment Status
* Paid At
* Created Date
* Updated Date

Payment Status chỉ được sử dụng để biểu diễn trạng thái thanh toán của Order:

* `UNPAID`
* `PAID`

Khi đơn hàng chuyển sang `DELIVERED`:

* `payment_status` được tự động chuyển thành `PAID`.
* `paid_at` được tự động gán thời điểm hiện tại.

Quy tắc dữ liệu thanh toán:

* `payment_status` chỉ nhận `UNPAID` hoặc `PAID`.
* Order mới được tạo với `payment_status = UNPAID` và `paid_at = NULL`.
* Khi Order chuyển sang `DELIVERED`, hệ thống tự động cập nhật `payment_status = PAID` và gán `paid_at` bằng thời điểm hiện tại.
* Với các trạng thái Order khác `DELIVERED`, `payment_status` phải là `UNPAID` và `paid_at = NULL`.
* Không cho phép Client tự quyết định hoặc gửi giá trị `payment_status`/`paid_at` để ghi đè quy tắc nghiệp vụ.
* MVP không triển khai Payment Entity riêng.
* MVP không triển khai Payment Gateway.
* MVP không triển khai thanh toán trực tuyến.
* MVP không yêu cầu Customer lựa chọn Payment Method.

`Payment Status` trong MVP chỉ là trạng thái nghiệp vụ được lưu trên Order, không phải một module thanh toán độc lập.

* * *

### FR-ORDER-03 — OrderItem Data

Mỗi OrderItem phải lưu tối thiểu:

* Order ID
* Product ID/reference
* Product Name snapshot
* SKU snapshot
* Quantity
* Price at purchase time
* Item Total

* * *

### FR-ORDER-04 — Historical Product Price

OrderItem phải lưu Product Price tại thời điểm mua.

Ví dụ:

```text
Product current price = 150,000
OrderItem price       = 120,000
```

Sau khi Order được tạo, việc Admin thay đổi Product Price không được làm thay đổi giá của OrderItem cũ.

* * *

### FR-ORDER-05 — Order Status

Order hỗ trợ các trạng thái:

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

* * *

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
   ↓
payment_status = PAID
   ↓
paid_at = current timestamp
```

Ngoài ra:

```text
PENDING
   ↓
CANCELLED
```

Customer chỉ có thể Cancel Order ở trạng thái `PENDING`.

Admin cũng chỉ được Cancel Order từ trạng thái `PENDING`.

* * *

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

* * *

### FR-ORDER-08 — Admin Update Order Status

Admin có thể cập nhật Order Status theo các transition hợp lệ.

Admin không được tùy ý chuyển Order sang một trạng thái không hợp lệ.

Admin có thể thực hiện:

```text
PENDING → CONFIRMED
PENDING → CANCELLED
CONFIRMED → SHIPPING
SHIPPING → DELIVERED
```

Khi Admin chuyển:

```text
PENDING → CANCELLED
```

hệ thống phải hoàn lại Stock theo quy tắc của `FR-ORDER-10`.

* * *

### FR-ORDER-09 — Admin Cannot Modify Order Content

Sau khi Order được tạo, Admin không được chỉnh sửa:

* Product
* Quantity
* OrderItem Price
* Shipping Information
* Total Amount
* Payment Status
* Paid At

Admin chỉ được thay đổi Order Status theo quyền được cấp.

* * *

### FR-ORDER-10 — Inventory Restoration

Khi Order chuyển từ `PENDING` sang `CANCELLED`, hệ thống phải hoàn lại số lượng tồn kho tương ứng với từng OrderItem.

Quy tắc áp dụng cho cả thao tác hủy của Customer và thao tác hủy của Admin.

Việc chuyển trạng thái Order và hoàn kho phải được xử lý trong cùng một Database Transaction.

Chỉ hoàn kho khi:

```text
Current Order Status = PENDING
AND
New Order Status = CANCELLED
```

Nếu Order đã bị hủy, hệ thống không được hoàn kho lần thứ hai.

Nếu một bước thất bại, transaction phải rollback để bảo đảm tính nhất quán dữ liệu.

* * *

### FR-ORDER-11 — Customer Order History

Customer phải có thể xem danh sách các Order của chính mình.

API:

```text
GET /api/orders/my-orders
```

* * *

### FR-ORDER-12 — Customer Order Detail

Customer phải có thể xem chi tiết Order của chính mình.

API:

```text
GET /api/orders/{id}
```

Backend phải kiểm tra ownership trước khi trả dữ liệu.

Customer không được xem Order của Customer khác.

* * *

### FR-ORDER-13 — Order History Filter

Customer có thể filter Order theo status.

Có thể xem các Order:

* Pending
* Confirmed
* Shipping
* Delivered
* Cancelled

* * *

## FR-ADMIN-PROD — Admin Product Management

### FR-ADMIN-PROD-01 — View Products

Admin phải có thể xem danh sách Product trong hệ thống, bao gồm cả Product `ACTIVE` và `INACTIVE`.

API:

```text
GET /api/admin/products
```

Admin Product List phải hỗ trợ:

* Keyword search.
* Filter Category.
* Filter Status.
* Pagination.

* * *

### FR-ADMIN-PROD-02 — Search Products

Admin phải có thể tìm kiếm Product.

Search sử dụng keyword trên:

* Product Name
* SKU
* Description

Search không phân biệt chữ hoa/chữ thường.

* * *

### FR-ADMIN-PROD-03 — Create Product

Admin phải có thể tạo Product mới.

Hệ thống phải validate:

* Product Name
* SKU
* Price
* Stock
* Category
* Status

* * *

### FR-ADMIN-PROD-04 — Update Product

Admin phải có thể cập nhật Product.

Có thể cập nhật:

* Product Name
* Description
* Price
* SKU theo rule uniqueness
* Image
* Category
* Stock
* Status

* * *

### FR-ADMIN-PROD-05 — Manage Inventory

Admin phải có thể cập nhật `stock_quantity` thông qua Product Management.

Stock không được nhỏ hơn `0`.

* * *

### FR-ADMIN-PROD-06 — Activate / Deactivate Product

Admin phải có thể:

```text
ACTIVE → INACTIVE
INACTIVE → ACTIVE
```

Product INACTIVE không được phép đặt hàng.

* * *

### FR-ADMIN-PROD-07 — Soft Delete Product

Admin có thể thực hiện Delete Product.

Delete phải được xử lý dưới dạng Soft Delete:

```text
status = INACTIVE
```

Không được hard delete Product.

* * *

### FR-ADMIN-PROD-08 — View Product Detail

Admin phải có thể xem chi tiết Product, bao gồm cả Product `ACTIVE` và `INACTIVE`.

API:

```text
GET /api/admin/products/{id}
```

* * *

## FR-ADMIN-CAT — Admin Category Management

### FR-ADMIN-CAT-01 — View Categories

Admin phải có thể xem danh sách Category.

* * *

### FR-ADMIN-CAT-02 — Create Category

Admin phải có thể tạo Category.

Category Name phải unique.

* * *

### FR-ADMIN-CAT-03 — Update Category

Admin phải có thể cập nhật Category.

* * *

### FR-ADMIN-CAT-04 — Delete Category

Admin có thể Delete Category nếu Category không có Product tham chiếu.

Nếu còn Product:

```text
Delete = Rejected
HTTP Status = 409 Conflict
```

Không được thay đổi dữ liệu liên quan.

Nếu muốn ẩn Category, Admin phải sử dụng:

```text
status = INACTIVE
```

* * *

## FR-ADMIN-ORDER — Admin Order Management

### FR-ADMIN-ORDER-01 — View All Orders

Admin phải có thể xem tất cả Order.

API:

```text
GET /api/admin/orders
```

Danh sách hỗ trợ:

* Pagination.
* Filter theo Order Status.
* Keyword search.

* * *

### FR-ADMIN-ORDER-02 — Search Orders

Admin phải có thể tìm kiếm Order theo từ khóa `keyword`.

Từ khóa được đối chiếu với:

* Order ID.
* `recipient_name`: tên người nhận hàng được lưu trong Order.
* Email của Customer liên kết với Order.
* `phone`: số điện thoại người nhận hàng được lưu trong Order.

Việc tìm kiếm theo tên và số điện thoại sử dụng thông tin giao hàng tại thời điểm đặt hàng.

Hệ thống không yêu cầu bổ sung trường `name` hoặc `phone` vào User chỉ để phục vụ chức năng tìm kiếm này.

Tìm kiếm phải hỗ trợ kết hợp với:

* Order Status filter.
* Pagination.

* * *

### FR-ADMIN-ORDER-03 — Filter Orders

Admin phải có thể filter Order theo Order Status.

Các trạng thái:

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

* * *

### FR-ADMIN-ORDER-04 — View Order Detail

Admin phải có thể xem:

* Order information
* Customer information
* Recipient information
* Customer Email
* Order Created Date
* Order Items
* Quantity
* Product price at purchase
* Total Amount
* Shipping information
* Payment Status
* Paid At
* Order Status

API:

```text
GET /api/admin/orders/{id}
```

* * *

### FR-ADMIN-ORDER-05 — Update Order Status

Admin phải có thể cập nhật Order Status theo transition được phép.

Các transition:

```text
PENDING → CONFIRMED
PENDING → CANCELLED
CONFIRMED → SHIPPING
SHIPPING → DELIVERED
```

Admin không được thực hiện các transition không hợp lệ.

Khi:

```text
PENDING → CANCELLED
```

hệ thống phải:

1. Cập nhật Order Status.
2. Hoàn lại Stock của tất cả OrderItem.
3. Thực hiện trong cùng một Database Transaction.
4. Không được hoàn Stock lần thứ hai.

Khi:

```text
SHIPPING → DELIVERED
```

hệ thống phải tự động:

```text
payment_status = PAID
paid_at = current timestamp
```

* * *

# 5. User Interface Requirements

## FR-UI-01 — Responsive Design

Frontend phải hỗ trợ Responsive UI cho:

* Desktop
* Tablet
* Mobile

* * *

## FR-UI-02 — Product List

Product List phải hỗ trợ:

* Product display
* Search
* Filter
* Sort
* Pagination

Product Card phải thể hiện tối thiểu:

* Product Name
* Price
* Category
* Image
* Availability/Stock Status

* * *

## FR-UI-03 — Product Detail

Product Detail phải hiển thị đầy đủ thông tin cần thiết và cung cấp chức năng Add to Cart khi Product khả dụng.

Nếu Product:

* `INACTIVE`; hoặc
* `stock_quantity = 0`;

thì Add to Cart phải bị disabled và UI phải hiển thị trạng thái phù hợp.

* * *

## FR-UI-04 — Shopping Cart

Cart UI phải hiển thị:

* Product
* Price
* Quantity
* Item Total
* Cart Total
* Availability
* Remove action

Customer phải có thể chỉnh sửa Quantity trực tiếp trên Cart.

Nếu CartItem không khả dụng, UI phải:

* Hiển thị lý do.
* Không cho phép Checkout khi Cart còn item không hợp lệ.

* * *

## FR-UI-05 — Checkout Page

Checkout UI phải cho phép Customer:

* Nhập Shipping Information.
* Kiểm tra lại thông tin Order.
* Xác nhận đặt hàng.

Shipping Information gồm:

* Recipient Name
* Phone Number
* Address
* Province/City
* District
* Ward

MVP không yêu cầu Customer lựa chọn:

* Payment Method.
* Shipping Method.

Shipping Method mặc định là `Standard Delivery`.

* * *

## FR-UI-06 — Order History

Customer Order History phải hiển thị:

* Order ID
* Date
* Total
* Status
* View Detail

Customer phải có thể mở Order Detail.

Order Detail phải hiển thị:

* Order information.
* Order Items.
* Quantity.
* Price.
* Total.
* Shipping Information.
* Payment Status.
* Order Status.

* * *

## FR-UI-07 — Loading State

Các màn hình có thao tác API phải có trạng thái Loading phù hợp.

* * *

## FR-UI-08 — Empty State

Các danh sách rỗng phải có Empty State.

Ví dụ:

```text
Your cart is empty.
No products found.
No orders found.
```

* * *

## FR-UI-09 — Error State

Frontend phải hiển thị Error State khi API hoặc thao tác nghiệp vụ thất bại.

* * *

## FR-UI-10 — Notification

Các thao tác thành công/thất bại phải cung cấp thông báo bằng Toast hoặc Alert.

* * *

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

Các endpoint chính:

```text
POST   /api/auth/register
POST   /api/auth/login

GET    /api/products
GET    /api/products/{id}

GET    /api/categories
GET    /api/categories/{id}

GET    /api/cart
POST   /api/cart/items
PUT    /api/cart/items/{productId}
DELETE /api/cart/items/{productId}

POST   /api/orders/checkout
GET    /api/orders/my-orders
GET    /api/orders/{id}
PATCH  /api/orders/{id}/cancel

GET    /api/admin/products
GET    /api/admin/products/{id}
POST   /api/admin/products
PUT    /api/admin/products/{id}
DELETE /api/admin/products/{id}

GET    /api/admin/categories
POST   /api/admin/categories
PUT    /api/admin/categories/{id}
DELETE /api/admin/categories/{id}

GET    /api/admin/orders
GET    /api/admin/orders/{id}
PATCH  /api/admin/orders/{id}/status
```

* * *

## FR-API-02 — Authentication

Các API protected phải yêu cầu JWT hợp lệ.

* * *

## FR-API-03 — Authorization

Backend phải kiểm tra role trước khi thực hiện Admin operation.

Không được chỉ dựa vào việc ẩn UI Admin ở Frontend.

* * *

## FR-API-04 — Resource Ownership

Các API Customer phải kiểm tra ownership đối với resource cá nhân.

Ví dụ:

```text
GET /api/orders/{id}
```

Customer A không được truy cập Order thuộc Customer B.

* * *

## FR-API-05 — Standard Error Response

API lỗi phải sử dụng format thống nhất:

```json
{
  "success": false,
  "message": "Error description",
  "errors": []
}
```

Nếu lỗi liên quan đến field cụ thể, `errors` có thể chứa:

```json
[
  {
    "field": "price",
    "message": "Price must be greater than 0"
  }
]
```

* * *

## FR-API-06 — HTTP Status Codes

API phải sử dụng HTTP Status Code phù hợp.

Status | Usage
--- | ---
200 OK | Request thành công
201 Created | Resource được tạo
400 Bad Request | Request không hợp lệ
401 Unauthorized | Chưa authenticated / JWT không hợp lệ
403 Forbidden | Không có quyền
404 Not Found | Resource không tồn tại
409 Conflict | Conflict, ví dụ SKU trùng hoặc Category đang được tham chiếu
500 Internal Server Error | Lỗi server

* * *

## FR-API-07 — Pagination

Các API List sử dụng pagination thống nhất:

* Default `page = 0`.
* Default `size = 10`.
* Maximum `size = 50`.

* * *

## FR-API-08 — Standard Success Response

API thành công phải sử dụng format thống nhất:

```json
{
  "success": true,
  "message": "Request successful",
  "data": {}
}
```

* * *

# 7. Non-Functional Requirements

## NFR-01 — Performance

Các API thông thường có mục tiêu thời gian phản hồi:

```text
< 500 ms
```

Mục tiêu này áp dụng cho các tác vụ backend thông thường và không bao gồm độ trễ của hệ thống bên thứ ba, nếu có.

MVP hiện tại không tích hợp Payment Gateway hoặc dịch vụ thanh toán bên thứ ba.

* * *

## NFR-02 — Scalability

MVP hướng tới quy mô nhỏ:

* < 1,000 Products
* < 100 concurrent users

* * *

## NFR-03 — Security

Hệ thống phải:

* Hash Password.
* Không lưu Password plain text.
* Sử dụng JWT.
* Kiểm tra authorization ở Backend.
* Không expose Admin API cho Customer.
* Validate input ở Backend.
* Không tin tưởng validation chỉ từ Frontend.
* Không cho Client tự gửi các giá trị hệ thống phải tự tính toán như `customerId`, `totalAmount`, `paymentStatus`, `paidAt` hoặc `orderStatus`.

* * *

## NFR-04 — Data Integrity

Hệ thống phải đảm bảo:

* SKU unique.
* Email unique.
* CartItem `(cart_id, product_id)` unique.
* Product phải thuộc Category hợp lệ.
* OrderItem phải thuộc Order hợp lệ.
* Stock không được âm.
* Order history không bị thay đổi khi Product được cập nhật.
* OrderItem phải bảo toàn Product Name, SKU và Price tại thời điểm mua.
* `paid_at` phải nullable.
* `payment_status` phải tuân thủ quy tắc nghiệp vụ của Order Status.

* * *

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

Đối với Cancel Order:

```text
Validate PENDING
    ↓
Update Order = CANCELLED
    ↓
Restore Stock
```

Việc cập nhật trạng thái và hoàn Stock phải nằm trong cùng một transaction.

* * *

## NFR-06 — Browser Compatibility

Frontend phải hỗ trợ các trình duyệt hiện đại:

* Google Chrome
* Mozilla Firefox
* Microsoft Edge
* Safari

* * *

## NFR-07 — Responsive UI

Giao diện phải hoạt động trên Desktop, Tablet và Mobile.

* * *

## NFR-08 — Maintainability

Codebase phải được tổ chức rõ ràng theo:

* Backend layers/modules.
* Frontend components/pages.
* Database entities.
* API resources.

Naming convention và Git convention phải được thống nhất trong tài liệu Development.

* * *

## NFR-09 — Logging

Server phải có Application Logging cho:

* Error
* Exception
* Các sự kiện cần thiết để debug hệ thống

Admin auditing không nằm trong MVP.

* * *

## NFR-10 — Testing

Backend phải có Unit Test cơ bản cho các Business Logic quan trọng:

* Checkout
* Stock deduction
* Stock restoration
* Order total calculation
* Order status transition

API phải có Postman Collection để hỗ trợ kiểm thử.

* * *

## NFR-11 — CI/CD

CI/CD không phải requirement bắt buộc của MVP.

Nếu có thời gian, có thể triển khai GitHub Actions ở mức đơn giản.

* * *

# 8. Business Rules

Các Business Rule quan trọng của hệ thống:

ID | Business Rule
--- | ---
BR-01 | Email của Account phải unique.
BR-02 | Customer phải đăng nhập trước khi Checkout.
BR-03 | Admin không được đăng ký public.
BR-04 | Product Price phải > 0.
BR-05 | Product Stock không được âm.
BR-06 | SKU phải unique.
BR-07 | Product thuộc đúng một Category.
BR-08 | Product INACTIVE không được mua.
BR-09 | Product hết Stock không được mua.
BR-10 | Cart quantity không được vượt Stock.
BR-11 | Cart không được Checkout nếu chứa Product không khả dụng.
BR-12 | Order mới có trạng thái PENDING.
BR-13 | Customer chỉ được Cancel Order ở PENDING.
BR-14 | Admin chỉ được Cancel Order ở PENDING.
BR-15 | Checkout thành công phải trừ Stock.
BR-16 | OrderItem phải lưu Product Name, SKU và Price tại thời điểm mua.
BR-17 | Cancel Order phải hoàn Stock.
BR-18 | Cancel Order chỉ được hoàn Stock một lần.
BR-19 | Customer chỉ được xem Order của chính mình.
BR-20 | Category không được xóa khi còn Product tham chiếu.
BR-21 | Product Delete phải sử dụng Soft Delete.
BR-22 | Admin không được sửa Product/Quantity/Shipping Information của Order đã tạo.
BR-23 | Admin API phải được bảo vệ bằng role authorization.
BR-24 | Coupon/Promotion không nằm trong MVP.
BR-25 | Admin auditing không nằm trong MVP.
BR-26 | Order mới có payment_status = UNPAID và paid_at = NULL.
BR-27 | Khi Order chuyển sang DELIVERED, payment_status tự động chuyển thành PAID và paid_at được gán thời điểm hiện tại.
BR-28 | Payment Status chỉ là thuộc tính của Order; MVP không có Payment Entity hoặc Payment Gateway.

* * *

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
```

Guest có thể xem danh sách, tìm kiếm, lọc, sắp xếp và xem chi tiết sản phẩm mà không cần đăng nhập.

Guest không được:

* Thêm sản phẩm vào Cart.
* Quản lý Cart.
* Checkout.

Khi muốn mua hàng, Guest phải đăng nhập hoặc đăng ký tài khoản Customer trước.

* * *

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
Create OrderItems
    ↓
Deduct Stock
    ↓
Clear Cart
    ↓
Order = PENDING
Payment = UNPAID
```

* * *

## 9.3. Customer Cancel Flow

```text
PENDING Order
      ↓
Customer Cancel
      ↓
Validate PENDING
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

* * *

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
  ├── PENDING → CONFIRMED
  │       ↓
  │   SHIPPING
  │       ↓
  │   DELIVERED
  │       ↓
  │   PAID
  │
  └── PENDING → CANCELLED
          ↓
      Restore Stock
```

* * *

## 9.5. Admin Product Flow

```text
Admin
  ↓
Product Management
  ├── View
  ├── Search
  ├── Filter
  ├── Create
  ├── Update
  ├── Update Stock
  ├── Activate
  ├── Deactivate
  └── Soft Delete
```

* * *

# 10. Out of Scope

Các chức năng sau không thuộc MVP:

* Coupon / Promotion.
* Payment Gateway.
* Online Payment.
* Payment Entity / Payment Transaction Management.
* Multiple shipping addresses.
* Multiple shipping methods.
* Category hierarchy.
* Multiple Product images.
* Guest Cart.
* Admin self-registration.
* Email Verification.
* Password Reset.
* Admin auditing.
* Advanced analytics/reporting.
* Advanced recommendation system.
* Advanced search engine.
* Mandatory CI/CD.
* Các chức năng E-commerce nâng cao chưa được xác định trong Requirement Clarification.

* * *

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

Requirement | Use Case | API/UI
--- | --- | ---
FR-AUTH-02 | Login | `POST /api/auth/login`
FR-PROD-01 | View Product List | Product List
FR-PROD-11 | Search Product | `GET /api/products?keyword=`
FR-CART-03 | Add to Cart | Cart UI / Cart API
FR-CHECKOUT-01 | Checkout | Checkout UI
FR-ORDER-01 | Create Order | `POST /api/orders/checkout`
FR-ORDER-11 | View Order History | `GET /api/orders/my-orders`
FR-ORDER-12 | View Order Detail | `GET /api/orders/{id}`
FR-ADMIN-PROD-01 | View Admin Products | `GET /api/admin/products`
FR-ADMIN-PROD-03 | Create Product | Admin Product UI/API
FR-ADMIN-CAT-02 | Create Category | Admin Category UI/API
FR-ADMIN-ORDER-01 | View All Orders | `GET /api/admin/orders`
FR-ADMIN-ORDER-05 | Update Order Status | `PATCH /api/admin/orders/{id}/status`

* * *

# 12. Acceptance Criteria Summary

Requirement Specification được xem là sẵn sàng để chuyển sang giai đoạn Design khi:

* [x] Actors đã được xác định.
* [x] Authentication requirements đã được xác định.
* [x] Authorization requirements đã được xác định.
* [x] Product requirements đã được xác định.
* [x] Category requirements đã được xác định.
* [x] Search/filter/sort requirements đã được xác định.
* [x] Shopping Cart requirements đã được xác định.
* [x] Checkout requirements đã được xác định.
* [x] Payment scope đã được xác định.
* [x] Order requirements đã được xác định.
* [x] Inventory requirements đã được xác định.
* [x] Customer Order History đã được xác định.
* [x] Admin Product Management đã được xác định.
* [x] Admin Category Management đã được xác định.
* [x] Admin Order Management đã được xác định.
* [x] Security requirements đã được xác định.
* [x] UI/UX requirements đã được xác định.
* [x] Non-functional requirements đã được xác định.
* [x] Testing requirements đã được xác định.
* [x] MVP Out-of-scope đã được xác định.
* [x] API endpoint conventions đã được thống nhất.
* [x] Pagination conventions đã được thống nhất.
* [x] Order status transition và inventory restoration rules đã được xác định.
* [x] Payment status rules đã được xác định.

* * *

# 13. Technical Decisions

Một số chi tiết kỹ thuật đã được xác định để làm cơ sở cho Design/Implementation:

ID | Decision | Status | Target Phase
--- | --- | --- | ---
TD-01 | Chọn BCrypt hay Argon2 | Open | Backend Setup
TD-02 | Chọn Tailwind CSS hay Bootstrap | Open | Frontend Setup
TD-03 | Soft Delete Product bằng `status = INACTIVE`, không hard-delete | Đã chốt | Database/API Design
TD-04 | Xác định format của SKU nếu auto-generated | Open | Database/API Design
TD-05 | Xác định chi tiết Order Status Transition Matrix và cách validate transition | Open | Use Case/API Design
TD-06 | Category không được Delete khi còn Product tham chiếu; trả `409 Conflict` | Đã chốt | Database/API Design
TD-07 | Xác định cơ chế concurrency control cho Stock ở mức implementation | Open / cần chốt ở Design | Backend/Database Design

Các Technical Decisions trên không làm thay đổi phạm vi nghiệp vụ chính đã được xác định trong Requirement Specification.

* * *

# 14. Conclusion

Requirement Specification xác định phạm vi chức năng của hệ thống Mini E-commerce / Inventory Management trong phạm vi MVP.

Các nghiệp vụ trọng tâm bao gồm:

1. Authentication & Authorization
2. Product Browsing
3. Product Search / Filter / Sort
4. Category Management
5. Shopping Cart
6. Checkout
7. Order Management
8. Inventory Management
9. Customer Order History
10. Admin Product Management
11. Admin Category Management
12. Admin Order Management
13. Payment Status Management
14. Error Handling
15. Basic Testing

MVP tập trung vào các nghiệp vụ bán hàng và quản lý tồn kho cốt lõi.

Hệ thống không triển khai Payment Gateway, Online Payment hoặc Payment Entity riêng. Trạng thái thanh toán chỉ được quản lý thông qua `payment_status` và `paid_at` trên Order; Order được tạo ở trạng thái `UNPAID` và tự động chuyển sang `PAID` khi Order đạt trạng thái `DELIVERED`.


---

## Change Log

| Version | Date | Changes |
|---|---|---|
| Revised after Review Round 2 | 2026-10-08 | Updated requirements after Review Round 2. Restored and preserved existing BR/FR/TD identifiers without renumbering previously issued IDs; new requirements are appended at the end. Clarified Order History endpoint and related requirements. Added/updated business rules for OrderItem snapshots, Product Soft Delete, Order cancellation and stock restoration, Payment Status, `paid_at`, Admin authorization, and Category deletion. Standardized naming for `customer_id`, `password_hash`, `product_name_snapshot`, `sku_snapshot`, and `unit_price_snapshot`. Clarified Payment as an Order attribute only, with no separate Payment Entity/Table or Payment Gateway in MVP. Updated technical decisions and requirement references to remain consistent with Use Cases, ERD, Class Diagram, and API Specification. |
