# Backend – Mini E-commerce

## 1. Giới thiệu

Backend của dự án Mini E-commerce được xây dựng bằng Spring Boot, sử dụng MySQL làm hệ quản trị cơ sở dữ liệu chính và Spring Data JPA để làm việc với dữ liệu.

**Công nghệ sử dụng:**

| Công nghệ | Phiên bản / Vai trò |
|---|---|
| Java | 17 |
| Spring Boot | 4.1.1 |
| Maven | Maven Wrapper |
| MySQL | 8.0.16 trở lên |
| H2 Database | Database in-memory dành cho kiểm thử |
| Hibernate / JPA | ORM và quản lý persistence |

MySQL 8.0.16 trở lên được khuyến nghị để hỗ trợ thực thi các CHECK constraints trong database schema.

## 2. Yêu cầu môi trường

Trước khi chạy backend, cần cài đặt:

- JDK 17.
- MySQL Server.
- MySQL Workbench (khuyến nghị để quản lý database).
- IntelliJ IDEA hoặc IDE hỗ trợ Java.
- Git.

Kiểm tra phiên bản Java:

```powershell
java -version
```

Kiểm tra MySQL:

```sql
SELECT VERSION();
```

## 3. Clone repository

Clone repository:

```powershell
git clone https://github.com/MinhQuan2k3/mini-e-commerce.git
```

Di chuyển vào thư mục backend:

```powershell
cd mini-e-commerce/backend
```

## 4. Khởi tạo database

### 4.1. Tạo database và các bảng

Mở MySQL Workbench, kết nối đến MySQL Server.

Mở và thực thi file:

```text
database/schema.sql
```

File SQL nằm tại thư mục gốc repository, bên ngoài thư mục `backend`.

Sau khi thực thi thành công, database `mini_ecommerce` sẽ được tạo với 7 bảng:

- `users`
- `categories`
- `products`
- `carts`
- `cart_items`
- `orders`
- `order_items`

Kiểm tra database:

```sql
USE mini_ecommerce;

SHOW TABLES;
```

### 4.2. Tạo tài khoản ứng dụng

Backend cần một tài khoản MySQL có quyền truy cập database `mini_ecommerce`.

Có thể sử dụng tài khoản MySQL hiện có hoặc tạo tài khoản riêng cho ứng dụng. Không nên sử dụng tài khoản `root` làm tài khoản chạy ứng dụng trong môi trường triển khai.

Ví dụ tạo tài khoản riêng (thực hiện bằng tài khoản quản trị MySQL):

```sql
CREATE USER 'mini_ecommerce_app'@'localhost'
IDENTIFIED BY 'your_database_password';

GRANT ALL PRIVILEGES ON mini_ecommerce.*
TO 'mini_ecommerce_app'@'localhost';
```

Thay mật khẩu mẫu bằng mật khẩu riêng của bạn. Không đưa mật khẩu thật vào source code hoặc commit lên Git.

## 5. Cấu hình biến môi trường

### 5.1. Các biến môi trường

Backend sử dụng các biến sau:

| Biến | Mô tả |
|---|---|
| `DB_URL` | JDBC URL kết nối MySQL |
| `DB_USERNAME` | Tên tài khoản MySQL |
| `DB_PASSWORD` | Mật khẩu tài khoản MySQL |
| `ADMIN_EMAIL` | Email dự kiến của tài khoản Admin |
| `ADMIN_PASSWORD` | Mật khẩu dự kiến của tài khoản Admin |

File `.env.example` tại thư mục gốc repository cung cấp giá trị mẫu cho các biến này.

Có thể sao chép `.env.example` thành `.env` để lưu thông tin cấu hình cá nhân. File `.env` đã được thêm vào `.gitignore`, không được commit lên Git.

**Lưu ý:** Spring Boot không tự động đọc file `.env`. Các biến môi trường cần được khai báo trong IntelliJ IDEA Run Configuration hoặc trong môi trường hệ điều hành trước khi chạy ứng dụng.

`ADMIN_EMAIL` và `ADMIN_PASSWORD` hiện được chuẩn bị cho các giai đoạn phát triển sau. Chúng chưa được sử dụng để tự động tạo tài khoản Admin.

