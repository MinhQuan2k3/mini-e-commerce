# Environment Setup

## 1. Mục đích

Tài liệu này hướng dẫn thiết lập môi trường phát triển cho dự án **Hệ thống Quản lý Bán hàng Mini (Mini E-commerce)**.

Mục tiêu:

- Chuẩn hóa môi trường phát triển giữa các thành viên.
- Có thể chạy Backend, Frontend và Database trên máy local.
- Giảm lỗi do khác phiên bản hoặc cấu hình môi trường.
- Không lưu thông tin nhạy cảm trực tiếp vào Git.

---

## 2. Kiến trúc môi trường phát triển

Project sử dụng mô hình:

```text
Frontend (ReactJS)
      |
      | HTTP / REST API
      v
Backend (Spring Boot)
      |
      | JDBC / JPA
      v
MySQL Database
```

Cấu trúc thư mục chính:

```text
mini-e-commerce/
├── backend/          # Spring Boot Backend
├── frontend/         # ReactJS Frontend
├── database/         # SQL script / database documentation
└── docs/             # Project documentation
```

---

## 3. Công nghệ sử dụng

| Thành phần       | Công nghệ          | Ghi chú                                       |
| ---------------- | ------------------ | --------------------------------------------- |
| Backend          | Java + Spring Boot | REST API                                      |
| Build Backend    | Maven              | Ưu tiên Maven Wrapper nếu project có cung cấp |
| Frontend         | ReactJS            | SPA                                           |
| Frontend Runtime | Node.js + npm      | Quản lý dependencies                          |
| Database         | MySQL              | Lưu trữ dữ liệu chính                         |
| API Testing      | Postman            | Kiểm thử REST API                             |
| Version Control  | Git + GitHub       | Quản lý source code                           |

### Phiên bản sử dụng

| Công cụ      | Phiên bản                      |
| ------------ | ------------------------------ |
| Java         | 17.0.2                         |
| Maven        | Theo Maven Wrapper của project |
| Node.js      | LTS(24.21.0)                   |
| npm          | Đi kèm Node.js                 |
| MySQL        | 8.0+                           |
| Git          | 2.4x+                          |
| IDE Backend  | IntelliJ IDEA / VS Code        |
| IDE Frontend | VS Code                        |

> Nếu repository đã khóa phiên bản cụ thể trong `pom.xml`, Maven Wrapper, `package.json` hoặc các file cấu hình khác thì **ưu tiên phiên bản được project khai báo** thay vì tự thay đổi phiên bản.

---

## 4. Cài đặt Git

Kiểm tra Git:

```bash
git --version
```

Clone repository:

```bash
git clone https://github.com/MinhQuan2k3/mini-e-commerce.git
cd mini-e-commerce
```

Kiểm tra branch hiện tại:

```bash
git branch
```

Kiểm tra trạng thái:

```bash
git status
```

---

## 5. Cài đặt Java

Kiểm tra Java:

```bash
java -version
```

Kiểm tra Java compiler:

```bash
javac -version
```

Thiết lập biến môi trường:

```text
JAVA_HOME=<đường_dẫn_đến_JDK>
```

Sau khi cấu hình, kiểm tra:

```bash
echo %JAVA_HOME%
```

Trên Linux/macOS:

```bash
echo $JAVA_HOME
```

---

## 6. Thiết lập Backend

Di chuyển vào thư mục Backend:

```bash
cd backend
```

Kiểm tra cấu trúc cơ bản:

```text
backend/
├── src/
├── pom.xml
└── ...
```

### 6.1. Kiểm tra Maven

Nếu project có Maven Wrapper:

Windows:

```bash
mvnw.cmd -version
```

Linux/macOS:

```bash
./mvnw -version
```

Nếu không sử dụng Maven Wrapper:

```bash
mvn -version
```

### 6.2. Cài dependencies

Windows:

```bash
mvnw.cmd clean install
```

Linux/macOS:

```bash
./mvnw clean install
```

### 6.3. Chạy Backend

Windows:

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Backend mặc định nên chạy tại:

```text
http://localhost:8080
```

Nếu project sử dụng port khác, cấu hình trong `application.properties` hoặc `application.yml`.

---

## 7. Thiết lập MySQL

### 7.1. Cài đặt MySQL

Cài đặt MySQL Server 8.0 hoặc phiên bản tương thích.

Kiểm tra:

```bash
mysql --version
```

Đăng nhập MySQL:

```bash
mysql -u root -p
```

### 7.2. Tạo database

Tạo database cho project:

