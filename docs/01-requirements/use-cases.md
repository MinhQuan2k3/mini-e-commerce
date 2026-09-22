# Use Cases

# 1. Information

| Item              | Description                                                    |
| ----------------- | -------------------------------------------------------------- |
| Project           | Mini E-commerce / Inventory Management                         |
| Company           | A-Software                                                     |                                                   
| Status            | Draft for Design                                               |

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

| Actor           | Description                                                                                             |
| --------------- | ------------------------------------------------------------------------------------------------------- |
| Guest           | Người dùng chưa đăng nhập. Có thể xem danh sách sản phẩm, tìm kiếm, lọc/sắp xếp và xem chi tiết sản phẩm. |
| Customer        | Người dùng đã đăng nhập, có thể quản lý giỏ hàng cá nhân, Checkout, đặt hàng và xem lịch sử đơn hàng.    |
| Admin           | Người quản trị hệ thống có quyền quản lý Product, Category, Inventory và Order.                         |

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

Checkout & Payment
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

Cho phép Guest tạo tài khoản Customer mới để sử dụng các chức năng yêu cầu đăng nhập, chẳng hạn như Checkout và quản lý đơn hàng.

| Item                 | Description                                                            |
| -------------------- | ---------------------------------------------------------------------- |
| Use Case ID          | UC-AUTH-01                                                             |
| Use Case Name        | Register Customer                                                      |
| Primary Actor        | Guest                                                                  |
| Goal                 | Tạo tài khoản Customer mới                                             |
| Trigger              | Guest chọn chức năng Register                                          |
| Preconditions        | Guest chưa đăng nhập                                                   |
| Postconditions       | Tài khoản Customer được tạo thành công hoặc yêu cầu đăng ký bị từ chối |
| Related Requirements | FR-AUTH-01, BR-01                                                      |

### Main Success Flow

1. Guest mở trang Register.
2. Hệ thống hiển thị biểu mẫu đăng ký.
3. Guest nhập Email và Password.
4. Guest gửi biểu mẫu.
5. Hệ thống kiểm tra định dạng Email.
6. Hệ thống kiểm tra Email đã tồn tại hay chưa.
7. Hệ thống kiểm tra dữ liệu đầu vào.
8. Hệ thống hash Password bằng BCrypt hoặc Argon2.
9. Hệ thống tạo tài khoản Customer.
10. Hệ thống hiển thị thông báo đăng ký thành công.

### Alternative / Exception Flows

| Step | Condition              | System Response                                    |
| ---- | ---------------------- | -------------------------------------------------- |
| 5    | Email không hợp lệ     | Hiển thị lỗi định dạng Email                       |
| 6    | Email đã tồn tại       | Từ chối đăng ký và thông báo Email đã được sử dụng |
| 7    | Thiếu dữ liệu bắt buộc | Hiển thị lỗi validation                            |
| 8    | Lỗi khi lưu dữ liệu    | Không tạo tài khoản và hiển thị lỗi hệ thống       |

---

## UC-AUTH-02 — Login

### Description

Cho phép Guest, Customer hoặc Admin đăng nhập vào hệ thống bằng Email và Password.

| Item                 | Description                                                   |
| -------------------- | ------------------------------------------------------------- |
| Use Case ID          | UC-AUTH-02                                                    |
| Use Case Name        | Login                                                         |
| Primary Actor        | Guest, Customer, Admin                                        |
| Goal                 | Xác thực tài khoản và truy cập chức năng phù hợp              |
| Trigger              | Người dùng gửi Login Form                                     |
| Preconditions        | Người dùng có tài khoản hợp lệ                                |
| Postconditions       | Người dùng được xác thực và nhận JWT nếu đăng nhập thành công |
| Related Requirements | FR-AUTH-02, FR-AUTH-03, FR-AUTH-06                            |

### Main Success Flow

1. Người dùng mở trang Login.
2. Hệ thống hiển thị Email và Password fields.
3. Người dùng nhập thông tin đăng nhập.
4. Người dùng gửi Login Form.
5. Hệ thống tìm tài khoản theo Email.
6. Hệ thống kiểm tra Password.
7. Hệ thống kiểm tra trạng thái tài khoản.
8. Hệ thống tạo JWT.
9. Hệ thống trả về thông tin đăng nhập và role.
10. Frontend lưu trạng thái authentication (Token).
11. Hệ thống chuyển người dùng đến trang phù hợp theo Role.

### Alternative / Exception Flows

| Step | Condition                  | System Response                                     |
| ---- | -------------------------- | --------------------------------------------------- |
| 5    | Email không tồn tại        | Hiển thị thông báo thông tin đăng nhập không hợp lệ |
| 6    | Password sai               | Từ chối đăng nhập                                   |
| 7    | Tài khoản bị vô hiệu hóa   | Từ chối đăng nhập                                   |
| 8    | Lỗi tạo JWT                | Hiển thị lỗi hệ thống                               |

---

## UC-AUTH-03 — Authorize User

### Description

Kiểm tra quyền truy cập của người dùng dựa trên Role (Role-based authorization) trước khi cho phép thực hiện các chức năng được bảo vệ.

