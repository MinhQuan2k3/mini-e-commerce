# Use Cases

## 1. Information

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
| Guest           | Người dùng chưa đăng nhập. Có thể xem sản phẩm, tìm kiếm sản phẩm và sử dụng giỏ hàng trên trình duyệt. |
| Customer        | Người dùng đã đăng nhập, có thể quản lý giỏ hàng, Checkout, đặt hàng và xem lịch sử đơn hàng.           |
| Admin           | Người quản trị có quyền quản lý Product, Category, Inventory và Order.                                  |
| Payment Gateway | Hệ thống bên thứ ba hỗ trợ xử lý thanh toán trực tuyến nếu phương thức này được sử dụng.                |

---

# 4. Use Case Diagram Overview

Các Use Case được chia thành các nhóm sau:

```text
Authentication & Account
    ├── UC-AUTH-01 Register Customer
    ├── UC-AUTH-02 Login
    ├── UC-AUTH-03 Verify Email
    ├── UC-AUTH-04 Forgot Password
    └── UC-AUTH-05 Authorize User

Product Browsing
    ├── UC-PROD-01 View Product List
    ├── UC-PROD-02 View Product Detail
    ├── UC-PROD-03 Search Product
    └── UC-PROD-04 Filter, Sort and Paginate Products

Shopping Cart
    ├── UC-CART-01 Add Product to Cart
    ├── UC-CART-02 View Cart
    ├── UC-CART-03 Update Cart Quantity
    ├── UC-CART-04 Remove Cart Item
    └── UC-CART-05 Merge Guest Cart

Checkout & Payment
    ├── UC-CHECKOUT-01 Checkout
    ├── UC-CHECKOUT-02 Select Payment Method
    └── UC-PAY-01 Process Online Payment

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
8. Hệ thống hash Password.
9. Hệ thống tạo tài khoản Customer.
10. Hệ thống yêu cầu Customer thực hiện Email Verification.
11. Hệ thống hiển thị thông báo đăng ký thành công.

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
| Related Requirements | FR-AUTH-02, FR-AUTH-03, FR-AUTH-08                            |

### Main Success Flow

1. Người dùng mở trang Login.
2. Hệ thống hiển thị Email và Password fields.
3. Người dùng nhập thông tin đăng nhập.
4. Người dùng gửi Login Form.
5. Hệ thống tìm tài khoản theo Email.
6. Hệ thống kiểm tra Password.
7. Hệ thống kiểm tra trạng thái tài khoản nếu cần.
8. Hệ thống tạo JWT.
9. Hệ thống trả về thông tin đăng nhập và role.
10. Frontend lưu trạng thái authentication.
11. Hệ thống chuyển người dùng đến trang phù hợp.

### Alternative / Exception Flows

| Step | Condition                     | System Response                                     |
| ---- | ----------------------------- | --------------------------------------------------- |
| 5    | Email không tồn tại           | Hiển thị thông báo thông tin đăng nhập không hợp lệ |
| 6    | Password sai                  | Từ chối đăng nhập                                   |
| 7    | Tài khoản chưa xác thực Email | Yêu cầu hoàn tất Email Verification                 |
| 7    | Tài khoản bị vô hiệu hóa      | Từ chối đăng nhập                                   |
| 8    | Lỗi tạo JWT                   | Hiển thị lỗi hệ thống                               |

---

## UC-AUTH-03 — Verify Email

### Description

Cho phép Customer xác thực Email sau khi đăng ký tài khoản.

| Item                 | Description                                               |
| -------------------- | --------------------------------------------------------- |
| Use Case ID          | UC-AUTH-03                                                |
| Use Case Name        | Verify Email                                              |
| Primary Actor        | Customer                                                  |
| Goal                 | Xác minh Email của tài khoản                              |
| Trigger              | Customer sử dụng Verification Link hoặc Verification Code |
| Preconditions        | Tài khoản đã được đăng ký nhưng chưa xác thực             |
| Postconditions       | Tài khoản được đánh dấu là Email Verified                 |
| Related Requirements | FR-AUTH-05                                                |

### Main Success Flow

1. Customer nhận Email Verification.
2. Customer mở Verification Link hoặc nhập Verification Code.
3. Hệ thống kiểm tra token/code.
4. Hệ thống kiểm tra token/code còn hiệu lực.
5. Hệ thống đánh dấu tài khoản đã xác thực.
6. Hệ thống hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition             | System Response                               |
| --------------------- | --------------------------------------------- |
| Token không hợp lệ    | Từ chối xác thực                              |
| Token hết hạn         | Yêu cầu gửi lại Verification Email            |
| Tài khoản đã xác thực | Hiển thị thông báo tài khoản đã được xác thực |

---

## UC-AUTH-04 — Forgot Password

### Description

Cho phép người dùng gửi yêu cầu hỗ trợ khi quên Password.

| Item                 | Description                                    |
| -------------------- | ---------------------------------------------- |
| Use Case ID          | UC-AUTH-04                                     |
| Use Case Name        | Forgot Password                                |
| Primary Actor        | Guest, Customer                                |
| Goal                 | Gửi yêu cầu khôi phục quyền truy cập tài khoản |
| Trigger              | Người dùng chọn Forgot Password                |
| Preconditions        | Người dùng không nhớ Password                  |
| Postconditions       | Yêu cầu hỗ trợ được ghi nhận                   |
| Related Requirements | FR-AUTH-06                                     |

### Main Success Flow

1. Người dùng chọn Forgot Password.
2. Hệ thống hiển thị biểu mẫu yêu cầu hỗ trợ.
3. Người dùng nhập Email.
4. Người dùng gửi yêu cầu.
5. Hệ thống kiểm tra dữ liệu.
6. Hệ thống ghi nhận yêu cầu.
7. Hệ thống hiển thị hướng dẫn tiếp theo.

### Alternative / Exception Flows

| Condition           | System Response                                             |
| ------------------- | ----------------------------------------------------------- |
| Email không hợp lệ  | Hiển thị lỗi validation                                     |
| Email không tồn tại | Hiển thị thông báo chung, không tiết lộ thông tin tài khoản |
| Lỗi hệ thống        | Hiển thị thông báo thử lại sau                              |

---

## UC-AUTH-05 — Authorize User

### Description

Kiểm tra quyền truy cập của người dùng trước khi cho phép thực hiện các chức năng được bảo vệ.

| Item                 | Description                                              |
| -------------------- | -------------------------------------------------------- |
| Use Case ID          | UC-AUTH-05                                               |
| Use Case Name        | Authorize User                                           |
| Primary Actor        | Customer, Admin                                          |
| Goal                 | Đảm bảo người dùng chỉ truy cập chức năng được cấp quyền |
| Trigger              | Người dùng gửi request đến protected API                 |
| Preconditions        | Request được gửi đến Backend                             |
| Postconditions       | Request được chấp nhận hoặc từ chối theo quyền           |
| Related Requirements | FR-AUTH-07, FR-API-02, FR-API-03, BR-22                  |

### Main Success Flow

1. Client gửi request kèm JWT.
2. Backend kiểm tra JWT.
3. Backend xác định User và Role.
4. Backend kiểm tra quyền truy cập resource.
5. Backend cho phép request tiếp tục nếu hợp lệ.

### Alternative / Exception Flows

| Condition                   | System Response           |
| --------------------------- | ------------------------- |
| Không có JWT                | Trả về `401 Unauthorized` |
| JWT không hợp lệ            | Trả về `401 Unauthorized` |
| User không có quyền         | Trả về `403 Forbidden`    |
| Customer truy cập Admin API | Từ chối request           |

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
3. Backend truy vấn các Product phù hợp.
4. Backend loại bỏ Product không được hiển thị công khai nếu cần.
5. Backend trả về danh sách Product.
6. Frontend hiển thị Product List.
7. Người dùng có thể chọn Product để xem chi tiết.

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
2. Frontend gửi request lấy Product Detail.
3. Backend tìm Product theo ID.
4. Backend kiểm tra Product có tồn tại và được phép hiển thị hay không.
5. Backend trả về Product Detail.
6. Frontend hiển thị thông tin Product.
7. Nếu Product còn hàng, hệ thống hiển thị Add to Cart.

### Alternative / Exception Flows

| Condition             | System Response        |
| --------------------- | ---------------------- |
| Product không tồn tại | Trả về `404 Not Found` |
| Product INACTIVE      | Không cho phép mua     |
| Product hết hàng      | Hiển thị Out of Stock  |
| API thất bại          | Hiển thị Error State   |

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

1. Người dùng nhập Keyword.
2. Frontend gửi Search Request.
3. Backend chuẩn hóa Keyword.
4. Backend tìm kiếm trong Product Name, SKU và Description.
5. Backend trả về kết quả.
6. Frontend hiển thị danh sách kết quả.

### Alternative / Exception Flows

| Condition        | System Response                                    |
| ---------------- | -------------------------------------------------- |
| Keyword rỗng     | Hiển thị toàn bộ Product hoặc yêu cầu nhập Keyword |
| Không có kết quả | Hiển thị `No products found`                       |
| Keyword quá dài  | Từ chối request hoặc giới hạn độ dài               |
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
2. Người dùng chọn Sort Option nếu cần.
3. Người dùng chọn Page hoặc chuyển sang trang tiếp theo.
4. Frontend gửi các tham số truy vấn.
5. Backend validate các tham số.
6. Backend áp dụng Filter, Sort và Pagination.
7. Backend trả về danh sách Product và thông tin phân trang.
8. Frontend cập nhật danh sách.

### Alternative / Exception Flows

| Condition              | System Response                       |
| ---------------------- | ------------------------------------- |
| Filter không hợp lệ    | Bỏ qua hoặc từ chối tham số           |
| Page vượt quá số trang | Trả về danh sách rỗng hoặc trang cuối |
| Không có kết quả       | Hiển thị Empty State                  |

---

# 7. Shopping Cart Use Cases

## UC-CART-01 — Add Product to Cart

### Description

Cho phép Guest hoặc Customer thêm Product còn khả dụng vào Cart.

| Item                 | Description                                                 |
| -------------------- | ----------------------------------------------------------- |
| Use Case ID          | UC-CART-01                                                  |
| Use Case Name        | Add Product to Cart                                         |
| Primary Actor        | Guest, Customer                                             |
| Goal                 | Thêm Product vào giỏ hàng                                   |
| Trigger              | Người dùng nhấn Add to Cart                                 |
| Preconditions        | Product tồn tại, Active và còn Stock                        |
| Postconditions       | Product được thêm vào Cart hoặc quantity hiện tại được tăng |
| Related Requirements | FR-CART-03, FR-CART-04, FR-CART-05, BR-08, BR-09            |

### Main Success Flow

1. Người dùng mở Product Detail.
2. Người dùng nhập Quantity.
3. Người dùng nhấn Add to Cart.
4. Hệ thống kiểm tra Product tồn tại.
5. Hệ thống kiểm tra Product có trạng thái Active.
6. Hệ thống kiểm tra Stock.
7. Hệ thống kiểm tra Product đã có trong Cart hay chưa.
8. Nếu chưa có, hệ thống tạo CartItem.
9. Nếu đã có, hệ thống tăng quantity của CartItem.
10. Hệ thống cập nhật Cart.
11. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition                                       | System Response              |
| ----------------------------------------------- | ---------------------------- |
| Product không tồn tại                           | Từ chối thao tác             |
| Product INACTIVE                                | Không cho phép thêm vào Cart |
| Product hết hàng                                | Hiển thị Out of Stock        |
| Quantity <= 0                                   | Hiển thị lỗi validation      |
| Quantity vượt Stock                             | Từ chối thao tác             |
| CartItem đã tồn tại và tổng quantity vượt Stock | Từ chối thao tác             |

---

## UC-CART-02 — View Cart

### Description

Cho phép Guest hoặc Customer xem các Product hiện có trong Cart.

| Item                 | Description                            |
| -------------------- | -------------------------------------- |
| Use Case ID          | UC-CART-02                             |
| Use Case Name        | View Cart                              |
| Primary Actor        | Guest, Customer                        |
| Goal                 | Xem và kiểm tra nội dung Cart          |
| Trigger              | Người dùng mở Cart                     |
| Preconditions        | Cart có thể được truy cập              |
| Postconditions       | Cart Items và Cart Total được hiển thị |
| Related Requirements | FR-CART-01, FR-CART-02, FR-UI-04       |

### Main Success Flow

1. Người dùng mở Cart.
2. Nếu là Guest, Frontend đọc Cart từ LocalStorage.
3. Nếu là Customer, Frontend gọi Cart API.
4. Hệ thống lấy thông tin Product liên quan.
5. Hệ thống kiểm tra Availability và Stock.
6. Frontend hiển thị Cart Items.
7. Frontend tính hoặc hiển thị Item Total và Cart Total.
8. Hệ thống hiển thị các thao tác Update Quantity và Remove.

### Alternative / Exception Flows

| Condition                    | System Response                  |
| ---------------------------- | -------------------------------- |
| Cart rỗng                    | Hiển thị Empty Cart State        |
| Product không còn tồn tại    | Đánh dấu CartItem không khả dụng |
| Product hết hàng             | Hiển thị Out of Stock            |
| Quantity vượt Stock hiện tại | Yêu cầu điều chỉnh Quantity      |

---

## UC-CART-03 — Update Cart Quantity

### Description

Cho phép người dùng thay đổi quantity của CartItem.

| Item                 | Description                            |
| -------------------- | -------------------------------------- |
| Use Case ID          | UC-CART-03                             |
| Use Case Name        | Update Cart Quantity                   |
| Primary Actor        | Guest, Customer                        |
| Goal                 | Điều chỉnh số lượng Product trong Cart |
| Trigger              | Người dùng tăng hoặc giảm Quantity     |
| Preconditions        | CartItem tồn tại                       |
| Postconditions       | Quantity được cập nhật nếu hợp lệ      |
| Related Requirements | FR-CART-06, FR-CART-07, BR-10          |

### Main Success Flow

1. Người dùng chọn CartItem.
2. Người dùng thay đổi Quantity.
3. Frontend gửi yêu cầu cập nhật.
4. Hệ thống kiểm tra Quantity > 0.
5. Hệ thống kiểm tra Product còn Active.
6. Hệ thống kiểm tra Quantity không vượt Stock.
7. Hệ thống cập nhật CartItem.
8. Hệ thống cập nhật Cart Total.
9. Frontend hiển thị kết quả.

### Alternative / Exception Flows

| Condition              | System Response                                  |
| ---------------------- | ------------------------------------------------ |
| Quantity <= 0          | Yêu cầu Remove CartItem hoặc nhập giá trị hợp lệ |
| Quantity vượt Stock    | Từ chối cập nhật                                 |
| Product INACTIVE       | Đánh dấu không khả dụng                          |
| CartItem không tồn tại | Trả về `404 Not Found`                           |

---

## UC-CART-04 — Remove Cart Item

### Description

Cho phép người dùng xóa Product khỏi Cart.

| Item                 | Description                     |
| -------------------- | ------------------------------- |
| Use Case ID          | UC-CART-04                      |
| Use Case Name        | Remove Cart Item                |
| Primary Actor        | Guest, Customer                 |
| Goal                 | Xóa CartItem không còn muốn mua |
| Trigger              | Người dùng nhấn Remove          |
| Preconditions        | CartItem tồn tại                |
| Postconditions       | CartItem bị xóa khỏi Cart       |
| Related Requirements | FR-CART-08, FR-UI-04            |

### Main Success Flow

1. Người dùng mở Cart.
2. Người dùng chọn Remove trên CartItem.
3. Hệ thống yêu cầu xác nhận nếu UI có hỗ trợ.
4. Người dùng xác nhận.
5. Hệ thống xóa CartItem.
6. Hệ thống cập nhật Cart Total.
7. Frontend hiển thị Cart mới.

### Alternative / Exception Flows

| Condition               | System Response             |
| ----------------------- | --------------------------- |
| Người dùng hủy xác nhận | Không thay đổi Cart         |
| CartItem không tồn tại  | Cập nhật lại Cart hiện tại  |
| Lỗi lưu dữ liệu         | Hiển thị Error Notification |

---

## UC-CART-05 — Merge Guest Cart

### Description

Đồng bộ Cart được lưu trong LocalStorage với Cart của Customer sau khi Guest đăng nhập.

| Item                 | Description                                     |
| -------------------- | ----------------------------------------------- |
| Use Case ID          | UC-CART-05                                      |
| Use Case Name        | Merge Guest Cart                                |
| Primary Actor        | Customer                                        |
| Goal                 | Giữ lại các Product đã thêm trước khi đăng nhập |
| Trigger              | Guest đăng nhập thành công                      |
| Preconditions        | Guest có Cart trong LocalStorage                |
| Postconditions       | Guest Cart được merge vào Customer Cart         |
| Related Requirements | FR-CART-09, FR-CART-01, FR-CART-02              |

### Main Success Flow

1. Guest có Product trong LocalStorage Cart.
2. Guest thực hiện Login.
3. Login thành công.
4. Frontend đọc LocalStorage Cart.
5. Frontend gửi Cart Data đến Backend.
6. Backend lấy Customer Cart.
7. Backend kiểm tra từng CartItem.
8. Nếu Product chưa tồn tại, Backend tạo CartItem mới.
9. Nếu Product đã tồn tại, Backend cộng dồn Quantity.
10. Backend kiểm tra Stock.
11. Backend lưu Customer Cart.
12. Frontend xóa hoặc đánh dấu LocalStorage Cart đã được đồng bộ.

### Alternative / Exception Flows

| Condition              | System Response                                    |
| ---------------------- | -------------------------------------------------- |
| LocalStorage Cart rỗng | Không cần merge                                    |
| Product đã INACTIVE    | Không merge Product đó và thông báo                |
| Quantity vượt Stock    | Giới hạn Quantity hoặc yêu cầu Customer điều chỉnh |
| Cart Merge thất bại    | Không xóa LocalStorage Cart và hiển thị lỗi        |

---

# 8. Checkout & Payment Use Cases

## UC-CHECKOUT-01 — Checkout

### Description

Cho phép Customer kiểm tra Cart, nhập thông tin giao hàng và tạo yêu cầu đặt hàng.

| Item                 | Description                                   |
| -------------------- | --------------------------------------------- |
| Use Case ID          | UC-CHECKOUT-01                                |
| Use Case Name        | Checkout                                      |
| Primary Actor        | Customer                                      |
| Goal                 | Hoàn tất thông tin cần thiết để đặt hàng      |
| Trigger              | Customer chọn Checkout                        |
| Preconditions        | Customer đã đăng nhập và Cart không rỗng      |
| Postconditions       | Dữ liệu Checkout hợp lệ và sẵn sàng tạo Order |
| Related Requirements | FR-CHECKOUT-01 đến FR-CHECKOUT-09             |

### Main Success Flow

1. Customer mở Cart.
2. Customer chọn Checkout.
3. Hệ thống kiểm tra Customer đã đăng nhập.
4. Hệ thống kiểm tra Cart không rỗng.
5. Hệ thống kiểm tra Product còn Active.
6. Hệ thống kiểm tra Stock của từng CartItem.
7. Hệ thống hiển thị Checkout Form.
8. Customer nhập:
   - Full Name
   - Phone Number
   - Address
   - Province/City
   - District/Ward
9. Customer chọn Payment Method.
10. Hệ thống validate Shipping Information.
11. Hệ thống tính Order Total.
12. Hệ thống hiển thị Order Summary.
13. Customer xác nhận Place Order.
14. Hệ thống chuyển sang UC-ORDER-01 — Create Order.

### Alternative / Exception Flows

| Condition                         | System Response                                |
| --------------------------------- | ---------------------------------------------- |
| Customer chưa đăng nhập           | Chuyển đến Login                               |
| Cart rỗng                         | Không cho phép Checkout                        |
| Product hết hàng                  | Yêu cầu cập nhật Cart                          |
| Product INACTIVE                  | Đánh dấu không khả dụng                        |
| Shipping Information thiếu        | Hiển thị lỗi validation                        |
| Stock thay đổi trong lúc Checkout | Từ chối tạo Order và yêu cầu kiểm tra lại Cart |
| Cart chứa Product không khả dụng  | Chặn Checkout                                  |

---

## UC-CHECKOUT-02 — Select Payment Method

### Description

Cho phép Customer lựa chọn phương thức thanh toán cho Order.

| Item                 | Description                                         |
| -------------------- | --------------------------------------------------- |
| Use Case ID          | UC-CHECKOUT-02                                      |
| Use Case Name        | Select Payment Method                               |
| Primary Actor        | Customer                                            |
| Goal                 | Xác định phương thức thanh toán                     |
| Trigger              | Customer mở Payment Method Selection                |
| Preconditions        | Customer đang ở Checkout                            |
| Postconditions       | Payment Method được chọn và lưu trong Checkout Data |
| Related Requirements | FR-PAY-01, FR-PAY-02, FR-PAY-04                     |

### Main Success Flow

1. Hệ thống hiển thị các Payment Methods được hỗ trợ.
2. Customer chọn một phương thức.
3. Hệ thống kiểm tra phương thức có được hỗ trợ hay không.
4. Hệ thống lưu lựa chọn.
5. Hệ thống hiển thị Payment Method trong Order Summary.

### Payment Methods

- COD
- Bank Transfer
- E-wallet
- Online Payment Gateway

### Alternative / Exception Flows

| Condition                               | System Response                  |
| --------------------------------------- | -------------------------------- |
| Payment Method không được hỗ trợ        | Từ chối lựa chọn                 |
| Payment Gateway tạm thời không khả dụng | Yêu cầu chọn phương thức khác    |
| Customer không chọn phương thức         | Sử dụng COD mặc định nếu phù hợp |

---

## UC-PAY-01 — Process Online Payment

### Description

Xử lý thanh toán thông qua Payment Gateway bên ngoài khi Customer chọn phương thức thanh toán trực tuyến.

| Item                 | Description                                         |
| -------------------- | --------------------------------------------------- |
| Use Case ID          | UC-PAY-01                                           |
| Use Case Name        | Process Online Payment                              |
| Primary Actor        | Customer                                            |
| Supporting Actor     | Payment Gateway                                     |
| Goal                 | Hoàn tất thanh toán trực tuyến                      |
| Trigger              | Customer xác nhận thanh toán online                 |
| Preconditions        | Checkout hợp lệ và Payment Gateway khả dụng         |
| Postconditions       | Payment Status được cập nhật theo kết quả giao dịch |
| Related Requirements | FR-PAY-02, FR-PAY-03                                |

### Main Success Flow

1. Customer chọn Online Payment Gateway.
2. Hệ thống tạo payment request.
3. Hệ thống chuyển Customer đến Payment Gateway hoặc hiển thị Payment UI.
4. Customer thực hiện thanh toán.
5. Payment Gateway xử lý giao dịch.
6. Payment Gateway trả về kết quả.
7. Hệ thống xác thực kết quả trả về.
8. Hệ thống cập nhật Payment Status.
9. Hệ thống hiển thị kết quả thanh toán.

### Alternative / Exception Flows

| Condition                | System Response                                             |
| ------------------------ | ----------------------------------------------------------- |
| Thanh toán thất bại      | Payment Status giữ ở trạng thái chưa thanh toán hoặc Failed |
| Customer hủy thanh toán  | Quay lại Checkout hoặc hiển thị thông báo                   |
| Payment Gateway timeout  | Hiển thị trạng thái đang xử lý hoặc yêu cầu thử lại         |
| Callback không hợp lệ    | Không cập nhật Payment Status                               |
| Giao dịch bị xử lý trùng | Không ghi nhận thanh toán nhiều lần                         |

---

# 9. Order Management Use Cases

## UC-ORDER-01 — Create Order

### Description

Tạo Order mới sau khi Customer hoàn tất Checkout hợp lệ.

| Item                 | Description                                                                |
| -------------------- | -------------------------------------------------------------------------- |
| Use Case ID          | UC-ORDER-01                                                                |
| Use Case Name        | Create Order                                                               |
| Primary Actor        | Customer                                                                   |
| Goal                 | Tạo Order và cập nhật Stock                                                |
| Trigger              | Customer nhấn Place Order                                                  |
| Preconditions        | Customer đã đăng nhập, Cart hợp lệ và Stock đủ                             |
| Postconditions       | Order được tạo, OrderItems được lưu, Stock được giảm và Cart được cập nhật |
| Related Requirements | FR-CHECKOUT-05 đến FR-CHECKOUT-09, FR-ORDER-01 đến FR-ORDER-04             |

### Main Success Flow

1. Customer nhấn Place Order.
2. Backend xác thực JWT.
3. Backend kiểm tra Customer ownership đối với Cart.
4. Backend kiểm tra Cart không rỗng.
5. Backend kiểm tra Product Status.
6. Backend kiểm tra Stock hiện tại.
7. Backend tính giá từng OrderItem.
8. Backend tính Order Total.
9. Backend tạo Order với trạng thái `PENDING`.
10. Backend tạo các OrderItems.
11. Backend lưu giá Product tại thời điểm mua vào OrderItem.
12. Backend giảm Stock tương ứng.
13. Backend cập nhật hoặc xóa CartItems đã đặt hàng.
14. Backend commit transaction.
15. Frontend hiển thị Order Confirmation.

### Transaction Requirements

Các thao tác sau phải được thực hiện trong cùng một transaction:

```text
Validate Cart
    ↓
