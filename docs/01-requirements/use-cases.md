# Use Cases

# 1. Information

| Item | Description |
|---|---|
| Project | Mini E-commerce / Inventory Management |
| Company | A-Software |

---

# 2. Overview

Tài liệu này mô tả các Use Case chính của hệ thống **Mini E-commerce / Inventory Management**.

Use Case được xây dựng dựa trên các Functional Requirements đã được xác định trong `requirement-specification.md`, nhằm mô tả:

- Actor tương tác với hệ thống.
- Mục tiêu của từng nghiệp vụ.
- Điều kiện trước và sau khi thực hiện.
- Luồng xử lý chính.
- Các luồng thay thế và ngoại lệ.
- Mối liên hệ giữa Use Case và Functional Requirements.

Tài liệu này là cơ sở cho các bước tiếp theo:

```text
Use Cases
    ↓
ERD
    ↓
Class Diagram
    ↓
Wireframes
    ↓
API Specification
```

---

# 3. Actors

| Actor | Description |
|---|---|
| Guest | Người dùng chưa đăng nhập. Có thể xem danh sách sản phẩm, tìm kiếm, lọc/sắp xếp và xem chi tiết sản phẩm. Không được sử dụng Cart hoặc Checkout. |
| Customer | Người dùng đã đăng nhập, có thể quản lý Cart cá nhân, Checkout, đặt hàng, xem lịch sử và chi tiết đơn hàng. |
| Admin | Người quản trị hệ thống có quyền quản lý Product, Category, Inventory và Order. |

---

# 4. Use Case Diagram Overview

Các Use Case được chia thành các nhóm sau:

```text
Authentication & Account
    ├── UC-AUTH-01 Register Customer
    ├── UC-AUTH-02 Login
    └── UC-AUTH-03 Authorize User

Product Browsing
    ├── UC-PROD-01 View Product List
    ├── UC-PROD-02 View Product Detail
    ├── UC-PROD-03 Search Product
    └── UC-PROD-04 Filter, Sort and Paginate Products

Shopping Cart
    ├── UC-CART-01 Add Product to Cart
    ├── UC-CART-02 View Cart
    ├── UC-CART-03 Update Cart Quantity
    └── UC-CART-04 Remove Cart Item

Checkout
    └── UC-CHECKOUT-01 Checkout & Place Order

Order Management
    ├── UC-ORDER-01 Create Order
    ├── UC-ORDER-02 View Customer Order History
    ├── UC-ORDER-03 View Order Detail
    ├── UC-ORDER-04 Cancel Pending Order
    └── UC-ORDER-05 Update Order Status

Admin Management
    ├── UC-ADMIN-PROD-01 Manage Products
    ├── UC-ADMIN-CAT-01 Manage Categories
    ├── UC-ADMIN-INV-01 Manage Inventory
    └── UC-ADMIN-ORDER-01 Manage Orders
```

---

# 5. Authentication & Account Use Cases

## UC-AUTH-01 — Register Customer

### Description

Cho phép Guest tạo tài khoản Customer mới để sử dụng các chức năng yêu cầu đăng nhập, chẳng hạn như Cart, Checkout và quản lý đơn hàng.

| Item | Description |
|---|---|
| Use Case ID | UC-AUTH-01 |
| Use Case Name | Register Customer |
| Primary Actor | Guest |
| Goal | Tạo tài khoản Customer mới |
| Trigger | Guest chọn chức năng Register |
| Preconditions | Guest chưa đăng nhập |
| Postconditions | Tài khoản Customer được tạo thành công hoặc yêu cầu đăng ký bị từ chối |
| Related Requirements | FR-AUTH-01, BR-01 |

### Main Success Flow

1. Guest mở trang Register.
2. Hệ thống hiển thị biểu mẫu đăng ký.
3. Guest nhập Email và Password.
4. Guest gửi biểu mẫu.
5. Hệ thống kiểm tra định dạng Email.
6. Hệ thống kiểm tra Email đã tồn tại hay chưa.
7. Hệ thống kiểm tra dữ liệu đầu vào.
8. Hệ thống kiểm tra Password đáp ứng chính sách tối thiểu 8 ký tự.
9. Hệ thống hash Password bằng BCrypt hoặc Argon2.
10. Hệ thống tạo tài khoản với Role = `CUSTOMER`.
11. Hệ thống hiển thị thông báo đăng ký thành công.

### Alternative / Exception Flows

| Step | Condition | System Response |
|---|---|---|
| 5 | Email không hợp lệ | Hiển thị lỗi định dạng Email |
| 6 | Email đã tồn tại | Từ chối đăng ký và thông báo Email đã được sử dụng |
| 7 | Thiếu dữ liệu bắt buộc | Hiển thị lỗi validation |
| 8 | Password không đáp ứng chính sách | Hiển thị lỗi validation |
| 9 | Lỗi khi lưu dữ liệu | Không tạo tài khoản và hiển thị lỗi hệ thống |

---

## UC-AUTH-02 — Login

### Description

Cho phép Guest, Customer hoặc Admin đăng nhập vào hệ thống bằng Email và Password.

| Item | Description |
|---|---|
| Use Case ID | UC-AUTH-02 |
| Use Case Name | Login |
| Primary Actor | Guest, Customer, Admin |
| Goal | Xác thực tài khoản và truy cập chức năng phù hợp |
| Trigger | Người dùng gửi Login Form |
| Preconditions | Người dùng có tài khoản hợp lệ |
| Postconditions | Người dùng được xác thực và nhận JWT nếu đăng nhập thành công |
| Related Requirements | FR-AUTH-02, FR-AUTH-03, FR-AUTH-06 |

### Main Success Flow

1. Người dùng mở trang Login.
2. Hệ thống hiển thị Email và Password fields.
3. Người dùng nhập thông tin đăng nhập.
4. Người dùng gửi Login Form.
5. Hệ thống tìm tài khoản theo Email.
6. Hệ thống kiểm tra Password.
7. Hệ thống tạo JWT.
8. Hệ thống trả về thông tin đăng nhập và Role.
9. Frontend lưu trạng thái authentication (Token).
10. Hệ thống chuyển người dùng đến trang phù hợp theo Role.

### Alternative / Exception Flows

| Step | Condition | System Response |
|---|---|---|
| 5 | Email không tồn tại | Hiển thị thông báo thông tin đăng nhập không hợp lệ |
| 6 | Password sai | Từ chối đăng nhập |
| 7 | Lỗi tạo JWT | Hiển thị lỗi hệ thống |

---

## UC-AUTH-03 — Authorize User

### Description

Kiểm tra quyền truy cập của người dùng dựa trên Role (Role-based authorization) trước khi cho phép thực hiện các chức năng được bảo vệ.