| Item                 | Description                                              |
| -------------------- | -------------------------------------------------------- |
| Use Case ID          | UC-AUTH-03                                               |
| Use Case Name        | Authorize User                                           |
| Primary Actor        | Customer, Admin                                          |
| Goal                 | Đảm bảo người dùng chỉ truy cập chức năng được cấp quyền |
| Trigger              | Người dùng gửi request đến protected API                 |
| Preconditions        | Request được gửi đến Backend                             |
| Postconditions       | Request được chấp nhận hoặc từ chối theo quyền           |
| Related Requirements | FR-AUTH-05, FR-API-02, FR-API-03, BR-22                  |

### Main Success Flow

1. Client gửi request kèm JWT trong Header.
2. Backend xác thực tính hợp lệ của JWT.
3. Backend trích xuất User Information và Role (`CUSTOMER` hoặc `ADMIN`).
4. Backend kiểm tra quyền truy cập resource tương ứng với Role.
5. Backend cho phép request tiếp tục xử lý nếu hợp lệ.

### Alternative / Exception Flows

| Condition                   | System Response           |
| --------------------------- | ------------------------- |
| Không có JWT                | Trả về `401 Unauthorized` |
| JWT không hợp lệ/hết hạn    | Trả về `401 Unauthorized` |
| User không đúng role yêu cầu| Trả về `403 Forbidden`    |
| Customer truy cập Admin API | Từ chối request (`403`)   |

---

# 6. Product Browsing Use Cases

## UC-PROD-01 — View Product List

### Description

Cho phép Guest, Customer và Admin xem danh sách Product.

| Item                 | Description                           |
| -------------------- | ------------------------------------- |
| Use Case ID          | UC-PROD-01                            |
| Use Case Name        | View Product List                     |
| Primary Actor        | Guest, Customer, Admin                |
| Goal                 | Xem danh sách sản phẩm hiện có        |
| Trigger              | Người dùng mở Product List            |
| Preconditions        | Hệ thống có thể truy cập Product data |
| Postconditions       | Danh sách Product được hiển thị       |
| Related Requirements | FR-PROD-01, FR-UI-02                  |

### Main Success Flow

1. Người dùng mở Product List.
2. Frontend gửi request lấy danh sách Product.
3. Backend truy vấn các Product có trạng thái `ACTIVE`.
4. Backend trả về danh sách Product bao gồm: Name, Price, Primary Image, Category, Availability/Stock Status.
5. Frontend hiển thị Product List.
6. Người dùng có thể chọn Product để xem chi tiết.

### Alternative / Exception Flows

| Condition        | System Response        |
| ---------------- | ---------------------- |
| Không có Product | Hiển thị Empty State   |
| API đang xử lý   | Hiển thị Loading State |
| API thất bại     | Hiển thị Error State   |

---

## UC-PROD-02 — View Product Detail

### Description

Cho phép người dùng xem thông tin chi tiết của một Product.

| Item                 | Description                        |
| -------------------- | ---------------------------------- |
| Use Case ID          | UC-PROD-02                         |
| Use Case Name        | View Product Detail                |
| Primary Actor        | Guest, Customer, Admin             |
| Goal                 | Xem thông tin chi tiết của Product |
| Trigger              | Người dùng chọn một Product        |
| Preconditions        | Product ID được cung cấp           |
| Postconditions       | Product Detail được hiển thị       |
| Related Requirements | FR-PROD-02, FR-PROD-03, FR-UI-03   |

### Main Success Flow

1. Người dùng chọn Product từ Product List.
2. Frontend gửi request lấy Product Detail theo ID.
3. Backend tìm Product theo ID.
4. Backend kiểm tra Product có tồn tại.
5. Backend trả về Product Detail (ID, SKU, Name, Description, Price, Stock, Image, Category, Status).
6. Frontend hiển thị thông tin Product.
7. Nếu Product có trạng thái `ACTIVE` và `stock_quantity > 0`, hiển thị nút "Thêm vào giỏ" (chỉ dành cho Customer).

### Alternative / Exception Flows

| Condition             | System Response                     |
| --------------------- | ----------------------------------- |
| Product không tồn tại | Trả về `404 Not Found`              |
| Product INACTIVE      | Hiển thị trạng thái "Không khả dụng"|
| Product hết stock     | Vô hiệu hóa nút đặt hàng / Thêm giỏ |
| API thất bại          | Hiển thị Error State                |

---

## UC-PROD-03 — Search Product

### Description

Cho phép người dùng tìm kiếm Product theo Name, SKU hoặc Description.

| Item                 | Description                             |
| -------------------- | --------------------------------------- |
| Use Case ID          | UC-PROD-03                              |
| Use Case Name        | Search Product                          |
| Primary Actor        | Guest, Customer, Admin                  |
| Goal                 | Tìm Product theo từ khóa                |
| Trigger              | Người dùng nhập Search Keyword          |
| Preconditions        | Product Search UI khả dụng              |
| Postconditions       | Danh sách Product phù hợp được hiển thị |
| Related Requirements | FR-PROD-11, FR-ADMIN-PROD-02            |

### Main Success Flow

1. Người dùng nhập Keyword vào ô tìm kiếm.
2. Frontend gửi Search Request.
3. Backend chuyển Keyword về chữ thường (case-insensitive).
4. Backend tìm kiếm trong Product Name, SKU và Description.
5. Backend trả về danh sách kết quả.
6. Frontend hiển thị danh sách kết quả.

### Alternative / Exception Flows

| Condition        | System Response                                    |
| ---------------- | -------------------------------------------------- |
| Keyword rỗng     | Hiển thị toàn bộ Product hoặc giữ nguyên danh sách |
| Không có kết quả | Hiển thị `No products found`                       |
| Lỗi truy vấn     | Hiển thị Error State                               |