Validate Stock
    ↓
Create Order
    ↓
Create OrderItems
    ↓
Deduct Stock
    ↓
Clear/Update Cart
    ↓
Commit Transaction
```

### Alternative / Exception Flows

| Condition                              | System Response                    |
| -------------------------------------- | ---------------------------------- |
| Customer chưa đăng nhập                | Trả về `401 Unauthorized`          |
| Cart rỗng                              | Từ chối tạo Order                  |
| Product không tồn tại                  | Rollback transaction               |
| Product INACTIVE                       | Rollback transaction               |
| Stock không đủ                         | Rollback transaction và thông báo  |
| Lỗi tạo OrderItem                      | Rollback transaction               |
| Lỗi giảm Stock                         | Rollback transaction               |
| Concurrent Checkout làm Stock không đủ | Từ chối request và không tạo Order |

---

## UC-ORDER-02 — View Customer Order History

### Description

Cho phép Customer xem danh sách các Order của chính mình.

| Item                 | Description                                |
| -------------------- | ------------------------------------------ |
| Use Case ID          | UC-ORDER-02                                |
| Use Case Name        | View Customer Order History                |
| Primary Actor        | Customer                                   |
| Goal                 | Theo dõi các Order đã tạo                  |
| Trigger              | Customer mở Order History                  |
| Preconditions        | Customer đã đăng nhập                      |
| Postconditions       | Danh sách Order của Customer được hiển thị |
| Related Requirements | FR-ORDER-11, FR-ORDER-13                   |

### Main Success Flow

1. Customer mở Order History.
2. Frontend gửi request đến Backend.
3. Backend xác thực Customer.
4. Backend truy vấn các Order thuộc Customer hiện tại.
5. Customer có thể filter theo Order Status.
6. Backend trả về danh sách Order.
7. Frontend hiển thị Order History.

### Alternative / Exception Flows

| Condition               | System Response                                    |
| ----------------------- | -------------------------------------------------- |
| Customer chưa đăng nhập | Trả về `401 Unauthorized`                          |
| Không có Order          | Hiển thị Empty State                               |
| Filter không hợp lệ     | Trả về lỗi validation hoặc sử dụng filter mặc định |

---

## UC-ORDER-03 — View Order Detail

### Description

Cho phép Customer xem chi tiết một Order thuộc về mình.

| Item                 | Description                                      |
| -------------------- | ------------------------------------------------ |
| Use Case ID          | UC-ORDER-03                                      |
| Use Case Name        | View Order Detail                                |
| Primary Actor        | Customer                                         |
| Goal                 | Xem thông tin chi tiết Order                     |
| Trigger              | Customer chọn một Order                          |
| Preconditions        | Customer đã đăng nhập                            |
| Postconditions       | Order Detail được hiển thị nếu Customer có quyền |
| Related Requirements | FR-ORDER-12, FR-ORDER-13, FR-API-04, BR-17       |

### Main Success Flow

1. Customer chọn Order từ Order History.
2. Frontend gửi Order ID đến Backend.
3. Backend xác thực Customer.
4. Backend tìm Order theo ID.
5. Backend kiểm tra Order thuộc Customer hiện tại.
6. Backend lấy OrderItems và thông tin liên quan.
7. Backend trả về Order Detail.
8. Frontend hiển thị Order Detail.

### Alternative / Exception Flows

| Condition                 | System Response           |
| ------------------------- | ------------------------- |
| Order không tồn tại       | Trả về `404 Not Found`    |
| Order thuộc Customer khác | Từ chối truy cập          |
| Customer chưa đăng nhập   | Trả về `401 Unauthorized` |

---

## UC-ORDER-04 — Cancel Pending Order

### Description

Cho phép Customer hủy Order khi Order đang ở trạng thái `PENDING`.

| Item                 | Description                                            |
| -------------------- | ------------------------------------------------------ |
| Use Case ID          | UC-ORDER-04                                            |
| Use Case Name        | Cancel Pending Order                                   |
| Primary Actor        | Customer                                               |
| Goal                 | Hủy Order chưa được xác nhận                           |
| Trigger              | Customer nhấn Cancel Order                             |
| Preconditions        | Customer đã đăng nhập và Order thuộc Customer hiện tại |
| Postconditions       | Order chuyển sang `CANCELLED` và Stock được hoàn lại   |
| Related Requirements | FR-ORDER-07, FR-ORDER-10, BR-13, BR-16                 |

### Main Success Flow

1. Customer mở Order Detail.
2. Hệ thống kiểm tra Order Status.
3. Nếu Order đang ở `PENDING`, hệ thống hiển thị Cancel action.
4. Customer nhấn Cancel Order.
5. Hệ thống yêu cầu xác nhận.
6. Customer xác nhận.
7. Backend kiểm tra Order ownership.
8. Backend kiểm tra Order Status vẫn là `PENDING`.
9. Backend chuyển Order sang `CANCELLED`.
10. Backend hoàn lại Stock của từng OrderItem.
11. Backend đảm bảo Stock chỉ được hoàn một lần.
12. Backend commit transaction.
13. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition                            | System Response              |
| ------------------------------------ | ---------------------------- |
| Order không thuộc Customer           | Từ chối truy cập             |
| Order đã chuyển sang trạng thái khác | Không cho phép Cancel        |
| Customer hủy xác nhận                | Không thay đổi Order         |
| Stock Restoration thất bại           | Rollback thay đổi Order      |
| Order đã được hoàn Stock trước đó    | Không hoàn Stock lần thứ hai |

---

## UC-ORDER-05 — Update Order Status

### Description

Cho phép Admin cập nhật trạng thái Order theo transition hợp lệ.

| Item                 | Description                                        |
| -------------------- | -------------------------------------------------- |
| Use Case ID          | UC-ORDER-05                                        |
| Use Case Name        | Update Order Status                                |
| Primary Actor        | Admin                                              |
| Goal                 | Cập nhật tiến trình xử lý Order                    |
| Trigger              | Admin thay đổi Order Status                        |
| Preconditions        | Admin đã đăng nhập và có quyền quản lý Order       |
| Postconditions       | Order Status được cập nhật nếu transition hợp lệ   |
| Related Requirements | FR-ORDER-08, FR-ORDER-09, FR-ADMIN-ORDER-05, BR-19 |

### Main Success Flow

1. Admin mở Order Management.
2. Admin chọn một Order.
3. Admin mở Order Detail.
4. Admin chọn Status mới.
5. Backend xác thực Admin role.
6. Backend kiểm tra Order tồn tại.
7. Backend kiểm tra transition có hợp lệ.
8. Backend cập nhật Order Status.
9. Backend lưu Updated Date.
10. Frontend hiển thị trạng thái mới.

### Supported Statuses

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

### Main Status Transition

```text
PENDING
   ↓
