# Kế hoạch Tuần 3: Backend — Nghiệp vụ chính

| Item                    | Description                                                                                                                                                                                                                                                                      |
|-------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Người lập               | Nguyễn Minh Quân                                                                                                                                                                                                                                                                 |
| Ngày cập nhật           | 25/11/2026                                                                                                                                                                                                                                                                       |
| Căn cứ                  | `requirement-specification.md`, `api-specification.md`, `entity-relationship-diagram.md`, `class-diagram.md`, `coding-convention.md`, `git-convention.md`, `environment-setup.md`, `uoc-luong-dau-viec-tuan-2-6.md`, Kế hoạch Tuần 2, Review Tuần 3 lần 1 và Review Tuần 3 lần 2 |
| Feedback được đối chiếu | K-01 đến K-08                                                                                                                                                                                                                                                                    |
| Quyết định áp dụng      | D-11, D-12, D-13                                                                                                                                                                                                                                                                 |

------------------------------------------------------------------------

## 1. Phạm vi Tuần 3

### 1.1. Mục tiêu

Hoàn thiện các nghiệp vụ Backend cốt lõi để Frontend có thể bắt đầu tích
hợp từ Tuần 4:

- Authentication sử dụng Spring Security và JWT.
- Authorization theo hai vai trò ADMIN và CUSTOMER.
- Cart: lấy/tạo giỏ hàng, thêm sản phẩm, cập nhật số lượng, xóa sản phẩm
  và kiểm tra tính hợp lệ.
- Checkout: tạo Order và OrderItem, tính tổng tiền, kiểm tra thông tin
  giao hàng.
- Inventory Transaction: trừ tồn kho an toàn trong transaction khi
  Checkout.
- Customer Order: xem danh sách và chi tiết Order của Customer.
- Public Product Search: tìm kiếm, lọc, sắp xếp và phân trang.
- Kiểm thử Unit Test và API Test cho các nghiệp vụ đã triển khai.
- Các nghiệp vụ Cancel Order, Edge Cases và Unit Test tổng hợp được
  chuyển sang Tuần 4 theo D-11.
- Nhóm Admin Order (9h) được chuyển sang Tuần 5.

**Thời gian thực hiện dự kiến:** từ **18/11/2026 đến 03/12/2026**, không
tính thứ Bảy và Chủ nhật (12 ngày làm việc). Đây là khung thời gian thực
hiện và hoàn tất các PR của kế hoạch Tuần 3; không đồng nghĩa toàn bộ
công việc phải hoàn thành trong 7 ngày lịch.

**Nguyên tắc về năng lực thực hiện:**

- Năng lực thực hiện tham chiếu: khoảng **30 giờ/tuần**.
- Mức **40 giờ/tuần** là giới hạn phân bổ tối đa, không phải mục tiêu
  hoặc cam kết bắt buộc phải hoàn thành trong mỗi tuần.
- Không bố trí quá **40 giờ thực hiện trong một tuần**.
- Nếu khối lượng thực tế vượt mức tham chiếu 30 giờ, cần theo dõi Actual
  Time và điều chỉnh thời điểm hoàn thành; không tự ý giảm estimate của
  task.
- Ngày hoàn thành từng PR được theo dõi theo §4.2; các ngày này là lịch
  dự kiến để reviewer xác nhận, không phải deadline đã được phê duyệt.

### 1.2. Quyết định và nguyên tắc phân bổ (D-06, D-11)

Baseline estimate Tuần 3 là 56 giờ. Hai task CORS và API Test Setup, mỗi
task 1 giờ, được chuyển từ Tuần 2 sang Tuần 3. Tổng khối lượng cần phân
bổ là 58 giờ.

| Nội dung                                    |    Giờ |
|---------------------------------------------|-------:|
| Baseline estimate Tuần 3                    |     56 |
| CORS chuyển từ Tuần 2 sang Tuần 3           |      1 |
| API Test Setup chuyển từ Tuần 2 sang Tuần 3 |      1 |
| **Tổng khối lượng cần phân bổ**             | **58** |
| Thực hiện trong Tuần 3                      | **40** |
| Chuyển sang Tuần 4                          |  **9** |
| Chuyển sang đầu Tuần 5                      |  **9** |
| **Tổng sau phân bổ**                        | **58** |

Theo quyết định D-11, 9 giờ chuyển sang Tuần 4 được xác định cụ thể:

| Task chuyển sang Tuần 4 | Estimate |
|-------------------------|---------:|
| Cancel Order            |       4h |
| Edge Cases              |       2h |
| Unit Test tổng hợp      |       3h |
| **Tổng**                |   **9h** |

9 giờ chuyển sang Tuần 5 là nhóm Admin Order:

| Task Admin Order                                                                                  | Estimate |
|---------------------------------------------------------------------------------------------------|---------:|
| Implement API Admin Order list/search/filter theo Order ID, Customer Email, Recipient Name, Phone |       3h |
| Implement Admin Order Detail và cập nhật Order Status theo transition hợp lệ                      |       3h |
| Xử lý Admin Cancel: restore stock transactional và cập nhật payment rule khi DELIVERED            |       3h |
| **Tổng**                                                                                          |   **9h** |

Nguyên tắc:

1.  Giữ nguyên baseline toàn dự án **208 giờ**.
2.  Khối lượng thực hiện trong Tuần 3 là **40 giờ**, không phải 58 giờ.
3.  Hai task CORS và API Test Setup vẫn được tính trong khối lượng Tuần
    3.
4.  Cancel Order (4h), Edge Cases (2h) và Unit Test tổng hợp (3h) được
    chuyển sang Tuần 4.
5.  Nhóm Admin Order (9h), gồm List/Search/Filter (3h), Detail + Status
    Update (3h) và Admin Cancel + Payment Rule (3h), được chuyển sang
    Tuần 5.
6.  Không giảm estimate của task chỉ vì task được chuyển sang tuần khác.
7.  Không tính trùng thời gian giữa các tuần.
8.  Ưu tiên hoàn thành các API cần thiết để Frontend tích hợp trong Tuần
    4.
9.  Theo dõi Actual Time theo từng task. Không tự điền Actual Time khi
    chưa có số liệu thực tế.
10. Theo file ước lượng đã cập nhật, Actual Time đã ghi nhận cho CORS là
    **0.5h**, API Test Setup là **0.5h** và Spring Security là **3h**;
    các task chưa thực hiện tiếp tục để `Chưa ghi nhận`.

### 1.3. Các nội dung không triển khai đầy đủ trong Tuần 3

| Nội dung                                                                              | Estimate | Thời điểm dự kiến | Ghi chú                                                                                                      |
|---------------------------------------------------------------------------------------|---------:|-------------------|--------------------------------------------------------------------------------------------------------------|
| Cancel Order                                                                          |       4h | Tuần 4            | Chuyển theo D-11; phụ thuộc Order và Inventory Transaction                                                   |
| Edge Cases                                                                            |       2h | Tuần 4            | Rà soát các trường hợp biên của nghiệp vụ đã triển khai                                                      |
| Unit Test tổng hợp                                                                    |       3h | Tuần 4            | Bổ sung kiểm thử tổng hợp; các PR chức năng vẫn phải có test riêng                                           |
| Admin Order — List/Search/Filter theo Order ID, Customer Email, Recipient Name, Phone |       3h | Tuần 5            | Phụ thuộc Order/User và shipping snapshot                                                                    |
| Admin Order — Detail và cập nhật Order Status theo transition hợp lệ                  |       3h | Tuần 5            | `PENDING → CONFIRMED → SHIPPING → DELIVERED`; `PENDING → CANCELLED`                                          |
| Admin Order — Admin Cancel + Payment Rule                                             |       3h | Tuần 5            | Restore stock transactional khi Admin hủy; `DELIVERED` tự động chuyển `paymentStatus = PAID` và ghi `paidAt` |