---

## UC-PROD-04 — Filter, Sort and Paginate Products

### Description

Cho phép người dùng lọc, sắp xếp và phân trang danh sách Product.

| Item                 | Description                                |
| -------------------- | ------------------------------------------ |
| Use Case ID          | UC-PROD-04                                 |
| Use Case Name        | Filter, Sort and Paginate Products         |
| Primary Actor        | Guest, Customer, Admin                     |
| Goal                 | Thu hẹp và tổ chức kết quả Product         |
| Trigger              | Người dùng thay đổi Filter, Sort hoặc Page |
| Preconditions        | Product List đang được hiển thị            |
| Postconditions       | Danh sách Product được cập nhật            |
| Related Requirements | FR-PROD-09, FR-PROD-10, FR-PROD-12         |

### Main Success Flow

1. Người dùng chọn Category Filter hoặc Price Filter.
2. Người dùng chọn kiểu Sort (Price/Newest/Name).
3. Người dùng chọn số trang (Offset-based pagination, mặc định 10-12 items/trang).
4. Frontend gửi các tham số truy vấn.
5. Backend áp dụng Filter, Sort và Pagination vào SQL query.
6. Backend trả về danh sách Product và thông tin phân trang (total pages, current page).
7. Frontend cập nhật lại giao diện danh sách.

### Alternative / Exception Flows

| Condition              | System Response                       |
| ---------------------- | ------------------------------------- |
| Filter không hợp lệ    | Bỏ qua tham số không hợp lệ           |
| Page vượt quá số trang | Trả về danh sách rỗng hoặc trang cuối |
| Không có kết quả       | Hiển thị Empty State                  |

---

# 7. Shopping Cart Use Cases

## UC-CART-01 — Add Product to Cart

### Description

Cho phép Customer thêm Product còn khả dụng vào Cart cá nhân (được lưu trữ trong Database).

| Item                 | Description                                                 |
| -------------------- | ----------------------------------------------------------- |
| Use Case ID          | UC-CART-01                                                  |
| Use Case Name        | Add Product to Cart                                         |
| Primary Actor        | Customer                                                    |
| Goal                 | Thêm Product vào giỏ hàng                                   |
| Trigger              | Customer nhấn Add to Cart                                   |
| Preconditions        | Customer đã đăng nhập; Product ACTIVE và `stock_quantity > 0` |
| Postconditions       | Product được thêm vào Cart hoặc quantity hiện tại được tăng |
| Related Requirements | FR-CART-01, FR-CART-02, FR-CART-03, FR-CART-05, BR-08, BR-09|

### Main Success Flow

1. Customer mở Product Detail.
2. Customer chọn số lượng mua (Quantity).
3. Customer nhấn "Thêm vào giỏ".
4. Backend xác thực Customer JWT.
5. Backend kiểm tra Product có trạng thái `ACTIVE` và còn tồn kho (`stock_quantity > 0`).
6. Backend kiểm tra Product đã có trong Cart của Customer hay chưa (dựa trên cặp `cart_id, product_id`).
7. Nếu chưa có, Backend tạo `CartItem` mới.
8. Nếu đã có, Backend cộng dồn `quantity` vào `CartItem` hiện tại.
9. Backend kiểm tra tổng `quantity` trong giỏ không vượt quá `stock_quantity`.
10. Backend lưu thay đổi vào Database.
11. Frontend hiển thị Toast thông báo thành công.

### Alternative / Exception Flows

| Condition                                       | System Response                      |
| ----------------------------------------------- | ------------------------------------ |
| Customer chưa đăng nhập                         | Chuyển hướng sang trang Login        |
| Product INACTIVE hoặc không tồn tại             | Từ chối thao tác                     |
| Product hết hàng (`stock = 0`)                  | Vô hiệu hóa tính năng và hiển thị lỗi|
| Quantity <= 0                                   | Hiển thị lỗi validation              |
| Quantity (hoặc tổng quantity) vượt quá Stock   | Từ chối thao tác và hiển thị thông báo|

---

## UC-CART-02 — View Cart

### Description

Cho phép Customer xem danh sách các Product trong Cart của chính mình.

| Item                 | Description                            |
| -------------------- | -------------------------------------- |
| Use Case ID          | UC-CART-02                             |
| Use Case Name        | View Cart                              |
| Primary Actor        | Customer                               |
| Goal                 | Xem và kiểm tra nội dung Cart          |
| Trigger              | Customer mở trang Cart                 |
| Preconditions        | Customer đã đăng nhập                  |
| Postconditions       | Cart Items và Cart Total được hiển thị |
| Related Requirements | FR-CART-01, FR-CART-08, FR-UI-04       |

### Main Success Flow

1. Customer nhấn chọn biểu tượng Giỏ hàng.
2. Frontend gọi API `GET /api/cart` kèm JWT.
3. Backend kiểm tra danh sách `CartItem` thuộc về Customer.
4. Backend kiểm tra giá cả, trạng thái `ACTIVE/INACTIVE` và Stock hiện tại của từng Product.
5. Backend trả về dữ liệu Cart gồm các sản phẩm, số lượng, đơn giá, tổng tiền và trạng thái khả dụng.
6. Frontend hiển thị danh sách Cart Items, tổng tiền giỏ hàng và các sản phẩm không khả dụng (nếu có).

### Alternative / Exception Flows