| Item | Description |
|---|---|
| Use Case ID | UC-AUTH-03 |
| Use Case Name | Authorize User |
| Primary Actor | Customer, Admin |
| Goal | Đảm bảo người dùng chỉ truy cập chức năng được cấp quyền |
| Trigger | Người dùng gửi request đến protected API |
| Preconditions | Request được gửi đến Backend |
| Postconditions | Request được chấp nhận hoặc từ chối theo quyền |
| Related Requirements | FR-AUTH-05, FR-API-02, FR-API-03, BR-23 |

### Main Success Flow

1. Client gửi request kèm JWT trong Header.
2. Backend xác thực tính hợp lệ của JWT.
3. Backend trích xuất User Information và Role (`CUSTOMER` hoặc `ADMIN`).
4. Backend kiểm tra quyền truy cập resource tương ứng với Role.
5. Backend cho phép request tiếp tục xử lý nếu hợp lệ.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Không có JWT | Trả về `401 Unauthorized` |
| JWT không hợp lệ/hết hạn | Trả về `401 Unauthorized` |
| User không đúng Role yêu cầu | Trả về `403 Forbidden` |
| Customer truy cập Admin API | Từ chối request (`403`) |

---

# 6. Product Browsing Use Cases

## UC-PROD-01 — View Product List

### Description

Cho phép Guest và Customer xem danh sách các Product đang ở trạng thái `ACTIVE`.

| Item | Description |
|---|---|
| Use Case ID | UC-PROD-01 |
| Use Case Name | View Product List |
| Primary Actor | Guest, Customer |
| Goal | Xem danh sách sản phẩm đang được cung cấp |
| Trigger | Người dùng mở Product List |
| Preconditions | Hệ thống có thể truy cập Product data |
| Postconditions | Danh sách Product `ACTIVE` được hiển thị |
| Related Requirements | FR-PROD-01, FR-UI-02 |

### Main Success Flow

1. Người dùng mở Product List.
2. Frontend gửi request lấy danh sách Product.
3. Backend truy vấn các Product có trạng thái `ACTIVE`.
4. Backend trả về danh sách Product bao gồm: ID, SKU, Name, Price, Description, Image URL, Category, Availability/Stock Status.
5. Frontend hiển thị Product List.
6. Người dùng có thể chọn Product để xem chi tiết.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Không có Product | Hiển thị Empty State |
| API đang xử lý | Hiển thị Loading State |
| API thất bại | Hiển thị Error State |

---

## UC-PROD-02 — View Product Detail

### Description

Cho phép Guest và Customer xem thông tin chi tiết của một Product.

| Item | Description |
|---|---|
| Use Case ID | UC-PROD-02 |
| Use Case Name | View Product Detail |
| Primary Actor | Guest, Customer |
| Goal | Xem thông tin chi tiết của Product |
| Trigger | Người dùng chọn một Product |
| Preconditions | Product ID được cung cấp |
| Postconditions | Product Detail được hiển thị |
| Related Requirements | FR-PROD-02, FR-PROD-03, FR-UI-03 |

### Main Success Flow

1. Người dùng chọn Product từ Product List.
2. Frontend gửi request lấy Product Detail theo ID.
3. Backend tìm Product theo ID.
4. Backend kiểm tra Product có tồn tại.
5. Backend trả về Product Detail gồm ID, SKU, Name, Description, Price, Stock, Image URL, Category và Status.
6. Frontend hiển thị thông tin Product.
7. Nếu Product có trạng thái `ACTIVE` và `stock_quantity > 0`, Customer được phép sử dụng nút "Thêm vào giỏ".

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Product không tồn tại | Trả về `404 Not Found` |
| Product `INACTIVE` | Backend vẫn trả về Detail với HTTP `200`; Frontend hiển thị trạng thái "Không khả dụng" |
| Product hết stock | Hiển thị "Hết hàng" và vô hiệu hóa nút "Thêm vào giỏ" |
| Guest cố gắng sử dụng Cart | Yêu cầu đăng nhập |
| API thất bại | Hiển thị Error State |

---

## UC-PROD-03 — Search Product

### Description

Cho phép Guest và Customer tìm kiếm Product theo Name, SKU hoặc Description.

| Item | Description |
|---|---|
| Use Case ID | UC-PROD-03 |
| Use Case Name | Search Product |
| Primary Actor | Guest, Customer |
| Goal | Tìm Product theo từ khóa |
| Trigger | Người dùng nhập Search Keyword |
| Preconditions | Product Search UI khả dụng |
| Postconditions | Danh sách Product phù hợp được hiển thị |
| Related Requirements | FR-PROD-11 |

### Main Success Flow

1. Người dùng nhập Keyword vào ô tìm kiếm.
2. Frontend gửi Search Request.
3. Backend thực hiện tìm kiếm không phân biệt chữ hoa/chữ thường.
4. Backend tìm kiếm Keyword trong Product Name, SKU và Description.
5. Backend chỉ trả về các Product có trạng thái `ACTIVE`.
6. Backend trả về danh sách kết quả.
7. Frontend hiển thị danh sách kết quả.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Keyword rỗng | Hiển thị toàn bộ Product hoặc giữ nguyên danh sách |
| Không có kết quả | Hiển thị `No products found` |
| Lỗi truy vấn | Hiển thị Error State |

---

## UC-PROD-04 — Filter, Sort and Paginate Products

### Description

Cho phép Guest và Customer lọc, sắp xếp và phân trang danh sách Product.

| Item | Description |
|---|---|
| Use Case ID | UC-PROD-04 |
| Use Case Name | Filter, Sort and Paginate Products |
| Primary Actor | Guest, Customer |
| Goal | Thu hẹp và tổ chức kết quả Product |
| Trigger | Người dùng thay đổi Filter, Sort hoặc Page |
| Preconditions | Product List đang được hiển thị |
| Postconditions | Danh sách Product được cập nhật |
| Related Requirements | FR-PROD-09, FR-PROD-10, FR-PROD-12 |

### Main Success Flow

1. Người dùng chọn Category Filter hoặc Price Filter.
2. Người dùng chọn kiểu Sort (Price/Newest/Name).
3. Người dùng chọn số trang.
4. Frontend gửi các tham số truy vấn.
5. Backend áp dụng Filter, Sort và Pagination vào SQL query.
6. Backend sử dụng page size mặc định là `10` và không vượt quá `50`.
7. Backend trả về danh sách Product và thông tin phân trang (total pages, current page).
8. Frontend cập nhật lại giao diện danh sách.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Filter không hợp lệ | Bỏ qua hoặc từ chối tham số không hợp lệ theo API validation |
| Page vượt quá số trang | Trả về danh sách rỗng hoặc trang cuối theo API specification |
| Không có kết quả | Hiển thị Empty State |

---

# 7. Shopping Cart Use Cases

## UC-CART-01 — Add Product to Cart

### Description

Cho phép Customer thêm Product còn khả dụng vào Cart cá nhân được lưu trữ trong Database.