```sql
CREATE DATABASE mini_ecommerce
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

Kiểm tra:

```sql
SHOW DATABASES;
```

Chọn database:

```sql
USE mini_ecommerce;
```

---

## 8. Cấu hình kết nối Database

Backend cần các thông tin:

```text
Database URL
Database Username
Database Password
```

Ví dụ:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/mini_ecommerce
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### Quy tắc bảo mật

Không commit password thật vào Git.

Không sử dụng:

```properties
spring.datasource.password=123456
```

thông tin này trong source code public.

Có thể sử dụng:

- Environment Variables.
- Local configuration.
- File `.env` hoặc file cấu hình local được thêm vào `.gitignore`.

Ví dụ:

```text
DB_URL=jdbc:mysql://localhost:3306/mini_ecommerce
DB_USERNAME=root
DB_PASSWORD=******
```

---

## 9. Database Initialization

Database cần được khởi tạo theo thiết kế tại:

```text
docs/02-design/
```

và các script trong:

```text
database/
```

Thứ tự triển khai khuyến nghị:

```text
1. Tạo database
2. Tạo bảng
3. Tạo constraint / index
4. Insert dữ liệu mẫu
5. Tạo tài khoản Admin mặc định
6. Kiểm tra dữ liệu
```

### Admin mặc định

MVP không hỗ trợ Admin tự đăng ký.

Tài khoản Admin mặc định phải được tạo thông qua:

- database seed,
- migration,
- hoặc cơ chế initialization của Backend.

Không tạo tài khoản Admin bằng một API public.

---

## 10. Thiết lập Frontend

Mở terminal mới và di chuyển:

```bash
cd frontend
```

Kiểm tra Node.js:

```bash
node -v
```

Kiểm tra npm:

```bash
npm -v
```

### 10.1. Cài dependencies

```bash
npm install
```

### 10.2. Chạy Frontend

Tùy cấu hình project:

```bash
npm run dev
```

hoặc:

```bash
npm start
```

URL thường dùng:

```text
http://localhost:5173
```

hoặc:

```text
http://localhost:3000
```

Port thực tế phụ thuộc vào cấu hình của project.

---

## 11. Cấu hình API Endpoint

Frontend phải biết địa chỉ Backend API.

Ví dụ:

```text
http://localhost:8080/api
```

Không hard-code URL API ở nhiều file.

Nên tập trung cấu hình tại một nơi, ví dụ:

```text
frontend/.env
```

Ví dụ:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

Hoặc sử dụng cơ chế tương ứng với toolchain hiện tại của Frontend.

---

## 12. Kiểm tra toàn bộ môi trường

Sau khi thiết lập xong, kiểm tra theo thứ tự:

### Database

```text
MySQL đang chạy
↓
Database mini_ecommerce tồn tại
↓
Các bảng đã được tạo
```

### Backend

```text
Spring Boot khởi động thành công
↓
Kết nối MySQL thành công
↓
REST API phản hồi
```

### Frontend

```text
React app khởi động thành công
↓
Frontend truy cập được
↓
Frontend gọi được Backend API
```

---

## 13. Checklist

### Git

- [ ] Git đã cài đặt.
- [ ] Repository đã được clone.
- [ ] Có thể `git pull`.
- [ ] Có thể tạo branch.

### Backend

- [ ] Java đúng phiên bản.
- [ ] Maven/Maven Wrapper hoạt động.
- [ ] Dependencies cài thành công.
- [ ] Spring Boot chạy thành công.

### Database

- [ ] MySQL đang chạy.
- [ ] Database đã được tạo.
- [ ] Backend kết nối được MySQL.
- [ ] Database schema đã được khởi tạo.
- [ ] Admin mặc định đã được tạo.

### Frontend

- [ ] Node.js đúng phiên bản.
- [ ] `npm install` thành công.
- [ ] React app chạy thành công.
- [ ] Frontend gọi được Backend.

### Kiểm tra cuối

- [ ] Không có secret/password được commit.
- [ ] Không có lỗi build.
- [ ] Không có lỗi kết nối Database.
- [ ] Có thể chạy đầy đủ Frontend + Backend + Database trên local.

---

## 14. Một số lỗi thường gặp

### Backend không kết nối được MySQL

Kiểm tra:

```text
- MySQL đã chạy chưa?
- Database name có đúng không?
- Username/password có đúng không?
- Port MySQL có đúng không?
```

### Frontend không gọi được Backend

Kiểm tra:

```text
- Backend có đang chạy không?
- API Base URL có đúng không?
- Backend có mở đúng port không?
- CORS đã được cấu hình chưa?
```

### Dependencies bị lỗi

Thử:

```bash
npm install
```

Frontend.

Backend:

```bash
mvnw.cmd clean install
```

hoặc:

```bash
./mvnw clean install
```

### Port bị chiếm

Kiểm tra process đang sử dụng port và:

- dừng process đó,
- hoặc đổi port của ứng dụng.

---

## 15. Nguyên tắc chung

Môi trường local phải phục vụ việc phát triển và kiểm thử, không chứa thông tin production.

Không commit:

```text
password
JWT secret
API key
private key
database credential
```

Không tự ý thay đổi phiên bản framework hoặc công cụ khi chưa thống nhất.

Khi thay đổi dependency hoặc yêu cầu môi trường, phải cập nhật lại tài liệu này.