| Condition                    | System Response                                  |
| ---------------------------- | ------------------------------------------------ |
| Customer chưa đăng nhập      | Yêu cầu đăng nhập                                |
| Cart rỗng                    | Hiển thị Empty Cart State ("Giỏ hàng rỗng")      |
| Product bị INACTIVE/hết Stock| Đánh dấu "Không khả dụng", chặn nút Checkout     |

---

## UC-CART-03 — Update Cart Quantity

### Description

Cho phép Customer thay đổi số lượng (quantity) của CartItem trong giỏ hàng.

| Item                 | Description                            |
| -------------------- | -------------------------------------- |
| Use Case ID          | UC-CART-03                             |
| Use Case Name        | Update Cart Quantity                   |
| Primary Actor        | Customer                               |
| Goal                 | Điều chỉnh số lượng Product trong Cart |
| Trigger              | Customer tăng/giảm số lượng trên UI    |
| Preconditions        | Customer đã đăng nhập, CartItem tồn tại|
| Postconditions       | Quantity được cập nhật trong Database  |
| Related Requirements | FR-CART-05, FR-CART-06, BR-10          |

### Main Success Flow

1. Customer điều chỉnh số lượng của sản phẩm trong giỏ hàng.
2. Frontend gửi request cập nhật số lượng đến Backend.
3. Backend kiểm tra `quantity > 0`.
4. Backend kiểm tra sản phẩm còn `ACTIVE`.
5. Backend kiểm tra `quantity <= product.stock_quantity`.
6. Backend cập nhật số lượng `CartItem` trong Database.
7. Backend tính toán lại tổng tiền.
8. Frontend cập nhật lại giao diện giỏ hàng và tổng giá trị.

### Alternative / Exception Flows

| Condition              | System Response                                  |
| ---------------------- | ------------------------------------------------ |
| Quantity <= 0          | Yêu cầu nhập giá trị hợp lệ hoặc gọi xóa sản phẩm|
| Quantity vượt Stock    | Từ chối cập nhật và báo lỗi không đủ tồn kho     |
| Product INACTIVE       | Đánh dấu sản phẩm không khả dụng                 |

---

## UC-CART-04 — Remove Cart Item

### Description

Cho phép Customer xóa một sản phẩm ra khỏi giỏ hàng.

| Item                 | Description                     |
| -------------------- | ------------------------------- |
| Use Case ID          | UC-CART-04                      |
| Use Case Name        | Remove Cart Item                |
| Primary Actor        | Customer                        |
| Goal                 | Xóa CartItem không còn muốn mua |
| Trigger              | Customer nhấn nút Xóa (Remove)  |
| Preconditions        | Customer đã đăng nhập, CartItem tồn tại |
| Postconditions       | CartItem bị xóa khỏi Database   |
| Related Requirements | FR-CART-07, FR-UI-04            |

### Main Success Flow

1. Customer nhấn biểu tượng Xóa trên một dòng sản phẩm trong giỏ hàng.
2. Frontend gửi request xóa `CartItem` đến Backend.
3. Backend xác thực ownership của CartItem.
4. Backend xóa `CartItem` khỏi Database.
5. Backend trả về trạng thái thành công.
6. Frontend cập nhật lại danh sách sản phẩm và tổng giá trị giỏ hàng.

### Alternative / Exception Flows

| Condition               | System Response             |
| ----------------------- | --------------------------- |
| CartItem không tồn tại  | Cập nhật lại giao diện giỏ  |
| Lỗi xử lý Backend       | Hiển thị Toast thông báo lỗi|

---

# 8. Checkout & Payment Use Cases

## UC-CHECKOUT-01 — Checkout & Place Order

### Description

Cho phép Customer nhập thông tin giao hàng, kiểm tra lại đơn hàng và hoàn tất đặt hàng (sử dụng phương thức thanh toán mặc định COD).

| Item                 | Description                                   |
| -------------------- | --------------------------------------------- |
| Use Case ID          | UC-CHECKOUT-01                                |
| Use Case Name        | Checkout & Place Order                        |
| Primary Actor        | Customer                                      |
| Goal                 | Đặt hàng thành công                            |
| Trigger              | Customer chọn Checkout từ trang Cart          |
| Preconditions        | Customer đã đăng nhập; Cart không rỗng và chứa sản phẩm hợp lệ |
| Postconditions       | Order được tạo (`PENDING`), Stock bị trừ, Cart được làm sạch |
| Related Requirements | FR-CHECKOUT-01 đến FR-CHECKOUT-09, FR-PAY-01 |

### Main Success Flow

1. Customer mở giỏ hàng và nhấn "Thanh toán / Checkout".
2. Backend validate: Customer authenticated, Cart không rỗng, tất cả sản phẩm đều ACTIVE và `cart_quantity <= stock_quantity`.
3. Customer nhập thông tin giao hàng: Full Name, Phone Number, Address, Province/City, District/Ward.
4. Hệ thống hiển thị Phương thức giao hàng mặc định ("Giao hàng tiêu chuẩn") và Phương thức thanh toán mặc định (COD).
5. Customer xác nhận thông tin và nhấn "Đặt hàng / Place Order".
6. Hệ thống chuyển sang thực hiện **UC-ORDER-01 — Create Order** trong một Database Transaction.
7. Đơn hàng được tạo thành công ở trạng thái `PENDING`, Payment Status là `UNPAID`.
8. Frontend chuyển sang trang Xác nhận đơn hàng (Order Confirmation).

### Alternative / Exception Flows