| Item | Description |
|---|---|
| Use Case ID | UC-CART-01 |
| Use Case Name | Add Product to Cart |
| Primary Actor | Customer |
| Goal | Thêm Product vào Cart |
| Trigger | Customer nhấn Add to Cart |
| Preconditions | Customer đã đăng nhập; Product `ACTIVE` và `stock_quantity > 0` |
| Postconditions | Product được thêm vào Cart hoặc quantity hiện tại được tăng |
| Related Requirements | FR-CART-01, FR-CART-02, FR-CART-03, FR-CART-05, BR-08, BR-09 |

### Main Success Flow

1. Customer mở Product Detail.
2. Customer chọn số lượng mua (Quantity).
3. Customer nhấn "Thêm vào giỏ".
4. Backend xác thực Customer JWT.
5. Backend kiểm tra Product có trạng thái `ACTIVE` và còn tồn kho (`stock_quantity > 0`).
6. Backend kiểm tra Product đã có trong Cart của Customer hay chưa dựa trên cặp `cart_id, product_id`.
7. Nếu chưa có, Backend tạo `CartItem` mới.
8. Nếu đã có, Backend cộng dồn `quantity` vào `CartItem` hiện tại.
9. Backend kiểm tra tổng `quantity` trong Cart không vượt quá `stock_quantity`.
10. Backend lưu thay đổi vào Database.
11. Frontend hiển thị Toast thông báo thành công.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Customer chưa đăng nhập | Chuyển hướng sang trang Login |
| Product `INACTIVE` hoặc không tồn tại | Từ chối thao tác |
| Product hết hàng (`stock = 0`) | Vô hiệu hóa tính năng và hiển thị lỗi |
| Quantity <= 0 | Hiển thị lỗi validation |
| Quantity hoặc tổng Quantity vượt quá Stock | Từ chối thao tác và hiển thị thông báo |

---

## UC-CART-02 — View Cart

### Description

Cho phép Customer xem danh sách các Product trong Cart của chính mình.

| Item | Description |
|---|---|
| Use Case ID | UC-CART-02 |
| Use Case Name | View Cart |
| Primary Actor | Customer |
| Goal | Xem và kiểm tra nội dung Cart |
| Trigger | Customer mở trang Cart |
| Preconditions | Customer đã đăng nhập |
| Postconditions | Cart Items và Cart Total được hiển thị |
| Related Requirements | FR-CART-01, FR-CART-08, FR-UI-04 |

### Main Success Flow

1. Customer nhấn chọn biểu tượng Giỏ hàng.
2. Frontend gọi API `GET /api/cart` kèm JWT.
3. Backend kiểm tra Cart thuộc về Customer hiện tại.
4. Backend kiểm tra giá cả, trạng thái `ACTIVE/INACTIVE` và Stock hiện tại của từng Product.
5. Backend xác định trạng thái khả dụng của từng CartItem.
6. Backend trả về dữ liệu Cart gồm sản phẩm, số lượng, đơn giá, tổng tiền và trạng thái khả dụng.
7. Frontend hiển thị danh sách Cart Items, tổng tiền giỏ hàng và các sản phẩm không khả dụng nếu có.
8. Frontend chỉ cho phép Checkout khi tất cả CartItem hợp lệ.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Customer chưa đăng nhập | Yêu cầu đăng nhập |
| Cart rỗng | Hiển thị Empty Cart State ("Giỏ hàng rỗng") |
| Product `INACTIVE` | Đánh dấu `INACTIVE`, hiển thị "Không khả dụng" và chặn Checkout |
| Product hết Stock | Đánh dấu `OUT_OF_STOCK`, chặn Checkout |
| Quantity vượt Stock hiện tại | Đánh dấu `EXCEEDS_STOCK`, chặn Checkout |

---

## UC-CART-03 — Update Cart Quantity

### Description

Cho phép Customer thay đổi số lượng (Quantity) của CartItem trong Cart.

| Item | Description |
|---|---|
| Use Case ID | UC-CART-03 |
| Use Case Name | Update Cart Quantity |
| Primary Actor | Customer |
| Goal | Điều chỉnh số lượng Product trong Cart |
| Trigger | Customer tăng/giảm số lượng trên UI |
| Preconditions | Customer đã đăng nhập, CartItem tồn tại |
| Postconditions | Quantity được cập nhật trong Database |
| Related Requirements | FR-CART-05, FR-CART-06, BR-10 |

### Main Success Flow

1. Customer điều chỉnh số lượng của Product trong Cart.
2. Frontend gửi request cập nhật Quantity đến Backend.
3. Backend xác thực ownership của CartItem.
4. Backend kiểm tra `quantity > 0`.
5. Backend kiểm tra Product còn `ACTIVE`.
6. Backend kiểm tra `quantity <= product.stock_quantity`.
7. Backend cập nhật số lượng CartItem trong Database.
8. Backend tính toán lại tổng tiền.
9. Frontend cập nhật lại giao diện Cart và tổng giá trị.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Quantity <= 0 | Yêu cầu nhập giá trị hợp lệ hoặc gọi chức năng xóa Product |
| Quantity vượt Stock | Từ chối cập nhật và báo lỗi không đủ tồn kho |
| Product `INACTIVE` | Đánh dấu Product không khả dụng |
| CartItem không thuộc Customer hiện tại | Từ chối request |

---

## UC-CART-04 — Remove Cart Item

### Description

Cho phép Customer xóa một Product khỏi Cart.

| Item | Description |
|---|---|
| Use Case ID | UC-CART-04 |
| Use Case Name | Remove Cart Item |
| Primary Actor | Customer |
| Goal | Xóa CartItem không còn muốn mua |
| Trigger | Customer nhấn nút Xóa (Remove) |
| Preconditions | Customer đã đăng nhập, CartItem tồn tại |
| Postconditions | CartItem bị xóa khỏi Database |
| Related Requirements | FR-CART-07, FR-UI-04 |

### Main Success Flow

1. Customer nhấn biểu tượng Xóa trên một dòng Product trong Cart.
2. Frontend gửi request xóa CartItem đến Backend.
3. Backend xác thực ownership của CartItem.
4. Backend xóa CartItem khỏi Database.
5. Backend trả về trạng thái thành công.
6. Frontend cập nhật lại danh sách Product và tổng giá trị Cart.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| CartItem không tồn tại | Cập nhật lại giao diện Cart |
| CartItem không thuộc Customer hiện tại | Từ chối request |
| Lỗi xử lý Backend | Hiển thị Toast thông báo lỗi |

---

# 8. Checkout Use Cases

## UC-CHECKOUT-01 — Checkout & Place Order

### Description

Cho phép Customer nhập thông tin giao hàng, kiểm tra lại đơn hàng và hoàn tất đặt hàng.