### 5.2. Cấu hình trong IntelliJ IDEA

1. Mở project trong IntelliJ IDEA.
2. Chọn **Run → Edit Configurations**.
3. Chọn cấu hình chạy `BackendApplication`.
4. Tìm mục **Environment variables**.
5. Khai báo các biến:

```text
DB_URL=jdbc:mysql://localhost:3306/mini_ecommerce
DB_USERNAME=mini_ecommerce_app
DB_PASSWORD=your_database_password
ADMIN_EMAIL=admin@example.com
ADMIN_PASSWORD=your_admin_password
```

6. Nhấn **Apply → OK** để lưu cấu hình.

Thay các giá trị mẫu bằng thông tin thực tế trên máy cá nhân.

## 6. Chạy ứng dụng

### 6.1. Chạy bằng IntelliJ IDEA

- Mở class `BackendApplication`.
- Chọn Run để khởi động ứng dụng.
- Đảm bảo MySQL Server đang hoạt động và các biến môi trường đã được khai báo.

Ứng dụng mặc định sử dụng:

| Cấu hình | Giá trị |
|---|---|
| Server port | `8080` |
| Context path | `/api` |
| Database | `mini_ecommerce` |

### 6.2. Chạy bằng Maven Wrapper trên Windows

Mở PowerShell tại thư mục `backend` và khai báo biến môi trường cho phiên làm việc hiện tại:

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/mini_ecommerce"
$env:DB_USERNAME = "mini_ecommerce_app"
$env:DB_PASSWORD = "your_database_password"

$env:ADMIN_EMAIL = "admin@example.com"
$env:ADMIN_PASSWORD = "your_admin_password"
```

Khởi động ứng dụng:

```powershell
.\mvnw.cmd spring-boot:run
```

Các biến được khai báo bằng `$env:` chỉ có hiệu lực trong phiên PowerShell hiện tại và các tiến trình được khởi chạy từ phiên đó.

## 7. Chạy kiểm thử

Tại thư mục `backend`, chạy:

```powershell
.\mvnw.cmd clean test
```

Profile `test` sử dụng H2 in-memory, độc lập với MySQL.

Cấu hình test nằm tại:

```text
src/test/resources/application-test.properties
```

H2 được bật chế độ tương thích MySQL thông qua `MODE=MySQL`.

Khi chạy test, không cần khởi động MySQL Server nếu các test chỉ sử dụng profile H2.

> **Lưu ý:** 
> - File `database/schema.sql` được thiết kế để khởi tạo
> schema trên một database trống. Script không được thiết kế để
> chạy lại trên database đã có các bảng tương ứng.
>
> - Nếu cần khởi tạo lại database từ đầu, hãy xóa database
> `mini_ecommerce` hiện tại trước, tạo lại database trống,
> sau đó thực thi lại `database/schema.sql`.
>
> - Việc xóa database sẽ xóa toàn bộ dữ liệu hiện có.
> Chỉ thực hiện khi chắc chắn không cần giữ lại dữ liệu.

## 8. Cấu trúc package

Backend sử dụng cách tổ chức package theo module nghiệp vụ (package-by-feature). Mỗi module chứa các thành phần liên quan đến nghiệp vụ tương ứng, được phân chia thành các package con như `controller/`, `service/`, `repository/`, `entity/` và `dto/` khi cần thiết.

Các thành phần dùng chung được tổ chức trong `common/`, cấu hình và thành phần khởi tạo ứng dụng được tổ chức trong `config/`.

Cấu trúc package hiện tại:

```text
com.miniecommerce.backend/
├── BackendApplication.java
│
├── auth/
│   ├── controller/
│   │   └── AuthController.java
│   ├── dto/
│   │   ├── RegisterRequest.java
│   │   └── RegisterResponse.java
│   └── service/
│       └── AuthService.java
│
├── cart/
│   ├── entity/
│   │   ├── Cart.java
│   │   └── CartItem.java
│   └── repository/
│       ├── CartItemRepository.java
│       └── CartRepository.java
│
├── category/
│   ├── controller/
│   │   ├── AdminCategoryController.java
│   │   └── CategoryController.java
│   ├── dto/
│   │   ├── CategoryRequest.java
│   │   └── CategoryResponse.java
│   ├── entity/
│   │   ├── Category.java
│   │   └── CategoryStatus.java
│   ├── repository/
│   │   └── CategoryRepository.java
│   └── service/
│       └── CategoryService.java
│
├── common/
│   ├── exception/
│   │   ├── BadRequestException.java
│   │   ├── ConflictException.java
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── UnauthorizedException.java
│   └── response/
│       ├── ApiResponse.java
│       ├── ErrorDetail.java
│       └── PageResponse.java
│
├── config/
│   └── DataInitializer.java
│
├── order/
│   ├── entity/
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   ├── OrderStatus.java
│   │   └── PaymentStatus.java
│   └── repository/
│       ├── OrderItemRepository.java
│       └── OrderRepository.java
│
├── product/
│   ├── controller/
│   │   └── AdminProductController.java
│   ├── dto/
│   │   ├── AdminProductDetailResponse.java
│   │   ├── AdminProductRequest.java
│   │   └── AdminProductResponse.java
│   ├── entity/
│   │   ├── Product.java
│   │   └── ProductStatus.java
│   ├── repository/
│   │   └── ProductRepository.java
│   └── service/
│       └── AdminProductService.java
│
└── user/
    ├── entity/
    │   ├── User.java
    │   └── UserRole.java
    └── repository/
        └── UserRepository.java