| Condition                         | System Response                                |
| --------------------------------- | ---------------------------------------------- |
| Customer chưa đăng nhập           | Chuyển hướng tới Login                         |
| Cart rỗng hoặc có item không khả dụng | Chặn Checkout và báo lỗi                      |
| Thông tin giao hàng bị thiếu      | Hiển thị lỗi validation trên Form              |
| Stock bị thay đổi trước khi nhấn Đặt hàng | Từ chối tạo Order, hiển thị thông báo cập nhật lại giỏ |

---

# 9. Order Management Use Cases

## UC-ORDER-01 — Create Order

### Description

Thực hiện tạo Order và OrderItems trong Database, trừ tồn kho và làm sạch Cart khi Customer hoàn tất Checkout.

| Item                 | Description                                                                |
| -------------------- | -------------------------------------------------------------------------- |
| Use Case ID          | UC-ORDER-01                                                                |
| Use Case Name        | Create Order                                                               |
| Primary Actor        | Customer                                                                   |
| Goal                 | Lưu thông tin Order, bảo toàn lịch sử giá và trừ tồn kho                   |
| Trigger              | Customer xác nhận Đặt hàng từ UC-CHECKOUT-01                               |
| Preconditions        | Thông tin Checkout hợp lệ                                                  |
| Postconditions       | Order được lưu, Stock giảm, CartItem bị xóa                                |
| Related Requirements | FR-CHECKOUT-05 đến 09, FR-ORDER-01 đến 04, FR-PAY-01, NFR-05, BR-12, BR-15|

### Main Success Flow (Trong 1 Database Transaction)

1. Backend bắt đầu Transaction.
2. Backend kiểm tra tồn kho chính xác của từng sản phẩm tại thời điểm tạo đơn (Stock Validation).
3. Backend tạo bản ghi `Order` mới (Order Status = `PENDING`, Payment Status = `UNPAID`, Payment Method = `COD`).
4. Với mỗi sản phẩm trong Cart, Backend tạo bản ghi `OrderItem` tương ứng và **lưu giá sản phẩm tại thời điểm mua** (`price_at_purchase`).
5. Backend tính tổng giá trị đơn hàng (`Total Amount`).
6. Backend trừ `stock_quantity` của từng sản phẩm tương ứng với số lượng mua.
7. Backend xóa các `CartItem` đã đặt hàng khỏi giỏ hàng của Customer.
8. Backend Commit Transaction.
9. Trả về Order Detail cho Frontend.

### Alternative / Exception Flows

| Condition                              | System Response                    |
| -------------------------------------- | ---------------------------------- |
| Xảy ra lỗi ở bất kỳ bước nào          | Rollback Transaction toàn bộ       |
| Sản phẩm bị hết hàng do mua đồng thời  | Rollback Transaction và thông báo lỗi tồn kho |

---

## UC-ORDER-02 — View Customer Order History

### Description

Cho phép Customer xem danh sách các Order do chính mình đã đặt.

| Item                 | Description                                |
| -------------------- | ------------------------------------------ |
| Use Case ID          | UC-ORDER-02                                |
| Use Case Name        | View Customer Order History                |
| Primary Actor        | Customer                                   |
| Goal                 | Theo dõi các Order đã tạo                  |
| Trigger              | Customer mở mục "Đơn hàng của tôi"         |
| Preconditions        | Customer đã đăng nhập                      |
| Postconditions       | Danh sách Order của Customer được hiển thị |
| Related Requirements | FR-ORDER-11, FR-ORDER-13, BR-17            |

### Main Success Flow

1. Customer truy cập trang Lịch sử đơn hàng.
2. Frontend gọi API `GET /api/orders/my-orders`.
3. Backend kiểm tra JWT và truy vấn toàn bộ Order của Customer đó.
4. Customer có thể lọc đơn hàng theo Order Status (`PENDING`, `CONFIRMED`, `SHIPPING`, `DELIVERED`, `CANCELLED`).
5. Backend trả về danh sách Order.
6. Frontend hiển thị danh sách đơn hàng bao gồm: Mã đơn, Ngày đặt, Tổng tiền, Trạng thái.

### Alternative / Exception Flows

| Condition               | System Response                                    |
| ----------------------- | -------------------------------------------------- |
| Customer chưa đăng nhập | Trả về `401 Unauthorized`                          |
| Chưa từng đặt đơn hàng  | Hiển thị Empty State ("Bạn chưa có đơn hàng nào")  |

---

## UC-ORDER-03 — View Order Detail

### Description

Cho phép Customer xem thông tin chi tiết một đơn hàng cụ thể của chính mình.

| Item                 | Description                                      |
| -------------------- | ------------------------------------------------ |
| Use Case ID          | UC-ORDER-03                                      |
| Use Case Name        | View Order Detail                                |
| Primary Actor        | Customer                                         |
| Goal                 | Xem thông tin chi tiết Order                     |
| Trigger              | Customer chọn xem một đơn hàng                   |
| Preconditions        | Customer đã đăng nhập                            |
| Postconditions       | Chi tiết đơn hàng được hiển thị                  |
| Related Requirements | FR-ORDER-12, FR-API-04, BR-17                    |

### Main Success Flow

1. Customer chọn một đơn hàng từ trang Lịch sử đơn hàng.
2. Frontend gọi API `GET /api/orders/{id}`.
3. Backend kiểm tra JWT và xác thực **quyền sở hữu** đơn hàng (`Order.customer_id == Current User ID`).
4. Backend lấy dữ liệu Order, danh sách OrderItems (gồm giá mua cũ) và thông tin giao hàng.
5. Backend trả về dữ liệu chi tiết.
6. Frontend hiển thị thông tin chi tiết đơn hàng.