| Item | Description |
|---|---|
| Use Case ID | UC-CHECKOUT-01 |
| Use Case Name | Checkout & Place Order |
| Primary Actor | Customer |
| Goal | Đặt hàng thành công |
| Trigger | Customer chọn Checkout từ trang Cart |
| Preconditions | Customer đã đăng nhập; Cart không rỗng và chứa các Product hợp lệ |
| Postconditions | Order được tạo ở trạng thái `PENDING`, Stock bị trừ và Cart được làm sạch |
| Related Requirements | FR-CHECKOUT-01 đến FR-CHECKOUT-09 |

### Main Success Flow

1. Customer mở Cart và nhấn "Checkout".
2. Backend xác thực Customer đã đăng nhập, Cart không rỗng và tất cả CartItem hợp lệ.
3. Customer nhập thông tin giao hàng:
   - Recipient Name
   - Phone
   - Address
   - Province/City
   - District
   - Ward
4. Hệ thống sử dụng phương thức giao hàng mặc định "Giao hàng tiêu chuẩn".
5. Hệ thống hiển thị thông tin Order để Customer kiểm tra.
6. Customer xác nhận và nhấn "Đặt hàng".
7. Hệ thống thực hiện UC-ORDER-01 — Create Order trong Database Transaction.
8. Order được tạo với:
   - Order Status = `PENDING`
   - Payment Status = `UNPAID`
   - `paid_at = NULL`
9. Frontend chuyển sang Order Confirmation / Order Success.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Customer chưa đăng nhập | Chuyển hướng tới Login |
| Cart rỗng | Chặn Checkout và báo lỗi |
| Cart có Product `INACTIVE` | Chặn Checkout và yêu cầu cập nhật Cart |
| Cart có Product hết Stock | Chặn Checkout và yêu cầu cập nhật Cart |
| Cart có Quantity vượt Stock | Chặn Checkout và yêu cầu cập nhật Cart |
| Thông tin giao hàng bị thiếu hoặc không hợp lệ | Hiển thị lỗi validation trên Form |
| Stock bị thay đổi trước khi đặt hàng | Từ chối tạo Order, rollback Transaction và thông báo cập nhật lại Cart |
| Lỗi trong quá trình tạo Order | Rollback toàn bộ Transaction và hiển thị lỗi |

---

# 9. Order Management Use Cases

## UC-ORDER-01 — Create Order

### Description

Thực hiện tạo Order và OrderItems trong Database, trừ tồn kho và làm sạch Cart khi Customer hoàn tất Checkout.

| Item | Description |
|---|---|
| Use Case ID | UC-ORDER-01 |
| Use Case Name | Create Order |
| Primary Actor | Customer |
| Goal | Lưu thông tin Order, bảo toàn snapshot sản phẩm và trừ tồn kho |
| Trigger | Customer xác nhận Đặt hàng từ UC-CHECKOUT-01 |
| Preconditions | Thông tin Checkout hợp lệ |
| Postconditions | Order được lưu, Stock giảm, CartItem được xóa |
| Related Requirements | FR-CHECKOUT-05 đến FR-CHECKOUT-09, FR-ORDER-01 đến FR-ORDER-04, NFR-05, BR-12, BR-15 |

### Main Success Flow — Trong 1 Database Transaction

1. Backend bắt đầu Transaction.
2. Backend đọc Cart và kiểm tra lại Product còn `ACTIVE`.
3. Backend kiểm tra Stock chính xác của từng Product tại thời điểm tạo Order.
4. Backend thực hiện cập nhật Stock theo cơ chế conditional update:

```sql
UPDATE products
SET stock_quantity = stock_quantity - :quantity
WHERE id = :productId
  AND stock_quantity >= :quantity;
```

5. Backend kiểm tra số dòng bị ảnh hưởng; nếu không đủ Stock thì Transaction thất bại.
6. Backend tạo bản ghi `Order` mới:
   - `order_status = PENDING`
   - `payment_status = UNPAID`
   - `paid_at = NULL`
7. Với mỗi Product trong Cart, Backend tạo `OrderItem` tương ứng và lưu snapshot:
   - `order_id`
   - `product_id`
   - `product_name_snapshot`
   - `sku_snapshot`
   - `unit_price_snapshot`
   - `quantity`
   - `item_total`
8. Backend tính tổng giá trị Order (`total_amount`).
9. Backend lưu thông tin giao hàng vào Order:
   - `recipient_name`
   - `phone`
   - `address`
   - `province_city`
   - `district`
   - `ward`
10. Backend xóa các CartItem đã được đặt hàng khỏi Cart của Customer.
11. Backend Commit Transaction.
12. Backend trả về Order Detail cho Frontend.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Product không còn `ACTIVE` | Rollback Transaction và thông báo Product không khả dụng |
| Stock không đủ do mua đồng thời | Rollback Transaction và thông báo lỗi tồn kho |
| Conditional Stock Update không ảnh hưởng bản ghi | Rollback Transaction |
| Xảy ra lỗi ở bất kỳ bước nào | Rollback toàn bộ Transaction |
| Không thể tạo OrderItem | Rollback toàn bộ Transaction |
| Không thể xóa CartItem sau khi tạo Order | Rollback toàn bộ Transaction |

---

## UC-ORDER-02 — View Customer Order History

### Description

Cho phép Customer xem danh sách các Order do chính mình đã đặt.

| Item | Description |
|---|---|
| Use Case ID | UC-ORDER-02 |
| Use Case Name | View Customer Order History |
| Primary Actor | Customer |
| Goal | Theo dõi các Order đã tạo |
| Trigger | Customer mở mục "Đơn hàng của tôi" |
| Preconditions | Customer đã đăng nhập |
| Postconditions | Danh sách Order của Customer được hiển thị |
| Related Requirements | FR-ORDER-11, FR-ORDER-13, BR-19 |

### Main Success Flow

1. Customer truy cập trang "Đơn hàng của tôi".
2. Frontend gọi API `GET /api/orders/my-orders`.
3. Backend xác thực JWT.
4. Backend truy vấn các Order thuộc Customer hiện tại.
5. Customer có thể lọc đơn hàng theo Order Status:
   - `PENDING`
   - `CONFIRMED`
   - `SHIPPING`
   - `DELIVERED`
   - `CANCELLED`
6. Backend trả về danh sách Order.
7. Frontend hiển thị:
   - Mã đơn
   - Ngày đặt
   - Tổng tiền
   - Trạng thái đơn hàng
   - Trạng thái thanh toán

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Customer chưa đăng nhập | Trả về `401 Unauthorized` |
| Chưa từng đặt đơn hàng | Hiển thị Empty State ("Bạn chưa có đơn hàng nào") |
| API thất bại | Hiển thị Error State |

---

## UC-ORDER-03 — View Order Detail

### Description

Cho phép Customer xem thông tin chi tiết một Order cụ thể của chính mình.

| Item | Description |
|---|---|
| Use Case ID | UC-ORDER-03 |
| Use Case Name | View Order Detail |
| Primary Actor | Customer |
| Goal | Xem thông tin chi tiết Order |
| Trigger | Customer chọn xem một Order |
| Preconditions | Customer đã đăng nhập |
| Postconditions | Chi tiết Order được hiển thị |
| Related Requirements | FR-ORDER-12, FR-API-04, BR-19 |