CONFIRMED
   ↓
SHIPPING
   ↓
DELIVERED
```

Alternative cancellation flow:

```text
PENDING
   ↓
CANCELLED
```

### Alternative / Exception Flows

| Condition                                          | System Response           |
| -------------------------------------------------- | ------------------------- |
| Admin chưa đăng nhập                               | Trả về `401 Unauthorized` |
| User không có Admin role                           | Trả về `403 Forbidden`    |
| Order không tồn tại                                | Trả về `404 Not Found`    |
| Transition không hợp lệ                            | Từ chối cập nhật          |
| Admin cố sửa Product/Quantity/Shipping Information | Không cho phép thao tác   |

---

# 10. Admin Product Management Use Cases

## UC-ADMIN-PROD-01 — Manage Products

### Description

Cho phép Admin xem, tìm kiếm, tạo, cập nhật, kích hoạt, vô hiệu hóa và Soft Delete Product.

| Item                 | Description                                  |
| -------------------- | -------------------------------------------- |
| Use Case ID          | UC-ADMIN-PROD-01                             |
| Use Case Name        | Manage Products                              |
| Primary Actor        | Admin                                        |
| Goal                 | Quản lý Product Catalog                      |
| Trigger              | Admin mở Product Management                  |
| Preconditions        | Admin đã đăng nhập và có quyền               |
| Postconditions       | Product được xem hoặc cập nhật theo thao tác |
| Related Requirements | FR-ADMIN-PROD-01 đến FR-ADMIN-PROD-07        |

### Supported Operations

- View Products
- Search Products
- Create Product
- Update Product
- Update Stock
- Activate Product
- Deactivate Product
- Soft Delete Product

### Main Success Flow — Create Product

1. Admin mở Product Management.
2. Admin chọn Create Product.
3. Hệ thống hiển thị Product Form.
4. Admin nhập:
   - SKU
   - Product Name
   - Description
   - Price
   - Stock Quantity
   - Image URL
   - Category
   - Status
5. Admin gửi Form.
6. Backend validate dữ liệu.
7. Backend kiểm tra SKU uniqueness.
8. Backend kiểm tra Category tồn tại.
9. Backend lưu Product.
10. Frontend hiển thị thông báo thành công.

### Main Success Flow — Update Product

1. Admin chọn Product.
2. Admin chọn Edit.
3. Hệ thống hiển thị dữ liệu hiện tại.
4. Admin cập nhật thông tin.
5. Admin gửi Form.
6. Backend validate dữ liệu.
7. Backend cập nhật Product.
8. Frontend hiển thị dữ liệu mới.

### Main Success Flow — Soft Delete Product

1. Admin chọn Product.
2. Admin nhấn Delete.
3. Hệ thống yêu cầu xác nhận.
4. Admin xác nhận.
5. Backend kiểm tra quyền.
6. Backend chuyển Product sang `INACTIVE` hoặc áp dụng cơ chế Soft Delete.
7. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition                              | System Response         |
| -------------------------------------- | ----------------------- |
| SKU đã tồn tại                         | Từ chối lưu Product     |
| Price <= 0                             | Hiển thị lỗi validation |
| Stock < 0                              | Hiển thị lỗi validation |
| Category không tồn tại                 | Từ chối lưu Product     |
| Product đang được tham chiếu bởi Order | Không hard-delete       |
| Admin không có quyền                   | Trả về `403 Forbidden`  |

---

# 11. Admin Category Management Use Cases

## UC-ADMIN-CAT-01 — Manage Categories

### Description

Cho phép Admin xem, tạo, cập nhật và xóa Category theo business rules.

| Item                 | Description                                                  |
| -------------------- | ------------------------------------------------------------ |
| Use Case ID          | UC-ADMIN-CAT-01                                              |
| Use Case Name        | Manage Categories                                            |
| Primary Actor        | Admin                                                        |
| Goal                 | Quản lý danh mục Product                                     |
| Trigger              | Admin mở Category Management                                 |
| Preconditions        | Admin đã đăng nhập và có quyền                               |
| Postconditions       | Category được tạo, cập nhật hoặc xóa nếu hợp lệ              |
| Related Requirements | FR-CAT-01 đến FR-CAT-06, FR-ADMIN-CAT-01 đến FR-ADMIN-CAT-04 |

### Supported Operations

- View Categories
- Create Category
- Update Category
- Delete Category

### Main Success Flow — Create Category

1. Admin mở Category Management.
2. Admin chọn Create Category.
3. Admin nhập Category Name, Description và Status.
4. Admin gửi Form.
5. Backend validate dữ liệu.
6. Backend lưu Category.
7. Frontend hiển thị Category mới.

### Main Success Flow — Delete Category

1. Admin chọn Category.
2. Admin nhấn Delete.
3. Backend kiểm tra Category có Product tham chiếu hay không.
4. Nếu không có Product tham chiếu, Backend thực hiện Delete.
5. Frontend hiển thị thông báo thành công.

### Alternative / Exception Flows

| Condition                       | System Response         |
| ------------------------------- | ----------------------- |
| Category không tồn tại          | Trả về `404 Not Found`  |
| Category còn Product tham chiếu | Block Delete            |
| Category Name không hợp lệ      | Hiển thị lỗi validation |
| Admin không có quyền            | Trả về `403 Forbidden`  |

---

# 12. Admin Inventory Management Use Cases

## UC-ADMIN-INV-01 — Manage Inventory

### Description

Cho phép Admin xem và cập nhật Stock Quantity của Product.

| Item                 | Description                                   |
| -------------------- | --------------------------------------------- |
| Use Case ID          | UC-ADMIN-INV-01                               |
| Use Case Name        | Manage Inventory                              |
| Primary Actor        | Admin                                         |
| Goal                 | Duy trì số lượng tồn kho chính xác            |
| Trigger              | Admin cập nhật Stock của Product              |
| Preconditions        | Admin đã đăng nhập và Product tồn tại         |
| Postconditions       | Stock Quantity được cập nhật hợp lệ           |
| Related Requirements | FR-ADMIN-PROD-05, FR-CHECKOUT-07, FR-ORDER-10 |

### Main Success Flow

1. Admin mở Product Management hoặc Inventory Management.
2. Admin chọn Product.
3. Admin xem Stock Quantity hiện tại.
4. Admin nhập Stock Quantity mới.
5. Backend kiểm tra Admin role.
6. Backend kiểm tra Stock không âm.
7. Backend cập nhật Stock Quantity.
8. Backend cập nhật Product Status nếu nghiệp vụ yêu cầu.
9. Frontend hiển thị Stock mới.

### Alternative / Exception Flows

| Condition             | System Response        |
| --------------------- | ---------------------- |
| Stock < 0             | Từ chối cập nhật       |
| Product không tồn tại | Trả về `404 Not Found` |
| Admin không có quyền  | Trả về `403 Forbidden` |
| Lỗi cập nhật database | Không thay đổi Stock   |

---

# 13. Admin Order Management Use Cases

## UC-ADMIN-ORDER-01 — Manage Orders

### Description

Cho phép Admin xem, tìm kiếm, lọc và cập nhật các Order trong hệ thống.

| Item                 | Description                                                             |
| -------------------- | ----------------------------------------------------------------------- |
| Use Case ID          | UC-ADMIN-ORDER-01                                                       |
| Use Case Name        | Manage Orders                                                           |
| Primary Actor        | Admin                                                                   |
| Goal                 | Theo dõi và xử lý toàn bộ Order                                         |
| Trigger              | Admin mở Order Management                                               |
| Preconditions        | Admin đã đăng nhập và có quyền                                          |
| Postconditions       | Order List hoặc Order Detail được hiển thị; Status có thể được cập nhật |
| Related Requirements | FR-ADMIN-ORDER-01 đến FR-ADMIN-ORDER-05                                 |

### Supported Operations

- View All Orders
- Search Orders
- Filter Orders
- View Order Detail
- Update Order Status

### Main Success Flow — View and Search Orders

1. Admin mở Order Management.
2. Hệ thống hiển thị danh sách Order.
3. Admin nhập Search Keyword nếu cần.
4. Admin chọn Status Filter nếu cần.
5. Frontend gửi request đến Backend.
6. Backend truy vấn Order theo điều kiện.
7. Backend trả về kết quả.
8. Frontend hiển thị Order List.

### Main Success Flow — View Order Detail

1. Admin chọn một Order.
2. Frontend gửi Order ID.
3. Backend kiểm tra Admin role.
4. Backend lấy Order và OrderItems.
5. Backend trả về thông tin chi tiết.
6. Frontend hiển thị Order Detail.

### Main Success Flow — Update Order Status

1. Admin mở Order Detail.
2. Admin chọn Status mới.
3. Backend kiểm tra transition.
4. Backend cập nhật Status.
5. Frontend hiển thị kết quả.

### Alternative / Exception Flows

| Condition                      | System Response                  |
| ------------------------------ | -------------------------------- |
| Không có Order phù hợp         | Hiển thị Empty State             |
| Order không tồn tại            | Trả về `404 Not Found`           |
| Search Keyword không hợp lệ    | Hiển thị lỗi hoặc bỏ qua tham số |
| Status Transition không hợp lệ | Từ chối cập nhật                 |
| Admin không có quyền           | Trả về `403 Forbidden`           |

---

# 14. Cross-Cutting Use Case Rules

## 14.1. Authentication Rule

Các Use Case sau yêu cầu Customer hoặc Admin phải đăng nhập:

- Checkout
- Create Order
- View Customer Order History
- View Customer Order Detail
- Cancel Pending Order
- Manage Products
- Manage Categories
- Manage Inventory
- Manage Orders

---

## 14.2. Authorization Rule

Backend phải kiểm tra Role ở server-side.

Không được chỉ dựa vào việc ẩn hoặc hiển thị menu trên Frontend.

| Operation                | Required Role |
| ------------------------ | ------------- |
| Checkout                 | CUSTOMER      |
| View Own Orders          | CUSTOMER      |
| Cancel Own Pending Order | CUSTOMER      |
| Manage Products          | ADMIN         |
| Manage Categories        | ADMIN         |
| Manage Inventory         | ADMIN         |
| Manage All Orders        | ADMIN         |
| Update Order Status      | ADMIN         |

---

## 14.3. Ownership Rule

Customer chỉ được truy cập các resource thuộc về chính mình.

Ví dụ:

- Customer chỉ xem được Order của bản thân.
- Customer không được xem Order của Customer khác.
- Customer không được cập nhật Order của Customer khác.

---

## 14.4. Stock Rule

Các nghiệp vụ liên quan đến Stock phải tuân thủ:

1. Stock không được âm.
2. Product hết Stock không được mua.
3. Cart Quantity không được vượt Stock.
4. Backend phải kiểm tra Stock tại thời điểm Checkout.
5. Checkout phải cập nhật Stock trong transaction.
6. Cancel Order phải hoàn Stock.
7. Stock chỉ được hoàn một lần cho mỗi Order.

---

## 14.5. Order Status Rule

Order Status hợp lệ:

```text
PENDING
CONFIRMED
SHIPPING
DELIVERED
CANCELLED
```

Transition cơ bản:

| Current Status | Allowed Next Status           |
| -------------- | ----------------------------- |
| PENDING        | CONFIRMED, CANCELLED          |
| CONFIRMED      | SHIPPING                      |
| SHIPPING       | DELIVERED                     |
| DELIVERED      | Không có transition trong MVP |
| CANCELLED      | Không có transition trong MVP |

---

# 15. Use Case Summary

| Use Case ID       | Use Case Name                      | Primary Actor             |
| ----------------- | ---------------------------------- | ------------------------- |
| UC-AUTH-01        | Register Customer                  | Guest                     |
| UC-AUTH-02        | Login                              | Guest, Customer, Admin    |
| UC-AUTH-03        | Verify Email                       | Customer                  |
| UC-AUTH-04        | Forgot Password                    | Guest, Customer           |
| UC-AUTH-05        | Authorize User                     | Customer, Admin           |
| UC-PROD-01        | View Product List                  | Guest, Customer, Admin    |
| UC-PROD-02        | View Product Detail                | Guest, Customer, Admin    |
| UC-PROD-03        | Search Product                     | Guest, Customer, Admin    |
| UC-PROD-04        | Filter, Sort and Paginate Products | Guest, Customer, Admin    |
| UC-CART-01        | Add Product to Cart                | Guest, Customer           |
| UC-CART-02        | View Cart                          | Guest, Customer           |
| UC-CART-03        | Update Cart Quantity               | Guest, Customer           |
| UC-CART-04        | Remove Cart Item                   | Guest, Customer           |
| UC-CART-05        | Merge Guest Cart                   | Customer                  |
| UC-CHECKOUT-01    | Checkout                           | Customer                  |
| UC-CHECKOUT-02    | Select Payment Method              | Customer                  |
| UC-PAY-01         | Process Online Payment             | Customer, Payment Gateway |
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
| FR-AUTH                      | UC-AUTH-01, UC-AUTH-02, UC-AUTH-03, UC-AUTH-04, UC-AUTH-05      |
| FR-PROD                      | UC-PROD-01, UC-PROD-02, UC-PROD-03, UC-PROD-04                  |
| FR-CAT                       | UC-ADMIN-CAT-01                                                 |
| FR-CART                      | UC-CART-01, UC-CART-02, UC-CART-03, UC-CART-04, UC-CART-05      |
| FR-CHECKOUT                  | UC-CHECKOUT-01, UC-ORDER-01                                     |
| FR-PAY                       | UC-CHECKOUT-02, UC-PAY-01                                       |
| FR-ORDER                     | UC-ORDER-01, UC-ORDER-02, UC-ORDER-03, UC-ORDER-04, UC-ORDER-05 |
| FR-ADMIN-PROD                | UC-ADMIN-PROD-01                                                |
| FR-ADMIN-CAT                 | UC-ADMIN-CAT-01                                                 |
| FR-ADMIN-ORDER               | UC-ADMIN-ORDER-01                                               |
| FR-ADMIN-INV                 | UC-ADMIN-INV-01                                                 |
| FR-API                       | UC-AUTH-05 và các Use Case yêu cầu Backend API                  |

---

# 17. Out of Scope

Các Use Case sau không thuộc phạm vi MVP:

- Coupon / Promotion Management.
- Multiple Shipping Addresses.
- Multiple Shipping Methods.
- Category Hierarchy Management.
- Product Reviews.
- Product Recommendations.
- Advanced Analytics.
- Admin Auditing.
- Advanced Customer Support System.
- Advanced Payment Reconciliation.
- Automated Delivery Tracking.
- Advanced Notification Center.

---

# 18. Open Design Decisions

Các vấn đề sau cần được quyết định trong giai đoạn Design hoặc API Specification:

| ID    | Open Decision                                       | Related Use Cases                      |
| ----- | --------------------------------------------------- | -------------------------------------- |
| OD-01 | Cơ chế Email Verification cụ thể                    | UC-AUTH-03                             |
| OD-02 | Cơ chế Forgot Password                              | UC-AUTH-04                             |
| OD-03 | Payment Gateway cụ thể                              | UC-PAY-01                              |
| OD-04 | Xác định thời điểm tạo Order đối với Online Payment | UC-CHECKOUT-01, UC-PAY-01, UC-ORDER-01 |
| OD-05 | Chi tiết Stock Locking/Concurrency Control          | UC-ORDER-01                            |
| OD-06 | Chọn `status` hoặc `deleted_at` cho Soft Delete     | UC-ADMIN-PROD-01                       |
| OD-07 | Cơ chế lưu Cart Merge                               | UC-CART-05                             |
| OD-08 | Chi tiết Error Response Schema                      | UC-AUTH-05 và các API Use Case         |
| OD-09 | Cơ chế xử lý Payment Callback                       | UC-PAY-01                              |
| OD-10 | Chi tiết Order Status Transition Validation         | UC-ORDER-05                            |

---

# 19. Conclusion

Các Use Case bao phủ những nghiệp vụ chính:

1. Authentication & Account
2. Product Browsing
3. Shopping Cart
4. Checkout
5. Payment
6. Order Management
7. Product Management
8. Category Management
9. Inventory Management
10. Admin Order Management

Use Case được sử dụng làm cơ sở để phát triển:

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
    ↓
Implementation
    ↓
Test Cases
```

Trước khi chuyển sang thiết kế ERD, cần bảo đảm các Use Case quan trọng nhất đã được review, đặc biệt là:

- UC-CART-05 — Merge Guest Cart
- UC-CHECKOUT-01 — Checkout
- UC-ORDER-01 — Create Order
- UC-ORDER-04 — Cancel Pending Order
- UC-ORDER-05 — Update Order Status
- UC-ADMIN-PROD-01 — Manage Products
- UC-ADMIN-ORDER-01 — Manage Orders