### Alternative / Exception Flows

| Condition                 | System Response           |
| ------------------------- | ------------------------- |
| Order không tồn tại       | Trả về `404 Not Found`    |
| Order thuộc Customer khác | Trả về `403 Forbidden`    |

---

## UC-ORDER-04 — Cancel Pending Order

### Description

Cho phép Customer hủy đơn hàng khi đơn hàng đang ở trạng thái `PENDING`.

| Item                 | Description                                            |
| -------------------- | ------------------------------------------------------ |
| Use Case ID          | UC-ORDER-04                                            |
| Use Case Name        | Cancel Pending Order                                   |
| Primary Actor        | Customer                                               |
| Goal                 | Hủy Order chưa xác nhận và hoàn trả tồn kho            |
| Trigger              | Customer nhấn "Hủy đơn hàng"                           |
| Preconditions        | Customer đã đăng nhập, Order ở trạng thái `PENDING`     |
| Postconditions       | Order chuyển sang `CANCELLED`, Stock được hoàn lại     |
| Related Requirements | FR-ORDER-07, FR-ORDER-10, BR-13, BR-16                 |

### Main Success Flow (Trong 1 Database Transaction)

1. Customer mở xem chi tiết đơn hàng đang ở trạng thái `PENDING`.
2. Customer nhấn nút "Hủy đơn hàng" và xác nhận.
3. Backend kiểm tra quyền sở hữu đơn hàng.
4. Backend kiểm tra trạng thái hiện tại của đơn hàng bắt buộc phải là `PENDING`.
5. Backend cập nhật trạng thái đơn hàng thành `CANCELLED`.
6. Backend cộng hoàn lại `stock_quantity` cho từng sản phẩm trong `OrderItem`.
7. Backend Commit Transaction và thông báo thành công.

### Alternative / Exception Flows

| Condition                            | System Response              |
| ------------------------------------ | ---------------------------- |
| Order đã chuyển sang `CONFIRMED` / `SHIPPING` | Từ chối hủy đơn và báo lỗi|
| Order thuộc Customer khác            | Trả về `403 Forbidden`       |

---

## UC-ORDER-05 — Update Order Status

### Description

Cho phép Admin cập nhật trạng thái xử lý đơn hàng theo luồng chuyển trạng thái hợp lệ.

| Item                 | Description                                        |
| -------------------- | -------------------------------------------------- |
| Use Case ID          | UC-ORDER-05                                        |
| Use Case Name        | Update Order Status                                |
| Primary Actor        | Admin                                              |
| Goal                 | Cập nhật tiến trình xử lý Order                    |
| Trigger              | Admin thay đổi trạng thái Order                    |
| Preconditions        | Admin đã đăng nhập; Order tồn tại                  |
| Postconditions       | Order Status được cập nhật nếu transition hợp lệ   |
| Related Requirements | FR-ORDER-08, FR-ORDER-09, FR-ADMIN-ORDER-05, BR-19 |

### Main Success Flow

1. Admin mở chi tiết đơn hàng trong trang Quản lý đơn hàng.
2. Admin chọn trạng thái mới cho đơn hàng.
3. Backend kiểm tra quyền Admin (`Role == ADMIN`).
4. Backend kiểm tra tính hợp lệ của luồng chuyển trạng thái (Order Status Transition Matrix).
5. Backend cập nhật trạng thái mới và thời gian `updated_at`.
6. Frontend hiển thị thông báo cập nhật trạng thái thành công.

### Supported Status Transitions

```text
PENDING ──► CONFIRMED ──► SHIPPING ──► DELIVERED
   │
   └──► CANCELLED (Nếu Admin/Customer hủy khi đơn ở PENDING)
```

*(Lưu ý: Admin không được chỉnh sửa sản phẩm, số lượng, giá hay thông tin giao hàng của đơn đã tạo).*

### Alternative / Exception Flows

| Condition                                | System Response         |
| ---------------------------------------- | ----------------------- |
| User không có vai trò Admin              | Trả về `403 Forbidden`  |
| Chuyển trạng thái không hợp lệ (vd: DELIVERED -> PENDING) | Từ chối cập nhật và báo lỗi|

---

# 10. Admin Product Management Use Cases

## UC-ADMIN-PROD-01 — Manage Products

### Description

Cho phép Admin thực hiện các thao tác quản trị sản phẩm: Xem, Tìm kiếm, Tạo mới, Cập nhật, Thay đổi trạng thái (ACTIVE/INACTIVE) và Soft Delete.

| Item                 | Description                                  |
| -------------------- | -------------------------------------------- |
| Use Case ID          | UC-ADMIN-PROD-01                             |
| Use Case Name        | Manage Products                              |
| Primary Actor        | Admin                                        |
| Goal                 | Quản lý thông tin danh mục sản phẩm          |
| Trigger              | Admin mở giao diện Quản lý sản phẩm          |
| Preconditions        | Admin đã đăng nhập                           |
| Postconditions       | Dữ liệu sản phẩm được thêm/sửa/xóa tương ứng |
| Related Requirements | FR-ADMIN-PROD-01 đến 07, FR-PROD-03 đến 07  |

### Main Success Flow — Create Product