### Main Success Flow

1. Customer chọn một Order từ trang Lịch sử đơn hàng.
2. Frontend gọi API `GET /api/orders/{id}`.
3. Backend xác thực JWT.
4. Backend xác thực Order thuộc về Customer hiện tại.
5. Backend lấy dữ liệu Order, danh sách OrderItems và thông tin giao hàng.
6. Backend trả về dữ liệu chi tiết.
7. Frontend hiển thị:
   - Order ID
   - Created At
   - Order Status
   - Payment Status
   - Paid At nếu có
   - Danh sách OrderItems
   - Unit Price tại thời điểm mua
   - Item Total
   - Total Amount
   - Recipient Name
   - Phone
   - Address
   - Province/City
   - District
   - Ward
8. Nếu Order ở trạng thái `PENDING`, Frontend hiển thị chức năng "Hủy đơn hàng".

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Order không tồn tại | Trả về `404 Not Found` |
| Order không thuộc Customer hiện tại | Từ chối truy cập theo ownership rule |
| Customer chưa đăng nhập | Trả về `401 Unauthorized` |

---

## UC-ORDER-04 — Cancel Pending Order

### Description

Cho phép Customer hủy Order khi Order đang ở trạng thái `PENDING`.

| Item | Description |
|---|---|
| Use Case ID | UC-ORDER-04 |
| Use Case Name | Cancel Pending Order |
| Primary Actor | Customer |
| Goal | Hủy Order chưa được xác nhận và hoàn trả Stock |
| Trigger | Customer nhấn "Hủy đơn hàng" |
| Preconditions | Customer đã đăng nhập; Order thuộc Customer; Order ở trạng thái `PENDING` |
| Postconditions | Order chuyển sang `CANCELLED`; Stock được hoàn lại đúng một lần |
| Related Requirements | FR-ORDER-07, FR-ORDER-10, BR-13, BR-17, BR-18 |

### Main Success Flow — Trong 1 Database Transaction

1. Customer mở chi tiết Order đang ở trạng thái `PENDING`.
2. Customer nhấn nút "Hủy đơn hàng" và xác nhận.
3. Backend xác thực JWT và ownership của Order.
4. Backend kiểm tra trạng thái hiện tại của Order bắt buộc phải là `PENDING`.
5. Backend cập nhật trạng thái Order thành `CANCELLED`.
6. Backend cộng hoàn lại `stock_quantity` cho từng Product trong OrderItem.
7. Backend bảo đảm việc hoàn Stock chỉ được thực hiện đúng một lần.
8. Backend Commit Transaction.
9. Frontend hiển thị thông báo hủy Order thành công.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Order đã chuyển sang `CONFIRMED` / `SHIPPING` / `DELIVERED` | Từ chối hủy Order |
| Order đã ở `CANCELLED` | Từ chối thao tác và không hoàn Stock lần thứ hai |
| Order thuộc Customer khác | Từ chối truy cập theo ownership rule |
| Order không tồn tại | Trả về `404 Not Found` |
| Lỗi khi hoàn Stock | Rollback toàn bộ Transaction |
| Lỗi khi cập nhật Order | Rollback toàn bộ Transaction |

---

## UC-ORDER-05 — Update Order Status

### Description

Cho phép Admin cập nhật trạng thái xử lý Order theo luồng chuyển trạng thái hợp lệ.

| Item | Description |
|---|---|
| Use Case ID | UC-ORDER-05 |
| Use Case Name | Update Order Status |
| Primary Actor | Admin |
| Goal | Cập nhật tiến trình xử lý Order |
| Trigger | Admin thay đổi trạng thái Order |
| Preconditions | Admin đã đăng nhập; Order tồn tại |
| Postconditions | Order Status được cập nhật nếu transition hợp lệ |
| Related Requirements | FR-ORDER-08, FR-ORDER-09, FR-ADMIN-ORDER-05, BR-22, BR-26, BR-27 |

### Main Success Flow

1. Admin mở chi tiết Order trong trang Quản lý Order.
2. Admin chọn trạng thái mới cho Order.
3. Backend kiểm tra quyền Admin (`Role == ADMIN`).
4. Backend kiểm tra tính hợp lệ của Status Transition.
5. Backend cập nhật Order Status và `updated_at`.
6. Nếu trạng thái mới là `DELIVERED`, Backend tự động:
   - cập nhật `payment_status = PAID`;
   - cập nhật `paid_at = current timestamp`.
7. Nếu trạng thái mới không phải `DELIVERED`, Payment Status vẫn là `UNPAID` và `paid_at = NULL`.
8. Frontend hiển thị thông báo cập nhật trạng thái thành công.

### Supported Status Transitions

```text
PENDING ──► CONFIRMED ──► SHIPPING ──► DELIVERED
   │
   └──► CANCELLED
```

Quy tắc:

- `PENDING` có thể chuyển sang `CONFIRMED`.
- `CONFIRMED` có thể chuyển sang `SHIPPING`.
- `SHIPPING` có thể chuyển sang `DELIVERED`.
- `PENDING` có thể chuyển sang `CANCELLED`.
- Admin không được thực hiện các transition ngược hoặc bỏ qua trạng thái.
- Admin không được chỉnh sửa Product, Quantity, Unit Price hoặc thông tin giao hàng của Order đã tạo.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| User không có vai trò Admin | Trả về `403 Forbidden` |
| Order không tồn tại | Trả về `404 Not Found` |
| Chuyển trạng thái không hợp lệ | Từ chối cập nhật và báo lỗi |
| Lỗi cập nhật Payment Status khi chuyển sang `DELIVERED` | Rollback Transaction |
| Order đã `CANCELLED` hoặc `DELIVERED` | Không cho phép transition tiếp theo |

---

# 10. Admin Product Management Use Cases

## UC-ADMIN-PROD-01 — Manage Products

### Description

Cho phép Admin xem, tìm kiếm, lọc, tạo mới, cập nhật, thay đổi trạng thái và Soft Delete Product.

| Item | Description |
|---|---|
| Use Case ID | UC-ADMIN-PROD-01 |
| Use Case Name | Manage Products |
| Primary Actor | Admin |
| Goal | Quản lý toàn bộ Product trong hệ thống |
| Trigger | Admin mở giao diện Quản lý Product |
| Preconditions | Admin đã đăng nhập |
| Postconditions | Dữ liệu Product được xem hoặc cập nhật tương ứng |
| Related Requirements | FR-ADMIN-PROD-01 đến FR-ADMIN-PROD-08, FR-PROD-03 đến FR-PROD-07 |

### Main Success Flow — View and Search Products

1. Admin mở trang Quản lý Product.
2. Frontend gọi API `GET /api/admin/products`.
3. Backend kiểm tra quyền Admin.
4. Backend trả về danh sách Product bao gồm cả `ACTIVE` và `INACTIVE`.
5. Admin có thể tìm kiếm theo:
   - Product Name
   - SKU
   - Description