### 1.4. Phạm vi chức năng được giữ nguyên

- Guest chỉ được xem sản phẩm; không có Guest Cart hoặc Guest Checkout.
- Cart và Order là nghiệp vụ của CUSTOMER đã đăng nhập.
- Không triển khai Payment Gateway, chuyển khoản ngân hàng hoặc ví điện
  tử.
- Không tạo bảng `payments`; trạng thái thanh toán tiếp tục sử dụng
  `orders.payment_status`.
- Không bổ sung Email Verification.
- Không bổ sung chức năng giảm giá/coupon.
- Nhóm Admin Order (9h) được chuyển sang Tuần 5.
- Không triển khai Cancel Order trong khối lượng 40 giờ của Tuần 3.
- Không tự ý mở rộng phạm vi nghiệp vụ ngoài Requirement Specification
  và API Specification.

------------------------------------------------------------------------

## 2. Các nguyên tắc kỹ thuật cần tuân thủ

### 2.1. Kiến trúc và cấu trúc code

- Tiếp tục tổ chức package theo module nghiệp vụ (package-by-feature).
- Phân tách Controller, Service, Repository và DTO theo trách nhiệm.
- Business logic đặt trong Service, không đặt trong Controller hoặc
  Repository.
- Không trả trực tiếp Entity từ API.
- Sử dụng `ApiResponse<T>` và `PageResponse<T>` theo API Specification.
- Sử dụng các exception hiện có và `GlobalExceptionHandler` để chuẩn hóa
  lỗi.
- Tái sử dụng `AuthService.authenticate(email, password)` đã xây dựng ở
  Tuần 2.
- Không tạo lại các class hoặc chức năng đã có nếu không có lý do kỹ
  thuật.
- Các thay đổi liên quan đến cùng một nghiệp vụ phải được kiểm thử và
  review trong cùng phạm vi PR; không merge một phần chức năng còn thiếu
  điều kiện nghiệp vụ.

### 2.2. Database và Entity

- Đối chiếu Entity với ERD và schema hiện tại.
- Không tự ý đổi tên bảng, cột hoặc kiểu dữ liệu đã chốt.
- Tiền sử dụng `BigDecimal`.
- Enum lưu bằng `@Enumerated(EnumType.STRING)`.
- Các quan hệ Cart, CartItem, Order và OrderItem phải đúng ERD.
- Dùng snapshot trong OrderItem để giữ thông tin sản phẩm tại thời điểm
  đặt hàng.
- Không bổ sung bảng `payments`.
- Khi cần cập nhật tồn kho có điều kiện, phải kiểm tra số row bị ảnh
  hưởng từ câu lệnh cập nhật.
- Các thao tác tạo Order, tạo OrderItem, trừ tồn kho và xóa CartItem
  trong Checkout phải nằm trong cùng transaction.

### 2.3. Authentication và Authorization

#### A. Thiết kế JWT (D-12)

Sử dụng **Spring Security OAuth2 Resource Server + Nimbus JWT**, không
tự viết filter chỉ để đọc và xác thực Bearer Token.

Các quy định:

- Thuật toán ký: `HS256`.
- Secret lấy từ biến môi trường `JWT_SECRET`.
- `JWT_SECRET` phải có độ dài tối thiểu 32 bytes.
- Không commit secret thật vào Git.
- Thời hạn token: **3600 giây**.
- Claim `sub`: User ID.
- Claim `role`: role của User.
- Sử dụng `JwtEncoder` để ký JWT.
- Sử dụng `JwtDecoder` để xác thực JWT.
- Sử dụng `JwtAuthenticationConverter` để ánh xạ claim role sang Spring
  Security authorities.
- `UserDetailsService` sử dụng `UserRepository` để tải thông tin User
  phục vụ xác thực email/password khi đăng nhập. Thành phần này không
  thay thế việc xác thực JWT của Resource Server.
- Bổ sung biến `JWT_SECRET` dạng placeholder vào `.env.example` và hướng
  dẫn cấu hình trong README.
- Không ghi JWT, password hoặc password hash vào log.

#### B. Authentication và Authorization

- CUSTOMER và ADMIN sử dụng chung User, phân biệt bằng role.
- Password tiếp tục được lưu dưới dạng BCrypt hash.
- Không trả password hoặc password hash trong response.
- JWT phải được xác thực trước khi truy cập endpoint yêu cầu đăng nhập.
- Endpoint public không được vô tình bị khóa bởi Spring Security.
- Endpoint ADMIN chỉ cho phép role ADMIN.
- Endpoint Cart và Customer Order chỉ cho phép CUSTOMER đã đăng nhập.
- Không chỉ kiểm tra trạng thái đăng nhập; phải kiểm tra quyền sở hữu dữ
  liệu ở Service.
- Lỗi xác thực và phân quyền phải trả JSON theo response format thống
  nhất của API, không để response 401/403 rỗng hoặc sai cấu trúc.
- Cho phép xử lý `ERROR` dispatcher và `/error` phù hợp với cấu hình
  Spring Security để tránh lỗi bảo mật làm che khuất lỗi ứng dụng ban
  đầu.

### 2.4. Quy tắc nghiệp vụ Cart và Checkout

#### A. Cart

- Cart thuộc về CUSTOMER hiện tại.
- Nếu Customer chưa có Cart, API Get Cart tạo Cart theo thiết kế.
- Khi thêm Product đã tồn tại trong Cart, cộng quantity mới vào quantity
  hiện tại (D-13).
- Nếu quantity sau khi cộng vượt stock hiện tại, trả `409 Conflict` và
  không lưu quantity vượt quá stock.
- Quantity phải lớn hơn 0.
- Product phải tồn tại và có trạng thái phù hợp với business rule.
- Không tin giá hoặc thông tin Product do Client gửi lên.
- Kiểm tra lại tình trạng Product và stock khi Checkout, không chỉ dựa
  trên lần kiểm tra lúc thêm vào Cart.

#### B. Checkout và Inventory Transaction

- Backend tự tính tổng tiền; không nhận tổng tiền từ Client làm giá trị
  tin cậy.

- Validate đầy đủ thông tin giao hàng.

- Tạo Order với trạng thái ban đầu `PENDING`, thanh toán `UNPAID`,
  `paidAt = null`.

- Tạo OrderItem snapshot gồm `product_name_snapshot`, `sku_snapshot`,
  `unit_price`, `quantity`, `item_total`.

- Trừ stock bằng conditional update:

  ``` sql
  UPDATE products
  SET stock_quantity = stock_quantity - :qty
  WHERE id = :id
    AND stock_quantity >= :qty;
  ```

- Nếu affected rows bằng 0, Checkout phải thất bại với lỗi phù hợp
  (`409 Conflict`) và rollback toàn bộ transaction.