```

Trong đó:

- `controller`: tiếp nhận và xử lý HTTP request.
- `service`: triển khai business logic.
- `repository`: truy cập dữ liệu.
- `entity`: các JPA Entity và kiểu dữ liệu liên quan đến module.
- `dto`: các đối tượng trao đổi dữ liệu.
- `common`: thành phần dùng chung giữa nhiều module.
- `config`: cấu hình và các thành phần khởi tạo ứng dụng.

Cấu trúc trên phản ánh các package và class hiện có trong source code. Khi triển khai thêm chức năng, các package con và class tương ứng sẽ được bổ sung vào module nghiệp vụ phù hợp.

## 9. Cấu hình JPA

Ứng dụng sử dụng cấu hình:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
```

Hibernate chỉ kiểm tra sự tương thích giữa Entity và database schema, không tự động tạo, sửa hoặc xóa bảng.

Do đó, cần thực thi `database/schema.sql` trước khi chạy ứng dụng với MySQL.

## 10. Xử lý lỗi thường gặp

| Lỗi | Nguyên nhân có thể | Cách xử lý |
|---|---|---|
| `Communications link failure` | MySQL Server chưa chạy hoặc sai host/port | Kiểm tra MySQL Server và `DB_URL` |
| `Access denied for user` | Sai username/password hoặc thiếu quyền | Kiểm tra `DB_USERNAME`, `DB_PASSWORD` và quyền tài khoản |
| `Unknown database` | Database chưa được tạo | Thực thi `database/schema.sql` |
| `Table doesn't exist` | Schema chưa được khởi tạo đầy đủ | Kiểm tra lại 7 bảng trong MySQL Workbench |
| `Schema-validation: missing table` | Entity yêu cầu bảng không tồn tại | Đối chiếu Entity và schema |
| `Port 8080 was already in use` | Cổng 8080 đang được tiến trình khác sử dụng | Dừng tiến trình đang chiếm cổng hoặc thay đổi port |
| `BUILD FAILURE` khi chạy test | Lỗi biên dịch hoặc kiểm thử | Đọc thông báo lỗi đầu tiên và phần `Caused by` trong Maven output |

## 11. Lưu ý bảo mật

- Không commit mật khẩu MySQL hoặc thông tin bí mật lên Git.
- Không sử dụng tài khoản MySQL `root` để chạy ứng dụng trong môi trường triển khai.
- Không commit file `.env`.
- Chỉ commit `.env.example` với các giá trị mẫu.
- Không đưa thông tin đăng nhập thật vào log hoặc tài liệu công khai.