6. Admin có thể lọc theo:
   - Category
   - Status (`ACTIVE` / `INACTIVE`)
7. Admin có thể phân trang danh sách Product.
8. Admin có thể chọn một Product để xem Detail bằng API `GET /api/admin/products/{id}`.

### Main Success Flow — Create Product

1. Admin chọn "Thêm sản phẩm mới".
2. Admin nhập:
   - SKU
   - Product Name
   - Description
   - Price
   - Stock Quantity
   - Image URL
   - Category ID
   - Status
3. Backend validate:
   - SKU duy nhất.
   - Price > 0.
   - Stock Quantity >= 0.
   - Category tồn tại.
4. Backend lưu Product mới vào Database.
5. Frontend hiển thị thông báo tạo Product thành công.

### Main Success Flow — Update Product

1. Admin chọn Product cần chỉnh sửa.
2. Frontend hiển thị thông tin Product hiện tại.
3. Admin cập nhật thông tin cần thiết.
4. Backend validate dữ liệu.
5. Backend lưu thay đổi vào Database.
6. Frontend hiển thị thông báo cập nhật thành công.

### Main Success Flow — Change Product Status

1. Admin chọn Product.
2. Admin thay đổi Status giữa `ACTIVE` và `INACTIVE`.
3. Backend validate quyền Admin.
4. Backend cập nhật `status`.
5. Frontend hiển thị trạng thái mới.

### Main Success Flow — Soft Delete Product

1. Admin chọn nút Xóa đối với một Product.
2. Frontend hiển thị Delete Confirmation.
3. Admin xác nhận xóa.
4. Backend kiểm tra quyền Admin.
5. Backend thực hiện Soft Delete bằng cách cập nhật `status = INACTIVE`.
6. Không có bản ghi Product vật lý nào bị xóa khỏi Database.
7. Frontend cập nhật danh sách Product.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| SKU bị trùng | Trả về lỗi Conflict và thông báo SKU đã tồn tại |
| Product không tồn tại | Trả về `404 Not Found` |
| Giá <= 0 | Hiển thị lỗi validation |
| Tồn kho < 0 | Hiển thị lỗi validation |
| Category không tồn tại | Hiển thị lỗi validation |
| Admin chưa đăng nhập | Trả về `401 Unauthorized` |
| User không phải Admin | Trả về `403 Forbidden` |

---

# 11. Admin Category Management Use Cases

## UC-ADMIN-CAT-01 — Manage Categories

### Description

Cho phép Admin xem, tạo mới, cập nhật và xóa Category dạng Flat Category.

| Item | Description |
|---|---|
| Use Case ID | UC-ADMIN-CAT-01 |
| Use Case Name | Manage Categories |
| Primary Actor | Admin |
| Goal | Quản lý các Category sản phẩm |
| Trigger | Admin mở trang Quản lý Category |
| Preconditions | Admin đã đăng nhập |
| Postconditions | Category được tạo, sửa hoặc xóa nếu hợp lệ |
| Related Requirements | FR-CAT-01 đến FR-CAT-04, FR-ADMIN-CAT-01 đến FR-ADMIN-CAT-04, BR-20 |

### Main Success Flow — View Categories

1. Admin mở trang Quản lý Category.
2. Backend kiểm tra quyền Admin.
3. Backend trả về danh sách Category.
4. Frontend hiển thị Name, Description và Status.

### Main Success Flow — Create Category

1. Admin chọn "Thêm Category".
2. Admin nhập Name và Description.
3. Backend kiểm tra Name không trùng.
4. Backend tạo Category với Status phù hợp.
5. Frontend hiển thị thông báo thành công.

### Main Success Flow — Update Category

1. Admin chọn Category cần chỉnh sửa.
2. Admin cập nhật Name, Description hoặc Status.
3. Backend validate dữ liệu.
4. Backend cập nhật Category.
5. Frontend hiển thị thông báo thành công.

### Main Success Flow — Delete Category

1. Admin nhấn Xóa một Category.
2. Backend kiểm tra xem có Product nào đang tham chiếu đến Category này hay không.
3. Nếu không còn Product tham chiếu, Backend thực hiện xóa Category.
4. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Category Name bị trùng | Từ chối thao tác và hiển thị lỗi |
| Category vẫn còn Product tham chiếu | Trả về `409 Conflict`, không thay đổi dữ liệu |
| Admin muốn ẩn Category đang được sử dụng | Cập nhật `status = INACTIVE` thay vì xóa |
| Category không tồn tại | Trả về `404 Not Found` |
| Admin chưa đăng nhập | Trả về `401 Unauthorized` |
| User không phải Admin | Trả về `403 Forbidden` |

---

# 12. Admin Inventory Management Use Cases

## UC-ADMIN-INV-01 — Manage Inventory

### Description

Cho phép Admin xem và điều chỉnh trực tiếp số lượng tồn kho (`stock_quantity`) của từng Product. Inventory không phải là một Entity riêng; số lượng tồn kho được quản lý trực tiếp trên Product.

| Item | Description |
|---|---|
| Use Case ID | UC-ADMIN-INV-01 |
| Use Case Name | Manage Inventory |
| Primary Actor | Admin |
| Goal | Duy trì và cập nhật số lượng tồn kho chính xác |
| Trigger | Admin điều chỉnh tồn kho trong màn hình Edit Product |
| Preconditions | Admin đã đăng nhập |
| Postconditions | Stock Quantity được cập nhật |
| Related Requirements | FR-ADMIN-PROD-05, BR-05 |

### Main Success Flow

1. Admin mở màn hình Quản lý Product / Chỉnh sửa Product.
2. Admin nhập số lượng tồn kho mới (`stock_quantity >= 0`).
3. Backend kiểm tra quyền Admin.
4. Backend kiểm tra `stock_quantity >= 0`.
5. Backend cập nhật giá trị tồn kho vào Database.
6. Frontend hiển thị số lượng tồn kho mới.

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| `stock_quantity < 0` | Hiển thị lỗi validation |
| Product không tồn tại | Trả về `404 Not Found` |
| Admin chưa đăng nhập | Trả về `401 Unauthorized` |
| User không phải Admin | Trả về `403 Forbidden` |

---

# 13. Admin Order Management Use Cases

## UC-ADMIN-ORDER-01 — Manage Orders

### Description

Cho phép Admin xem toàn bộ danh sách Order, tìm kiếm, lọc, xem chi tiết và cập nhật trạng thái Order trong hệ thống.

| Item | Description |
|---|---|
| Use Case ID | UC-ADMIN-ORDER-01 |
| Use Case Name | Manage Orders |
| Primary Actor | Admin |
| Goal | Quản lý và theo dõi toàn bộ Order trong hệ thống |
| Trigger | Admin mở trang Quản lý Order |
| Preconditions | Admin đã đăng nhập |
| Postconditions | Danh sách / Chi tiết / Trạng thái Order được hiển thị hoặc cập nhật nếu hợp lệ |
| Related Requirements | FR-ADMIN-ORDER-01 đến FR-ADMIN-ORDER-05 |