1. Admin chọn "Thêm sản phẩm mới".
2. Admin nhập: SKU, Product Name, Description, Price (> 0), Stock Quantity (>= 0), Image URL, Category ID, Status (`ACTIVE`/`INACTIVE`).
3. Backend validate: SKU duy nhất, Price > 0, Stock >= 0, Category tồn tại.
4. Backend lưu Product mới vào Database.

### Main Success Flow — Soft Delete Product

1. Admin chọn nút Xóa đối với một sản phẩm.
2. Backend kiểm tra quyền Admin.
3. Backend thực hiện Soft Delete (cập nhật `Status = INACTIVE` hoặc gán thời gian `deleted_at`) nhằm bảo toàn dữ liệu lịch sử đơn hàng.

### Alternative / Exception Flows

| Condition                              | System Response         |
| -------------------------------------- | ----------------------- |
| SKU bị trùng                           | Báo lỗi SKU đã tồn tại  |
| Giá <= 0 hoặc Tồn kho < 0              | Hiển thị lỗi validation |

---

# 11. Admin Category Management Use Cases

## UC-ADMIN-CAT-01 — Manage Categories

### Description

Cho phép Admin xem, tạo mới, cập nhật và xóa danh mục sản phẩm (Flat Category).

| Item                 | Description                                                  |
| -------------------- | ------------------------------------------------------------ |
| Use Case ID          | UC-ADMIN-CAT-01                                              |
| Use Case Name        | Manage Categories                                            |
| Primary Actor        | Admin                                                        |
| Goal                 | Quản lý các danh mục sản phẩm                                |
| Trigger              | Admin mở trang Quản lý danh mục                              |
| Preconditions        | Admin đã đăng nhập                                           |
| Postconditions       | Category được tạo, sửa hoặc xóa nếu hợp lệ                   |
| Related Requirements | FR-CAT-01 đến 04, FR-ADMIN-CAT-01 đến 04, BR-20              |

### Main Success Flow — Delete Category

1. Admin nhấn Xóa một Category.
2. Backend kiểm tra xem có Product nào đang tham chiếu đến Category này hay không.
3. Nếu **không** còn Product tham chiếu, Backend thực hiện xóa Category.
4. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition                            | System Response                                |
| ------------------------------------ | ---------------------------------------------- |
| Category vẫn còn chứa Product        | Chặn thao tác xóa và báo lỗi (BR-20)           |

---

# 12. Admin Inventory Management Use Cases

## UC-ADMIN-INV-01 — Manage Inventory

### Description

Cho phép Admin xem và điều chỉnh trực tiếp số lượng tồn kho (`stock_quantity`) của từng sản phẩm.

| Item                 | Description                                   |
| -------------------- | --------------------------------------------- |
| Use Case ID          | UC-ADMIN-INV-01                               |
| Use Case Name        | Manage Inventory                              |
| Primary Actor        | Admin                                         |
| Goal                 | Duy trì và cập nhật số lượng tồn kho chính xác|
| Trigger              | Admin điều chỉnh tồn kho trong màn hình Edit Product |
| Preconditions        | Admin đã đăng nhập                            |
| Postconditions       | Stock Quantity được cập nhật                   |
| Related Requirements | FR-ADMIN-PROD-05, BR-05                       |

### Main Success Flow

1. Admin mở màn hình Quản lý tồn kho / Chỉnh sửa sản phẩm.
2. Admin nhập số lượng tồn kho mới (`stock_quantity >= 0`).
3. Backend kiểm tra `stock_quantity >= 0`.
4. Backend cập nhật giá trị tồn kho vào Database.
5. Frontend hiển thị số lượng tồn kho mới.

---

# 13. Admin Order Management Use Cases

## UC-ADMIN-ORDER-01 — Manage Orders

### Description

Cho phép Admin xem toàn bộ danh sách đơn hàng, tìm kiếm, lọc đơn hàng và xem chi tiết đơn hàng trong hệ thống.

| Item                 | Description                                                             |
| -------------------- | ----------------------------------------------------------------------- |
| Use Case ID          | UC-ADMIN-ORDER-01                                                       |
| Use Case Name        | Manage Orders                                                           |
| Primary Actor        | Admin                                                                   |
| Goal                 | Quản lý và theo dõi toàn bộ đơn hàng hệ thống                           |
| Trigger              | Admin mở trang Quản lý đơn hàng                                         |
| Preconditions        | Admin đã đăng nhập                                                      |
| Postconditions       | Danh sách / Chi tiết đơn hàng được hiển thị                             |
| Related Requirements | FR-ADMIN-ORDER-01 đến 05                                                |

### Main Success Flow

1. Admin xem danh sách toàn bộ Order (`GET /api/admin/orders`).
2. Admin có thể tìm kiếm đơn hàng theo Order ID, Tên Customer, Email hoặc Số điện thoại.
3. Admin có thể lọc danh sách theo Order Status.
4. Admin chọn một đơn hàng để xem đầy đủ thông tin: Thông tin người nhận, danh sách OrderItems, đơn giá lúc mua, tổng tiền và trạng thái thanh toán.

---

# 14. Cross-Cutting Use Case Rules

## 14.1. Authentication & Authorization Rule

- Các chức năng nâng cao (Cart, Checkout, Order History, Admin Operations) bắt buộc phải qua xác thực JWT.
- Backend phải phân quyền theo Role (`CUSTOMER`, `ADMIN`) trên Server-side. Không tin tưởng kiểm tra ở Client-side.