- Không để tồn kho âm, Order dở dang hoặc CartItem bị xóa khi Checkout
  thất bại.

- Chỉ xóa CartItem sau khi các bước Checkout cần thiết đã thành công
  trong cùng transaction.

------------------------------------------------------------------------

## 3. Các bước thực hiện

Các giai đoạn dưới đây được tổ chức theo nhóm PR để bảo đảm mỗi PR chứa
một phần nghiệp vụ hoàn chỉnh, có test tương ứng và có thể review độc
lập.

### Giai đoạn 1 — CORS và API Test Setup (2h)

**Nhánh:** `feature/backend-cors-api-test`

**PR:** \#14 —
[feature/backend-cors-api-test](https://github.com/MinhQuan2k3/mini-e-commerce/pull/14)

**Trạng thái:** Đã merge ngày **20/11/2026**.

Nội dung đã hoàn thành:

1.  Cấu hình CORS thông qua biến môi trường `CORS_ALLOWED_ORIGINS`.
2.  Giới hạn origin, HTTP method và header được phép theo cấu hình môi
    trường.
3.  Kiểm tra preflight request (`OPTIONS`) và khả năng hoạt động cùng
    Spring Security.
4.  Bổ sung Postman Collection
    `postman/Mini-Ecommerce-API.postman_collection.json`.
5.  Bổ sung Postman Environment
    `postman/Mini-Ecommerce-Local.postman_environment.json`.
6.  Bổ sung test scripts kiểm tra response và lưu các ID cần thiết vào
    environment variables.
7.  Cập nhật `backend/README.md` với hướng dẫn chạy Maven test, Postman
    Collection Runner và vai trò các file `.http`.

**Kết quả đã xác nhận:**

- Backend: 47 tests, 0 failures, 0 errors, 0 skipped — `BUILD SUCCESS`.
- Postman Collection Runner: 12/12 requests passed, 23/23 tests passed,
  0 errors.
- Trường hợp xóa Category đang được Product tham chiếu trả về
  `409 Conflict` theo đúng business rule.

### Giai đoạn 2 — Spring Security (3h)

**Nhánh:** `feature/backend-spring-security`

**PR:** \#15 —
[feature/backend-spring-security](https://github.com/MinhQuan2k3/mini-e-commerce/pull/15)

**Trạng thái:** Đã merge ngày **23/11/2026**.

Nội dung đã hoàn thành:

1.  Cấu hình `SecurityFilterChain` theo mô hình REST API stateless.
2.  Tắt Form Login, HTTP Basic, CSRF và request cache theo thiết kế.
3.  Cho phép truy cập public đối với `POST /auth/register` và các Public
    Category/Product GET endpoints.
4.  Các endpoint còn lại yêu cầu authentication; Admin API hiện trả
    `401 Unauthorized` cho đến khi JWT và Authorization được triển khai.
5.  Sử dụng `PasswordEncoder` BCrypt hiện có.
6.  Bổ sung `CustomAuthenticationEntryPoint` trả JSON `ApiResponse` cho
    lỗi 401.
7.  Bổ sung `CustomAccessDeniedHandler` trả JSON `ApiResponse` cho lỗi
    403.
8.  Cho phép `ERROR` dispatcher và `/error` để không che khuất lỗi ứng
    dụng ban đầu.
9.  Cập nhật `backend/README.md` và Postman notes về trạng thái bảo mật
    trước khi có JWT.

**Kết quả đã xác nhận:**

- `SecurityConfigIntegrationTest`: 7 tests passed.
- Backend đầy đủ: 54 tests, 0 failures, 0 errors, 0 skipped —
  `BUILD SUCCESS`.
- Các lỗi 401/403 được chuẩn hóa theo cấu trúc `ApiResponse`.

### Giai đoạn 3 — JWT, Login và Authorization (7h)

**Nhánh đề xuất:** `feature/backend-jwt-authorization`

**Phạm vi PR:** JWT + Login + `/auth/me` + Authorization.

**Estimate:**

| Task          | Estimate |
|---------------|---------:|
| JWT           |       4h |
| Authorization |       3h |
| **Tổng**      |   **7h** |

Lưu ý: 3h Spring Security thuộc PR \#15, không tính lại vào PR này.

#### A. JWT

1.  Cấu hình OAuth2 Resource Server và Nimbus JWT.
2.  Cấu hình `JwtEncoder` và `JwtDecoder`.
3.  Sử dụng HS256 và secret từ `JWT_SECRET`.
4.  Token có:
    - `sub`: User ID.
    - `role`: role của User.
    - Thời hạn 3600 giây.
5.  Sử dụng `JwtAuthenticationConverter` để ánh xạ role.
6.  Kiểm tra token hợp lệ, hết hạn, sai chữ ký hoặc không hợp lệ.
7.  Kiểm tra trường hợp thiếu hoặc sai định dạng Bearer Token.
8.  Không tự viết `OncePerRequestFilter` chỉ để parse Bearer Token.
9.  Không ghi token hoặc thông tin nhạy cảm vào log.

#### B. Login — `POST /auth/login`

1.  Request sử dụng email và password.
2.  Sử dụng `UserDetailsService` dựa trên `UserRepository` để tải User
    phục vụ xác thực.
3.  Gọi lại `AuthService.authenticate(email, password)` từ Tuần 2
    (D-07).
4.  Email không tồn tại hoặc password sai phải trả cùng một thông báo
    lỗi 401.
5.  Đăng nhập thành công trả HTTP 200 với:
    - `accessToken`.
    - `tokenType = "Bearer"`.
    - `expiresIn = 3600`.
    - `user { id, email, role }`.
6.  Không trả password hoặc password hash.

#### C. Current User — `GET /auth/me`

1.  Yêu cầu JWT hợp lệ.
2.  Lấy User ID từ claim `sub`.
3.  Trả thông tin User hiện tại gồm `id`, `email`, `role`.
4.  Không nhận User ID từ Client để xác định người đang đăng nhập.
5.  Không trả thông tin nhạy cảm.

#### D. Authorization

| Nhóm endpoint           | Quyền           |
|-------------------------|-----------------|
| `POST /auth/register`   | Public          |
| `POST /auth/login`      | Public          |
| `GET /auth/me`          | CUSTOMER, ADMIN |
| Public Category/Product | Public          |
| `/admin/**`             | ADMIN           |
| `/cart/**`              | CUSTOMER        |
| `/orders/**`            | CUSTOMER        |

1.  Cấu hình role mapping nhất quán giữa database, JWT và Spring
    Security.
2.  CUSTOMER không thể gọi Admin API.
3.  ADMIN không mặc định có quyền truy cập dữ liệu Cart/Order của
    Customer nếu API Specification không quy định.
4.  Kiểm tra ownership ở Service đối với Cart và Order.
5.  Trả JSON response đúng format khi thiếu token, token không hợp lệ
    hoặc không đủ quyền.

**Commit mẫu:**

- `feat: implement jwt resource server`
- `feat: add login and current user endpoints`
- `feat: enforce role-based authorization`
- `test: add jwt and authorization tests`

**Nghiệm thu:**

- Login thành công trả JWT đúng cấu trúc.
- JWT hợp lệ xác thực được User.
- JWT hết hạn hoặc không hợp lệ bị từ chối.
- `/auth/me` trả đúng User từ token.
- CUSTOMER truy cập `/admin/**` bị từ chối.
- ADMIN và CUSTOMER chỉ truy cập endpoint đúng quyền.
- Lỗi 401/403 trả JSON đúng format.
- Không có secret thật trong Git.

### Giai đoạn 4 — Cart và Cart Validation (9h)

**Nhánh đề xuất:** `feature/backend-cart`

**Phạm vi PR:** Cart API + Cart Validation.

Không tách Cart Validation thành PR độc lập vì validation là điều kiện
cần để nghiệp vụ Cart hoàn chỉnh.

**Estimate:**

| Task                         | Estimate |
|------------------------------|---------:|
| Lấy/tạo Cart và thêm Product |       4h |
| Cập nhật và xóa CartItem     |       3h |
| Cart Validation              |       2h |
| **Tổng**                     |   **9h** |

#### A. Lấy hoặc tạo Cart

1.  Customer đăng nhập có thể lấy Cart của chính mình.
2.  Nếu chưa có Cart, tạo Cart theo thiết kế API.
3.  Thêm Product vào Cart.
4.  Nếu Product đã tồn tại trong Cart, cộng quantity mới vào quantity
    hiện tại (D-13).
5.  Nếu quantity sau khi cộng vượt stock, trả `409 Conflict` và không
    lưu quantity vượt quá stock.
6.  Không cho phép thao tác Cart của Customer khác.
7.  Không cho Guest tạo hoặc sử dụng Cart.

#### B. Cập nhật và xóa CartItem

1.  Cập nhật quantity của CartItem.
2.  Xóa CartItem khỏi Cart.
3.  Kiểm tra CartItem có thuộc Cart của Customer hiện tại không.
4.  Không cho phép cập nhật/xóa CartItem của Customer khác.
5.  Trả response đúng API Specification.

#### C. Cart Validation

1.  Product phải có status ACTIVE để được thêm vào hoặc sử dụng trong
    Cart theo business rule.
2.  Quantity phải lớn hơn 0.
3.  Quantity không được vượt quá stock hiện có.
4.  Kiểm tra Product còn tồn tại.
5.  Không tin dữ liệu giá hoặc thông tin sản phẩm do Client gửi lên; sử
    dụng dữ liệu từ database.
6.  Kiểm tra lại điều kiện tồn kho tại Checkout, không chỉ dựa trên lần
    kiểm tra khi thêm vào Cart.

#### D. Quy tắc hiển thị Cart

`GET /cart` trả thông tin cho từng CartItem:

- `available`.
- `unavailableReason`: `INACTIVE`, `OUT_OF_STOCK` hoặc `EXCEEDS_STOCK`.
- `subtotal`.

Cart trả thêm:

- `totalAmount`: chỉ cộng các item có `available = true` (D-05).
- `checkoutAllowed`: Cart không rỗng và tất cả item đều
  `available = true`.

**Commit mẫu:**

- `feat: add customer cart service`
- `feat: add cart and cart item APIs`
- `feat: validate cart items`
- `test: add cart service and validation tests`

**Nghiệm thu:**

- Customer có thể lấy/tạo Cart.
- Thêm Product vào Cart thành công.
- Product đã tồn tại được cộng dồn quantity đúng quy tắc.
- Quantity vượt stock bị từ chối với `409 Conflict`.
- Cập nhật quantity và xóa CartItem hoạt động đúng.
- Cart trả đúng `available`, `unavailableReason`, `subtotal`,
  `totalAmount`, `checkoutAllowed`.
- Không truy cập được Cart của Customer khác.
- Response và HTTP status đúng API Specification.
- Unit Test bao phủ các trường hợp thành công và thất bại.

#### E. Cập nhật API Specification §9.2 (D-13) và Change Log

Thực hiện trong cùng PR Cart, không tạo PR tài liệu riêng.

1.  Bổ sung một dòng vào API Specification §9.2 quy định: nếu Product đã
    tồn tại trong Cart, quantity mới được cộng vào quantity hiện tại.
2.  Nếu quantity sau khi cộng vượt quá stock hiện tại, API trả
    `409 Conflict`.
3.  Không persist quantity mới khi kết quả cộng dồn vượt stock.
4.  Cập nhật Change Log của API Specification để ghi nhận thay đổi theo
    D-13.
5.  Đối chiếu request/response, HTTP status và ví dụ lỗi trong API
    Specification với implementation thực tế; không tự bổ sung hành vi
    ngoài D-13.

**Nghiệm thu tài liệu:** API Specification §9.2, Change Log và Cart
implementation mô tả thống nhất quy tắc cộng dồn và xử lý
`409 Conflict`.

### Giai đoạn 5 — Checkout và Inventory Transaction (11h)

**Nhánh đề xuất:** `feature/backend-checkout`

**Phạm vi PR:** Checkout + Inventory Transaction.

Không tách Inventory Transaction thành PR độc lập vì trừ tồn kho là phần
bắt buộc của Checkout. Không merge Checkout khi chưa bảo đảm transaction
và rollback.

**Estimate:**

| Task                                            | Estimate |
|-------------------------------------------------|---------:|
| Tạo Order và OrderItem                          |       4h |
| Tính tổng tiền và kiểm tra shipping information |       2h |
| Inventory Transaction                           |       5h |
| **Tổng**                                        |  **11h** |

#### A. Tạo Order và OrderItem

1.  Customer thực hiện Checkout từ Cart của chính mình.
2.  Không cho Checkout khi Cart rỗng.
3.  Không cho Checkout nếu Cart có item không khả dụng.
4.  Tạo Order và OrderItem theo ERD.
5.  Lưu snapshot thông tin sản phẩm vào OrderItem:
    - `product_name_snapshot`.
    - `sku_snapshot`.
    - `unit_price`.
    - `quantity`.
    - `item_total`.
6.  Không phụ thuộc vào giá hiện tại của Product khi hiển thị lại Order
    cũ.
7.  Khởi tạo trạng thái:
    - `order_status = PENDING`.
    - `payment_status = UNPAID`.
    - `paid_at = null`.
8.  Không tạo Payment Gateway hoặc bảng `payments`.

#### B. Tính tổng tiền và kiểm tra shipping information

1.  Tính tổng tiền từ dữ liệu Product trong database và quantity hợp lệ.
2.  Sử dụng `BigDecimal` cho phép tính tiền.
3.  Validate đầy đủ:
    - `recipientName`.
    - `phone`.
    - `address`.
    - `provinceCity`.
    - `district`.
    - `ward`.
4.  Không nhận tổng tiền từ Client làm giá trị tin cậy.
5.  Trả lỗi validation đúng format.

#### C. Inventory Transaction

Đây là phần quan trọng của Checkout, cần bảo đảm tính nguyên tử và tránh
race condition.

1.  Trừ stock khi Checkout thành công.

2.  Sử dụng conditional update để chỉ trừ stock khi số lượng tồn kho đáp
    ứng yêu cầu:

    ``` sql
    UPDATE products
    SET stock_quantity = stock_quantity - :qty
    WHERE id = :id
      AND stock_quantity >= :qty;
    ```

3.  Kiểm tra số lượng row bị ảnh hưởng (affected rows).

4.  Nếu affected rows bằng 0:

    - Checkout thất bại với lỗi phù hợp (`409 Conflict`).
    - Không tạo Order/OrderItem dở dang.
    - Không để stock bị trừ một phần.
    - Không xóa CartItem.

5.  Toàn bộ Checkout phải nằm trong transaction phù hợp.

6.  Nếu bất kỳ bước nào thất bại, rollback toàn bộ thay đổi liên quan.

7.  Không chỉ đọc stock rồi trừ stock bằng thao tác riêng biệt mà bỏ qua
    race condition.

#### D. Kiểm thử transaction

- Mockito Unit Test: khi Repository trả affected rows = 0, Service phải
  xử lý lỗi 409 và không xóa CartItem.
- Integration Test sử dụng `@SpringBootTest` và H2 MySQL mode:
  - Checkout không đủ stock → stock không đổi, không có Order dở dang.
  - Transaction rollback đúng khi có lỗi.
- Không bắt buộc viết test đa luồng thực sự.

**Commit mẫu:**

- `feat: implement checkout and order creation`
- `feat: add transactional inventory deduction`
- `test: add checkout and inventory transaction tests`

**Nghiệm thu:**

- Tạo Order và OrderItem thành công.
- Snapshot thông tin sản phẩm được lưu đúng.
- Tổng tiền được tính từ dữ liệu hợp lệ.
- Cart rỗng hoặc shipping information không hợp lệ bị từ chối.
- Checkout thành công trừ đúng stock.
- Checkout thất bại không làm thay đổi stock.
- Không xảy ra stock âm.
- Không tạo Order dở dang khi trừ stock thất bại.
- Transaction rollback đúng khi có lỗi.
- Có Unit Test và Integration Test phù hợp.

### Giai đoạn 6 — Customer Order và Public Product Search (6h)

**Phạm vi PR:** My Orders + Product Search.

**Estimate:**

| Task           | Estimate |
|----------------|---------:|
| Customer Order |       3h |
| Product Search |       3h |
| **Tổng**       |   **6h** |

#### A. Customer Order API (3h)

**Nhánh chung của PR:** `feature/backend-customer-order-product-search`

1.  Implement API xem danh sách Order của Customer đang đăng nhập.
2.  Implement API xem chi tiết một Order.
3.  Chỉ trả Order thuộc Customer hiện tại.
4.  Kiểm tra ownership trước khi trả dữ liệu.
5.  Hỗ trợ phân trang theo API Specification.
6.  Không để Customer truy cập Order của người khác bằng cách thay
    `orderId`.

**Endpoint:**

- `GET /orders/my-orders?status&page&size&sort&direction`
- `GET /orders/{id}`

**Nghiệm thu:**

- Customer xem được danh sách và chi tiết Order của mình.
- Không thể xem Order của Customer khác.
- Order không tồn tại trả đúng HTTP status.
- Query không hợp lệ trả lỗi 400.
- `size` không vượt quá 50.
- Response đúng API Specification.

#### B. Public Product Search (3h)

**Nhánh chung của PR:** `feature/backend-customer-order-product-search`

**Endpoint:**

- `GET /products`
- `GET /products/{id}`

1.  Implement public Product list.
2.  Implement Product detail.
3.  Tìm kiếm theo Name, SKU và Description.
4.  Keyword không phân biệt chữ hoa/chữ thường.
5.  Hỗ trợ filter theo:
    - Category.
    - Khoảng giá.
6.  Hỗ trợ sort theo:
    - `name`.
    - `price`.
    - `createdAt`.
7.  Hỗ trợ pagination:
    - Page mặc định: 0.
    - Size mặc định: 10.
    - Size tối đa: 50.
8.  Public Product list chỉ hiển thị Product có trạng thái ACTIVE.
9.  Product detail có thể trả Product INACTIVE kèm `status`; Frontend sử
    dụng trạng thái này để vô hiệu hóa Add to Cart.
10. Mỗi Product trong danh sách trả thông tin Category gồm `id`, `name`.

**Nghiệm thu:**

- Tìm kiếm theo Name/SKU/Description hoạt động.
- Filter và sort cho kết quả đúng.
- Pagination đúng `PageResponse`.
- Product list không hiển thị Product INACTIVE.
- Product detail trả đúng kết quả khi ID tồn tại hoặc không tồn tại.
- Response đúng API Specification.

**Commit mẫu:**

- `feat: add customer order history and detail APIs`
- `feat: add public product search and filtering`
- `test: add customer order and product search tests`

### Giai đoạn 7 — API Test và kiểm tra tích hợp (2h)

**Branch/PR:** Không tạo branch hoặc PR độc lập cho Giai đoạn 7.

Đây là hoạt động kiểm tra bổ sung sau khi các PR chức năng đã có Unit
Test tương ứng. Không thay thế test trong từng PR và không được dùng để
trì hoãn các kiểm thử bắt buộc của Cart hoặc Checkout.

- Các request Postman mới được bổ sung trong PR chức năng tương ứng;
  không tạo một branch riêng chỉ để cập nhật collection.
- Sau khi các PR chức năng được tích hợp, thực hiện kiểm tra API tổng
  hợp trong 2h và ghi nhận kết quả.
- Nếu phát hiện lỗi cần sửa code, tạo branch `fix/` riêng theo Git
  Convention; không đưa thay đổi sửa lỗi vào một branch kiểm thử độc
  lập.

1.  Cập nhật Postman collection đã được chuẩn bị ở Giai đoạn 1.
2.  Bổ sung request cho:
    - Login và lấy thông tin User.
    - JWT authentication.
    - Authorization ADMIN/CUSTOMER.
    - Cart.
    - Checkout.
    - Customer Order.
    - Product Search.
3.  Kiểm thử request thành công và request lỗi.
4.  Kiểm tra HTTP status, response body và dữ liệu trong database.
5.  Kiểm tra ownership và role bằng các tài khoản khác nhau.
6.  Không lưu mật khẩu thật hoặc token thật trong Git.
7.  Ghi nhận kết quả kiểm thử.

**Nghiệm thu:**

- Postman collection được cập nhật theo endpoint đã triển khai.
- Có thể chạy các request Backend độc lập với Frontend.
- Kết quả kiểm thử được ghi nhận.
- Không có secret trong collection hoặc environment được commit.

------------------------------------------------------------------------

## 4. Quy trình Git và Pull Request

Theo `git-convention.md`:

### 4.1. Thứ tự xử lý PR

PR \#14 (CORS + API Test Setup) và PR \#15 (Spring Security) đã được
merge vào `main` lần lượt ngày 20/11/2026 và 23/11/2026.

1.  Lấy `main` đã bao gồm PR \#15 làm base cho các nhánh nghiệp vụ tiếp
    theo.
2.  Tạo từng branch chức năng theo đúng Git Convention; chỉ tạo branch
    tiếp theo sau khi PR trước đã được merge nếu có dependency.
3.  Không tạo stacked PR nếu không có lý do kỹ thuật cần thiết.
4.  Các ngày dự kiến ở §4.2 là mốc kế hoạch để reviewer xác nhận; cần
    đối chiếu ngày hoàn thành thực tế khi PR được merge.

### 4.2. Nhóm PR dự kiến

| PR                                                                | Nhóm công việc                                         | Estimate liên quan | Ngày hoàn thành          | Trạng thái                                  |
|-------------------------------------------------------------------|--------------------------------------------------------|-------------------:|--------------------------|---------------------------------------------|
| [PR \#14](https://github.com/MinhQuan2k3/mini-e-commerce/pull/14) | CORS + API Test Setup                                  |                 2h | **20/11/2026 — thực tế** | Đã merge                                    |
| [PR \#15](https://github.com/MinhQuan2k3/mini-e-commerce/pull/15) | Spring Security                                        |                 3h | **23/11/2026 — thực tế** | Đã merge                                    |
| PR \#16                                                           | JWT + Login + `/auth/me` + Authorization               |                 7h | 27/11/2026 — dự kiến     | Chưa triển khai; chờ reviewer xác nhận ngày |
| PR \#17                                                           | Cart + Cart Validation + cập nhật API Spec §9.2 (D-13) |                 9h | 30/11/2026 — dự kiến     | Chưa triển khai; chờ reviewer xác nhận ngày |
| PR \#18                                                           | Checkout + Inventory Transaction                       |                11h | 02/12/2026 — dự kiến     | Chưa triển khai; chờ reviewer xác nhận ngày |
| PR \#19                                                           | Customer Order + Product Search                        |                 6h | 03/12/2026 — dự kiến     | Chưa triển khai; chờ reviewer xác nhận ngày |

Giai đoạn 7 (API Test 2h) không tạo PR riêng: request Postman được cập
nhật trong các PR chức năng tương ứng, sau đó chạy kiểm tra API tổng hợp
và ghi nhận kết quả; mục tiêu hoàn tất cùng mốc 03/12/2026. Các ngày PR
\#16–#19 là ngày dự kiến, chưa phải deadline được reviewer phê duyệt.
Nếu cần sửa code phát sinh từ kiểm thử, tạo branch `fix/` theo Git
Convention.

**Nhóm Admin Order dự kiến ở Tuần 5 (9h):**

| Task                                                                                              | Estimate | Tuần thực hiện | Ghi chú                                                                   |
|---------------------------------------------------------------------------------------------------|---------:|---------------:|---------------------------------------------------------------------------|
| Implement API Admin Order list/search/filter theo Order ID, Customer Email, Recipient Name, Phone |       3h |              5 | Phụ thuộc Order/User và shipping snapshot                                 |
| Implement Admin Order Detail và cập nhật Order Status theo transition hợp lệ                      |       3h |              5 | `PENDING → CONFIRMED → SHIPPING → DELIVERED`; `PENDING → CANCELLED`       |
| Xử lý Admin Cancel: restore stock transactional và cập nhật payment rule khi DELIVERED            |       3h |              5 | `DELIVERED` tự động chuyển `paymentStatus = PAID` và ghi `paidAt`         |
| **Tổng**                                                                                          |   **9h** |                | **Chưa gán số PR hoặc ngày hoàn thành; sẽ xác định khi lập lịch Tuần 5.** |

Nhóm Admin Order này thuộc kế hoạch Tuần 5 và không nằm trong estimate
PR \#16–#19 ở bảng trên. Không tự gán số PR hoặc ngày hoàn thành khi
chưa được thống nhất.

Cancel Order (4h), Edge Cases (2h) và Unit Test tổng hợp (3h) được
chuyển sang Tuần 4 theo D-11. Nhóm Admin Order (9h), gồm
List/Search/Filter (3h), Detail + Status Update (3h) và Admin Cancel +
Payment Rule (3h), được chuyển sang Tuần 5.

Mỗi PR chức năng phải có Unit Test phù hợp. Riêng Checkout phải có cả
kiểm thử Service và Integration Test để kiểm tra transaction/rollback.
API Test ở Giai đoạn 7 là bước kiểm tra bổ sung.

### 4.3. Quy tắc tạo branch và commit

1.  Cập nhật local branch từ remote sau khi PR trước đó được merge:

    ``` powershell
    git checkout main
    git pull origin main
    ```

2.  Tạo branch theo từng nhóm công việc.

3.  Chỉ sử dụng branch prefix được cho phép:

    - `feature/`
    - `fix/`
    - `docs/`
    - `refactor/`

4.  Không sử dụng prefix `test/`; các thay đổi kiểm thử đi kèm branch
    chức năng hoặc sử dụng prefix phù hợp với quy ước.

5.  Mỗi giai đoạn công việc có commit riêng theo kế hoạch.

6.  Commit theo format:

    ``` text
    <type>: <description>
    ```

7.  Push branch và tạo Pull Request vào `main`.

8.  Gửi link PR để review; chỉ merge khi review đạt.

**Không:**

- Commit trực tiếp vào `main`.
- Dùng branch prefix ngoài quy ước.
- Đưa secret, password hoặc JWT vào Git.
- Gộp nhiều thay đổi không liên quan vào một commit chỉ để giảm số lượng
  commit.
- Tạo PR phụ thuộc vào một PR chưa merge nếu không có lý do kỹ thuật rõ
  ràng.

------------------------------------------------------------------------

## 5. Kiểm tra cuối Tuần 3

### 5.1. Kiểm tra trạng thái PR

- [x] PR \#14 — CORS + API Test Setup đã merge vào `main` ngày
  20/11/2026.
- [x] Postman Collection và Environment đã được thêm; README có hướng
  dẫn sử dụng.
- [x] PR \#15 — Spring Security đã merge vào `main` ngày 23/11/2026.
- [ ] Các PR nghiệp vụ tiếp theo được tạo từ `main` đã cập nhật.
- [ ] Mỗi PR chức năng có test tương ứng.

### 5.2. Kiểm tra chức năng

- [x] CORS được cấu hình và kiểm tra preflight request.
- [x] Spring Security hoạt động đúng với REST API.
- [ ] JWT được tạo và validate bằng OAuth2 Resource Server + Nimbus.
- [ ] JWT sử dụng HS256, `sub` là User ID, claim `role` và thời hạn 3600
  giây.
- [ ] `POST /auth/login` hoạt động đúng.
- [ ] `GET /auth/me` trả thông tin User hiện tại từ token.
- [ ] ADMIN/CUSTOMER được phân quyền đúng.
- [x] Response lỗi 401/403 đúng JSON format.
- [ ] Customer chỉ thao tác trên Cart của mình.
- [ ] Cart validation hoạt động đúng.
- [ ] Thêm Product đã có trong Cart cộng dồn quantity theo D-13.
- [ ] Quantity vượt stock bị từ chối với 409 và không lưu dữ liệu vượt
  stock.
- [ ] Cart trả đúng `available`, `unavailableReason`, `subtotal`,
  `totalAmount`, `checkoutAllowed`.
- [ ] Checkout tạo Order và OrderItem đúng snapshot.
- [ ] Tổng tiền được tính từ dữ liệu hợp lệ.
- [ ] Inventory Transaction không làm stock âm.
- [ ] Checkout rollback toàn bộ khi có lỗi.
- [ ] Customer xem được danh sách và chi tiết Order của mình.
- [ ] Public Product Search hỗ trợ search/filter/sort/pagination.
- [ ] Product list chỉ hiển thị Product ACTIVE.
- [ ] Unit Test trong từng PR chạy đạt.
- [ ] Integration Test kiểm tra Checkout rollback khi không đủ stock.
- [ ] API Test được thực hiện và ghi nhận kết quả.
- [ ] Không có secret trong Git.
- [ ] `.\mvnw.cmd clean test` chạy thành công.
- [ ] Actual Time được ghi nhận cho task đã thực hiện.
- [ ] Task chuyển sang Tuần 4 và Tuần 5 được cập nhật trong file ước
  lượng, không tính giờ trùng lặp.

### 5.3. Các nội dung không phải điều kiện hoàn thành Tuần 3

Các task sau đã được chuyển khỏi khối lượng Tuần 3, không đánh dấu là
thiếu sót của kế hoạch 40 giờ:

- Cancel Order — 4h.
- Edge Cases — 2h.
- Unit Test tổng hợp — 3h.
- Admin Order — 9h, gồm List/Search/Filter (3h), Detail + Status Update
  (3h), Admin Cancel + Payment Rule (3h).

Các task này vẫn phải được thực hiện ở tuần đã phân bổ, không được xóa
khỏi baseline dự án.

------------------------------------------------------------------------

## 6. Đối chiếu đề xuất → file ước lượng

| Nhóm công việc                                                                        | Estimate | Vị trí trong file này                                    | Tuần thực hiện |
|---------------------------------------------------------------------------------------|---------:|----------------------------------------------------------|----------------|
| CORS                                                                                  |       1h | §3 Giai đoạn 1                                           | Tuần 3         |
| API Test Setup                                                                        |       1h | §3 Giai đoạn 1                                           | Tuần 3         |
| Spring Security                                                                       |       3h | §3 Giai đoạn 2                                           | Tuần 3         |
| JWT                                                                                   |       4h | §3 Giai đoạn 3                                           | Tuần 3         |
| Authorization                                                                         |       3h | §3 Giai đoạn 3                                           | Tuần 3         |
| Cart — Tạo/lấy Cart và thêm Product                                                   |       4h | §3 Giai đoạn 4                                           | Tuần 3         |
| Cart — Cập nhật và xóa CartItem                                                       |       3h | §3 Giai đoạn 4                                           | Tuần 3         |
| Cart Validation                                                                       |       2h | §3 Giai đoạn 4                                           | Tuần 3         |
| Checkout — Tạo Order/OrderItem                                                        |       4h | §3 Giai đoạn 5                                           | Tuần 3         |
| Checkout — Tổng tiền và shipping                                                      |       2h | §3 Giai đoạn 5                                           | Tuần 3         |
| Inventory Transaction                                                                 |       5h | §3 Giai đoạn 5                                           | Tuần 3         |
| Customer Order                                                                        |       3h | §3 Giai đoạn 6                                           | Tuần 3         |
| Product Search                                                                        |       3h | §3 Giai đoạn 6                                           | Tuần 3         |
| API Test                                                                              |       2h | §3 Giai đoạn 7                                           | Tuần 3         |
| Cancel Order                                                                          |       4h | §1.3 — Các nội dung không triển khai đầy đủ trong Tuần 3 | Tuần 4         |
| Edge Cases                                                                            |       2h | §1.3 — Các nội dung không triển khai đầy đủ trong Tuần 3 | Tuần 4         |
| Unit Test tổng hợp                                                                    |       3h | §1.3 — Các nội dung không triển khai đầy đủ trong Tuần 3 | Tuần 4         |
| Admin Order — List/Search/Filter theo Order ID, Customer Email, Recipient Name, Phone |       3h | §1.2–§1.3, §4.2                                          | Tuần 5         |
| Admin Order — Detail và cập nhật Order Status theo transition hợp lệ                  |       3h | §1.2–§1.3, §4.2                                          | Tuần 5         |
| Admin Order — Admin Cancel + Payment Rule                                             |       3h | §1.2–§1.3, §4.2                                          | Tuần 5         |

**Kiểm tra tổng giờ Tuần 3:**

- Baseline Tuần 3: 56h.
- CORS + API Test Setup chuyển từ Tuần 2: 2h.
- Tổng trước phân bổ: 58h.
- Chuyển Cancel Order + Edge Cases + Unit Test tổng hợp sang Tuần 4: 9h.
- Chuyển nhóm Admin Order sang Tuần 5: 9h.
- Khối lượng thực hiện trong Tuần 3: **58 − 9 − 9 = 40h**.

**Lưu ý:** Tổng baseline toàn dự án giữ nguyên 208h. Việc thay đổi tuần
thực hiện không đồng nghĩa với việc giảm hoặc loại bỏ estimate của task.

------------------------------------------------------------------------

## 7. Đối chiếu feedback K-01 → K-08

| Mã feedback | Nội dung feedback                                                                                                      | Trạng thái                                        |
|-------------|------------------------------------------------------------------------------------------------------------------------|---------------------------------------------------|
| K-01        | Ghi rõ endpoint, quyền, HTTP status, lỗi chính và business rule trong kế hoạch                                         | Đã xử lý — §3 Giai đoạn 3–6; §7.1                 |
| K-02        | Thống nhất phân bổ 40h Tuần 3, 9h Tuần 4, 9h Tuần 5; gắn năng lực tham chiếu với khung ngày thực hiện                  | Đã xử lý — §1.1–§1.3; §4.2; §6; §7.2              |
| K-03        | Gộp Cart + Cart Validation; gộp Checkout + Inventory Transaction theo phạm vi PR hoàn chỉnh                            | Đã xử lý — §3 Giai đoạn 4–5; §4.2                 |
| K-04        | Chốt cơ chế JWT (D-12) và quy tắc cộng dồn quantity trong Cart (D-13)                                                  | Đã xử lý — §2.3.A; §2.4.A; §3 Giai đoạn 3–4; §7.4 |
| K-05        | Gom nhóm PR, bảo đảm mỗi PR có test và tuân thủ Git Convention                                                         | Đã xử lý — §3; §4.2–§4.3                          |
| K-06        | Phân biệt phần đã hoàn thành, phần đã merge và phần chưa triển khai; bổ sung yêu cầu từ review vào giai đoạn JWT       | Đã xử lý — §3 Giai đoạn 1–3; §4.1–§4.2            |
| K-07        | Bổ sung Mockito Unit Test và Integration Test cho conditional update/transaction; không bắt buộc test đa luồng thực sự | Đã xử lý — §3 Giai đoạn 5.D                       |
| K-08        | Làm rõ 40h thực hiện trong Tuần 3, 9h chuyển Tuần 4 và 9h chuyển Tuần 5                                                | Đã xử lý — §1.2; §7.2                             |

### 7.1. Các endpoint và business rule bắt buộc (K-01)

Các endpoint dưới đây ghi tương đối so với prefix `/api`.

| Nhóm           | Endpoint                                                                                | Quyền           | Thành công                                                                                          | Lỗi chính                                                                                   | Rule bắt buộc                                                                                                                                  |
|----------------|-----------------------------------------------------------------------------------------|-----------------|-----------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| Login          | `POST /auth/login`                                                                      | Public          | 200: `accessToken`, `tokenType = "Bearer"`, `expiresIn = 3600`, `user {id,email,role}`              | 400 request sai; 401 sai email/password (cùng message)                                      | Tái sử dụng `AuthService.authenticate()` (D-07).                                                                                               |
| Current user   | `GET /auth/me`                                                                          | CUSTOMER, ADMIN | 200: `{id,email,role}`                                                                              | 401 thiếu/sai/hết hạn token                                                                 | Lấy User từ token; không nhận User ID từ Client.                                                                                               |
| Get Cart       | `GET /cart`                                                                             | CUSTOMER        | 200; item có `available`, `unavailableReason`, `subtotal`; Cart có `totalAmount`, `checkoutAllowed` | 401                                                                                         | Chưa có Cart thì tạo mới; `totalAmount` chỉ cộng item available (D-05).                                                                        |
| Add item       | `POST /cart/items` `{productId, quantity}`                                              | CUSTOMER        | 200                                                                                                 | 400 quantity ≤ 0; 404 Product không tồn tại; 409 vượt stock hoặc Product INACTIVE           | Product đã có trong Cart thì cộng dồn quantity (D-13); nếu tổng quantity sau khi cộng vượt stock thì trả `409 Conflict` và không lưu thay đổi. |
| Update item    | `PUT /cart/items/{productId}` `{quantity}`                                              | CUSTOMER        | 200                                                                                                 | 400 quantity không hợp lệ; 404 CartItem không tồn tại; 409 vượt stock hoặc Product INACTIVE | Chỉ cập nhật CartItem thuộc Cart của User hiện tại.                                                                                            |
| Remove item    | `DELETE /cart/items/{productId}`                                                        | CUSTOMER        | 204, không có body                                                                                  | 404 CartItem không tồn tại                                                                  | Chỉ xóa CartItem thuộc Cart của User hiện tại.                                                                                                 |
| Checkout       | `POST /orders/checkout` `{recipientName, phone, address, provinceCity, district, ward}` | CUSTOMER        | 201                                                                                                 | 400 shipping không hợp lệ; 409 Cart rỗng, Product INACTIVE hoặc không đủ stock              | Một transaction: validate → tạo Order → tạo OrderItem snapshot → conditional stock deduction → xóa CartItems. Backend tự tính tổng tiền.       |
| My Orders      | `GET /orders/my-orders?status&page&size&sort&direction`                                 | CUSTOMER        | 200 `PageResponse`                                                                                  | 400 query không hợp lệ; 401                                                                 | Chỉ trả Order của User hiện tại; `size` ≤ 50 (D-01).                                                                                           |
| Order detail   | `GET /orders/{id}`                                                                      | CUSTOMER        | 200                                                                                                 | 401; 403 Order thuộc User khác; 404 không tồn tại                                           | Kiểm tra ownership trước khi trả dữ liệu.                                                                                                      |
| Cancel         | `PATCH /orders/{id}/cancel`                                                             | CUSTOMER        | 200                                                                                                 | 401; 403; 404; 409 Order không ở PENDING                                                    | Chỉ cập nhật Order PENDING; chỉ hoàn stock khi conditional update trạng thái thành công; cùng transaction. Chuyển sang Tuần 4.                 |
| Product list   | `GET /products?keyword&categoryId&minPrice&maxPrice&sort&direction&page&size`           | Public          | 200 `PageResponse`; item có `category {id,name}`                                                    | 400 query không hợp lệ hoặc `size > 50`                                                     | Chỉ Product ACTIVE; keyword không phân biệt hoa thường trên name/SKU/description; sort `name`, `price`, `createdAt`.                           |
| Product detail | `GET /products/{id}`                                                                    | Public          | 200                                                                                                 | 404                                                                                         | Có thể trả Product INACTIVE kèm `status`; Frontend dùng trạng thái để disable Add to Cart.                                                     |

### 7.2. Phân bổ công việc theo D-11

- Tuần 3 thực hiện 40h.
- Tuần 4 nhận 9h từ Tuần 3: Cancel Order 4h, Edge Cases 2h, Unit Test
  tổng hợp 3h.
- Tuần 5 nhận 9h nhóm Admin Order từ Tuần 3.
- Việc chuyển task không làm thay đổi tổng baseline 208h.
- Các thay đổi phân bổ phải được cập nhật đồng bộ trong
  `uoc-luong-dau-viec-tuan-2-6.md`.

### 7.3. Ghi nhận Actual Time

- Ghi Actual Time khi task đã được thực hiện và có số liệu thực tế.
- Không điền Actual Time dự kiến thay cho thời gian thực tế.
- Không tự suy ra Actual Time của Tuần 3 từ estimate.
- Khi task được chuyển tuần, cập nhật cột `Tuần thực hiện` trong file
  ước lượng.
- Nếu phát sinh thay đổi estimate hoặc phạm vi, phải ghi lý do trong
  Change Log.

### 7.4. Các quyết định áp dụng (D-11 → D-13)

| Mã quyết định | Nội dung đã thống nhất                                                                                                                                                                                                  | Vị trí áp dụng trong kế hoạch  |
|---------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------|
| D-11          | Chuyển Cancel Order (4h), Edge Cases (2h) và Unit Test tổng hợp (3h) sang Tuần 4; giữ Cart Validation trong Tuần 3; chuyển nhóm Admin Order (9h) sang Tuần 5.                                                           | §1.2–§1.3; §6; §7.2            |
| D-12          | Sử dụng Spring Security OAuth2 Resource Server + Nimbus JWT, HS256, `JWT_SECRET` tối thiểu 32 bytes, thời hạn 3600 giây, claim `sub` và `role`.                                                                         | §2.3.A; §3 Giai đoạn 3         |
| D-13          | Khi Product đã có trong Cart, cộng quantity mới vào quantity hiện tại; nếu tổng vượt stock thì trả `409 Conflict` và không lưu thay đổi. Bổ sung dòng tương ứng vào API Specification §9.2 và Change Log trong PR Cart. | §2.4.A; §3 Giai đoạn 4.E; §7.1 |

## Change Log

| Ngày       | Mã review | Vị trí                            | Thay đổi                                                                                                                                                        |
|------------|-----------|-----------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 2026-11-25 | L-02      | §1.1; §4.2                        | Bổ sung khung thời gian 18/11/2026–03/12/2026 và ngày dự kiến hoàn thành từng PR; phân biệt ngày dự kiến với ngày merge thực tế.                                |
| 2026-11-25 | L-03      | §1.2; §1.3; §4.2; §6              | Đồng bộ ba task Admin Order theo file ước lượng: List/Search/Filter (3h), Detail + Status Update (3h), Admin Cancel + Payment Rule (3h), đều thuộc Tuần 5.      |
| 2026-11-25 | L-04      | §3 Giai đoạn 1–2; §4.1–§4.2; §5.1 | Sửa URL PR \#14/#15; ghi nhận trạng thái và ngày merge thực tế.                                                                                                 |
| 2026-11-25 | L-05      | §3 Giai đoạn 4.E; §7; §7.1; §7.4  | Bổ sung bước cập nhật API Specification §9.2 và Change Log theo D-13; đổi §7 thành bảng đối chiếu feedback K-01–K-08, tách riêng danh mục quyết định D-11–D-13. |

------------------------------------------------------------------------

*File này là kế hoạch Tuần 3 đã được điều chỉnh theo feedback K-01 đến
K-08 và các quyết định D-11 đến D-13. Khi có thay đổi so với kế hoạch,
cần ghi rõ lý do và cập nhật đồng bộ file ước lượng; không tự ý thay đổi
baseline hoặc ghi nhận task chưa hoàn thành là đã nghiệm thu.*