### Main Success Flow — View and Search Orders

1. Admin mở trang Quản lý Order.
2. Frontend gọi API `GET /api/admin/orders`.
3. Backend kiểm tra quyền Admin.
4. Backend trả về danh sách Order.
5. Admin có thể tìm kiếm theo:
   - Order ID
   - Customer Email
   - Recipient Name
   - Phone
6. Admin có thể lọc danh sách theo Order Status.
7. Admin có thể phân trang danh sách.
8. Admin chọn một Order để xem Detail.

### Main Success Flow — View Order Detail

1. Admin chọn một Order.
2. Frontend gọi API `GET /api/admin/orders/{id}`.
3. Backend kiểm tra quyền Admin.
4. Backend trả về:
   - Order ID
   - Customer Email
   - Created At
   - Recipient Name
   - Phone
   - Address
   - Province/City
   - District
   - Ward
   - OrderItems
   - Unit Price
   - Quantity
   - Item Total
   - Total Amount
   - Order Status
   - Payment Status
   - Paid At
5. Frontend hiển thị đầy đủ thông tin Order.

### Main Success Flow — Update Order Status

1. Admin mở Order Detail.
2. Admin chọn Status mới.
3. Frontend chỉ cho phép các transition hợp lệ.
4. Backend kiểm tra quyền Admin.
5. Backend kiểm tra Status Transition.
6. Backend cập nhật Order Status.
7. Nếu chuyển sang `DELIVERED`, Backend tự động cập nhật `payment_status = PAID` và `paid_at`.
8. Nếu Admin hủy Order đang `PENDING`, Backend hoàn lại Stock trong cùng Database Transaction.
9. Frontend hiển thị kết quả cập nhật.

### Supported Admin Order Transitions

```text
PENDING ──► CONFIRMED ──► SHIPPING ──► DELIVERED
   │
   └──► CANCELLED
```

### Alternative / Exception Flows

| Condition | System Response |
|---|---|
| Admin chưa đăng nhập | Trả về `401 Unauthorized` |
| User không phải Admin | Trả về `403 Forbidden` |
| Order không tồn tại | Trả về `404 Not Found` |
| Status Transition không hợp lệ | Từ chối cập nhật |
| Admin hủy Order không ở `PENDING` | Từ chối thao tác |
| Admin hủy Order `PENDING` nhưng hoàn Stock thất bại | Rollback toàn bộ Transaction |
| Order đã `CANCELLED` | Không hoàn Stock lần thứ hai |
| Order đã `DELIVERED` | Không cho phép thay đổi sang trạng thái khác |

---

# 14. Cross-Cutting Use Case Rules

## 14.1. Authentication & Authorization Rule

- Các chức năng yêu cầu đăng nhập gồm:
  - Cart
  - Checkout
  - Customer Order History
  - Customer Order Detail
  - Customer Cancel Order
  - Admin Operations
- Backend phải xác thực JWT đối với các API được bảo vệ.
- Backend phải phân quyền theo Role (`CUSTOMER`, `ADMIN`) trên Server-side.
- Không tin tưởng kiểm tra quyền chỉ ở Client-side.
- Guest chỉ được sử dụng các chức năng Product Browsing công khai.

---

## 14.2. Ownership Rule

- Customer chỉ được phép xem Order do chính tài khoản đó tạo ra.
- Customer chỉ được phép hủy Order thuộc tài khoản đó.
- Customer không được truy cập Cart hoặc CartItem của Customer khác.
- Backend phải kiểm tra ownership trước khi thực hiện các thao tác liên quan đến dữ liệu cá nhân.

---

## 14.3. Inventory & Stock Rules

1. `stock_quantity >= 0`.
2. Khi Add to Cart, Quantity không được vượt quá Stock hiện tại.
3. Khi Update Cart Quantity, Quantity không được vượt quá Stock hiện tại.
4. Khi Checkout thành công, Stock phải bị trừ trong cùng Database Transaction với việc tạo Order.
5. Stock deduction phải sử dụng cơ chế conditional update hoặc cơ chế tương đương để xử lý concurrency.
6. Nếu Stock không đủ tại thời điểm tạo Order, toàn bộ Transaction phải rollback.
7. Khi Order bị hủy (`CANCELLED`), Stock phải được cộng hoàn trả.
8. Stock chỉ được hoàn đúng một lần cho mỗi Order bị hủy.
9. Admin có thể cập nhật `stock_quantity` trực tiếp từ chức năng quản lý Product/Inventory.
10. `stock_quantity` không được âm.

---

## 14.4. Order Rules

1. Order mới được tạo với `order_status = PENDING`.
2. Order mới được tạo với `payment_status = UNPAID`.
3. Order mới được tạo với `paid_at = NULL`.
4. OrderItem phải lưu snapshot của Product tại thời điểm mua:
   - `order_id`
   - `product_id`
   - `product_name_snapshot`
   - `sku_snapshot`
   - `unit_price_snapshot`
   - `quantity`
   - `item_total`
5. Giá trong OrderItem không thay đổi khi Admin cập nhật Product Price sau đó.
6. Order Status phải tuân theo:

```text
PENDING → CONFIRMED → SHIPPING → DELIVERED
PENDING → CANCELLED
```

7. Customer chỉ được hủy Order khi `PENDING`.
8. Admin chỉ được hủy Order khi `PENDING`.
9. Admin không được chỉnh sửa Product, Quantity, Unit Price hoặc thông tin giao hàng của Order đã tạo.

---

## 14.5. Payment Status Rules

1. MVP không có Payment Entity riêng.
2. MVP không tích hợp Payment Gateway, E-Wallet hoặc hệ thống thanh toán trực tuyến.
3. Order mới tạo có:
   - `payment_status = UNPAID`
   - `paid_at = NULL`
4. Khi Order chuyển sang `DELIVERED`, Backend tự động:
   - đặt `payment_status = PAID`;
   - đặt `paid_at = current timestamp`.
5. Với các Order Status khác `DELIVERED`:
   - `payment_status = UNPAID`;
   - `paid_at = NULL`.
6. Client không được tự ý gửi hoặc cập nhật `payment_status`.
7. Admin không được chỉnh sửa thủ công `payment_status` hoặc `paid_at`.
8. Payment Status phải được Backend kiểm soát dựa trên Order Status.

---

## 14.6. Product Availability Rules

Product được xem là có thể mua khi:

```text
status = ACTIVE
AND
stock_quantity > 0
```

Nếu Product:

- `INACTIVE` → không được Add to Cart.
- `stock_quantity = 0` → không được Add to Cart.
- Quantity trong Cart > Stock hiện tại → Cart không hợp lệ để Checkout.