## 14.2. Ownership Rule

- Customer chỉ được phép xem và hủy các Order do chính tài khoản đó tạo ra (`customer_id`).

## 14.3. Inventory & Stock Rules

1. `stock_quantity` không được âm (`>= 0`).
2. Khi Checkout thành công, Stock phải bị trừ ngay trong Database Transaction.
3. Khi Order bị hủy (`CANCELLED`), Stock phải được cộng hoàn trả lại (chỉ thực hiện hoàn Stock đúng 1 lần).

## 14.4. Order Rules

1. Giá sản phẩm trong `OrderItem` phải bảo toàn giá tại thời điểm mua (`price_at_purchase`), không thay đổi khi Admin đổi giá sản phẩm sau đó.
2. Luồng trạng thái đơn hàng tuân theo: `PENDING` -> `CONFIRMED` -> `SHIPPING` -> `DELIVERED` (hoặc `CANCELLED` từ `PENDING`).

---

# 15. Use Case Summary

| Use Case ID       | Use Case Name                      | Primary Actor             |
| ----------------- | ---------------------------------- | ------------------------- |
| UC-AUTH-01        | Register Customer                  | Guest                     |
| UC-AUTH-02        | Login                              | Guest, Customer, Admin    |
| UC-AUTH-03        | Authorize User                     | Customer, Admin           |
| UC-PROD-01        | View Product List                  | Guest, Customer, Admin    |
| UC-PROD-02        | View Product Detail                | Guest, Customer, Admin    |
| UC-PROD-03        | Search Product                     | Guest, Customer, Admin    |
| UC-PROD-04        | Filter, Sort and Paginate Products | Guest, Customer, Admin    |
| UC-CART-01        | Add Product to Cart                | Customer                  |
| UC-CART-02        | View Cart                          | Customer                  |
| UC-CART-03        | Update Cart Quantity               | Customer                  |
| UC-CART-04        | Remove Cart Item                   | Customer                  |
| UC-CHECKOUT-01    | Checkout & Place Order             | Customer                  |
| UC-ORDER-01       | Create Order                       | Customer                  |
| UC-ORDER-02       | View Customer Order History        | Customer                  |
| UC-ORDER-03       | View Order Detail                  | Customer                  |
| UC-ORDER-04       | Cancel Pending Order               | Customer                  |
| UC-ORDER-05       | Update Order Status                | Admin                     |
| UC-ADMIN-PROD-01  | Manage Products                    | Admin                     |
| UC-ADMIN-CAT-01   | Manage Categories                  | Admin                     |
| UC-ADMIN-INV-01   | Manage Inventory                   | Admin                     |
| UC-ADMIN-ORDER-01 | Manage Orders                      | Admin                     |

---

# 16. Requirement Traceability Matrix

| Functional Requirement Group | Related Use Cases                                               |
| ---------------------------- | --------------------------------------------------------------- |
| FR-AUTH                      | UC-AUTH-01, UC-AUTH-02, UC-AUTH-03                              |
| FR-PROD                      | UC-PROD-01, UC-PROD-02, UC-PROD-03, UC-PROD-04                  |
| FR-CAT                       | UC-ADMIN-CAT-01                                                 |
| FR-CART                      | UC-CART-01, UC-CART-02, UC-CART-03, UC-CART-04                  |
| FR-CHECKOUT                  | UC-CHECKOUT-01, UC-ORDER-01                                     |
| FR-PAY                       | UC-CHECKOUT-01, UC-ORDER-01                                     |
| FR-ORDER                     | UC-ORDER-01, UC-ORDER-02, UC-ORDER-03, UC-ORDER-04, UC-ORDER-05 |
| FR-ADMIN-PROD                | UC-ADMIN-PROD-01                                                |
| FR-ADMIN-CAT                 | UC-ADMIN-CAT-01                                                 |
| FR-ADMIN-ORDER               | UC-ADMIN-ORDER-01                                               |
| FR-ADMIN-INV                 | UC-ADMIN-INV-01                                                 |
| FR-API                       | UC-AUTH-03 và các API Use Cases                                 |

---

# 17. Out of Scope

Các tính năng/Use Case sau không thuộc phạm vi MVP:

- Coupon / Promotion / Mã giảm giá.
- Multiple Shipping Addresses / Multiple Shipping Methods.
- Category Hierarchy (Danh mục cha/con).
- Multiple Product Images (Nhiều hình ảnh sản phẩm).
- Online Payment Gateway Integration / E-Wallet.
- Admin Self-Registration (Đăng ký tài khoản Admin công khai).
- Admin Auditing Logs.

---

# 18. Open Design Decisions

Các vấn đề kỹ thuật chi tiết sẽ được quyết định ở giai đoạn API Specification & Database Design:

| ID    | Open Decision                                       | Related Use Cases                      |
| ----- | --------------------------------------------------- | -------------------------------------- |
| OD-01 | Chi tiết Stock Locking / Concurrency Control        | UC-ORDER-01                            |
| OD-02 | Chọn `status` hay `deleted_at` cho Soft Delete      | UC-ADMIN-PROD-01                       |
| OD-03 | Định dạng chuẩn cho API Error Response Schema       | UC-AUTH-03 và toàn bộ API Use Cases    |

---

# 19. Conclusion

Bản Use Cases đã được chuẩn hóa và bao phủ chính xác toàn bộ luồng nghiệp vụ của hệ thống **Mini E-commerce / Inventory Management** theo đúng định hướng MVP.