---

## 14.7. Category Rules

1. Category sử dụng mô hình Flat Category.
2. Category Name phải là duy nhất.
3. Category có trạng thái `ACTIVE` hoặc `INACTIVE`.
4. Category `INACTIVE` không được sử dụng như Category khả dụng cho Product Browsing.
5. Category không được xóa nếu còn Product tham chiếu.
6. Khi Category đang được sử dụng nhưng cần ẩn khỏi hệ thống, Admin phải chuyển Category sang `INACTIVE`.
7. Việc xóa Category có Product tham chiếu phải trả về `409 Conflict`.

---

## 14.8. API Error Handling Rule

Các Use Case sử dụng format lỗi thống nhất:

```json
{
  "success": false,
  "message": "Error message",
  "errors": []
}
```

Trong trường hợp lỗi validation theo field:

```json
{
  "success": false,
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format"
    }
  ]
}
```

Success Response sử dụng format:

```json
{
  "success": true,
  "message": "Operation successful",
  "data": {}
}
```

---

## 14.9. Pagination Rule

- Pagination sử dụng Offset-based Pagination.
- Page size mặc định: `10`.
- Page size tối đa: `50`.
- Backend phải validate các tham số `page` và `size`.
- API phải trả về thông tin phân trang cần thiết để Frontend hiển thị Pagination UI.

---

# 15. Use Case Summary

| Use Case ID | Use Case Name | Primary Actor |
|---|---|---|
| UC-AUTH-01 | Register Customer | Guest |
| UC-AUTH-02 | Login | Guest, Customer, Admin |
| UC-AUTH-03 | Authorize User | Customer, Admin |
| UC-PROD-01 | View Product List | Guest, Customer |
| UC-PROD-02 | View Product Detail | Guest, Customer |
| UC-PROD-03 | Search Product | Guest, Customer |
| UC-PROD-04 | Filter, Sort and Paginate Products | Guest, Customer |
| UC-CART-01 | Add Product to Cart | Customer |
| UC-CART-02 | View Cart | Customer |
| UC-CART-03 | Update Cart Quantity | Customer |
| UC-CART-04 | Remove Cart Item | Customer |
| UC-CHECKOUT-01 | Checkout & Place Order | Customer |
| UC-ORDER-01 | Create Order | Customer |
| UC-ORDER-02 | View Customer Order History | Customer |
| UC-ORDER-03 | View Order Detail | Customer |
| UC-ORDER-04 | Cancel Pending Order | Customer |
| UC-ORDER-05 | Update Order Status | Admin |
| UC-ADMIN-PROD-01 | Manage Products | Admin |
| UC-ADMIN-CAT-01 | Manage Categories | Admin |
| UC-ADMIN-INV-01 | Manage Inventory | Admin |
| UC-ADMIN-ORDER-01 | Manage Orders | Admin |

---

# 16. Requirement Traceability Matrix

| Functional Requirement Group | Related Use Cases |
|---|---|
| FR-AUTH | UC-AUTH-01, UC-AUTH-02, UC-AUTH-03 |
| FR-PROD | UC-PROD-01, UC-PROD-02, UC-PROD-03, UC-PROD-04 |
| FR-CAT | UC-ADMIN-CAT-01 |
| FR-CART | UC-CART-01, UC-CART-02, UC-CART-03, UC-CART-04 |
| FR-CHECKOUT | UC-CHECKOUT-01, UC-ORDER-01 |
| FR-ORDER | UC-ORDER-01, UC-ORDER-02, UC-ORDER-03, UC-ORDER-04, UC-ORDER-05 |
| FR-ADMIN-PROD | UC-ADMIN-PROD-01, UC-ADMIN-INV-01 |
| FR-ADMIN-CAT | UC-ADMIN-CAT-01 |
| FR-ADMIN-ORDER | UC-ADMIN-ORDER-01 |
| FR-API | UC-AUTH-03 và các API Use Cases |

---

# 17. Out of Scope

Các tính năng/Use Case sau không thuộc phạm vi MVP:

- Coupon / Promotion / Mã giảm giá.
- Multiple Shipping Addresses / Multiple Shipping Methods.
- Category Hierarchy (Danh mục cha/con).
- Multiple Product Images (Nhiều hình ảnh sản phẩm).
- Online Payment Gateway Integration / E-Wallet.
- Payment Entity / Payment Table riêng.
- Guest Cart.
- Email Verification.
- Password Reset / Forgot Password.
- Admin Self-Registration (Đăng ký tài khoản Admin công khai).
- Admin Auditing Logs.
- Advanced Analytics / Reporting.
- Customer tự chỉnh sửa `payment_status` hoặc `paid_at`.
- Customer tự chỉnh sửa Order sau khi tạo.

---

# 18. Use Case-Level Design Decisions

Các quyết định dưới đây là các quyết định thiết kế ở mức Use Case được sử dụng làm cơ sở cho Design. Các Technical Decision còn Open trong Requirement Specification sẽ được chốt trong giai đoạn Design/Implementation.

| ID | Decision | Related Use Cases |
|---|---|---|
| DD-01 | Stock concurrency sử dụng conditional SQL update và kiểm tra số dòng bị ảnh hưởng | UC-ORDER-01 |
| DD-02 | Product Soft Delete được thực hiện bằng `status = INACTIVE`; không sử dụng `deleted_at` | UC-ADMIN-PROD-01 |
| DD-03 | API sử dụng Common Success/Error Response Format thống nhất | UC-AUTH-03 và toàn bộ API Use Cases |
| DD-04 | Customer Order History sử dụng `GET /api/orders/my-orders` | UC-ORDER-02 |
| DD-05 | Customer Order Detail sử dụng `GET /api/orders/{id}` và kiểm tra ownership | UC-ORDER-03 |
| DD-06 | Admin Order Search sử dụng Order ID, Customer Email, Recipient Name và Phone | UC-ADMIN-ORDER-01 |
| DD-07 | Khi Order chuyển sang `DELIVERED`, Backend tự động đặt `payment_status = PAID` và `paid_at = current timestamp` | UC-ORDER-05 |
| DD-08 | Không có Payment Entity riêng trong MVP | UC-CHECKOUT-01, UC-ORDER-01, UC-ORDER-05 |

---

# 19. Conclusion

Bản Use Cases đã được chuẩn hóa và đồng bộ với các quyết định nghiệp vụ của hệ thống **Mini E-commerce / Inventory Management**.

Các Use Case bao phủ các nhóm chức năng chính:

- Authentication & Authorization.
- Product Browsing.
- Shopping Cart.
- Checkout.
- Customer Order Management.
- Admin Product Management.
- Admin Category Management.
- Admin Inventory Management.
- Admin Order Management.

Các quy tắc quan trọng về Authentication, Ownership, Inventory, Stock Concurrency, Order Status, Payment Status, Category và API Error Handling được mô tả thống nhất trong tài liệu.
