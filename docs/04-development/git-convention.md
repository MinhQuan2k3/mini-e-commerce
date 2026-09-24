# Git Convention

## 1. Mục đích

Tài liệu này quy định cách sử dụng Git trong dự án **Hệ thống Quản lý Bán hàng Mini**.

Mục tiêu:

- Commit history dễ đọc.
- Dễ truy vết thay đổi.
- Giảm conflict.
- Tách biệt các tính năng.
- Hỗ trợ code review.
- Hạn chế commit code chưa hoàn thiện.

---

# 2. Repository

Repository:

```text
mini-e-commerce
```

Remote chính:

```text
origin
```

Kiểm tra:

```bash
git remote -v
```

---

# 3. Branch Convention

## 3.1. Main Branch

Branch chính:

```text
main
```

`main` phải ở trạng thái có thể build và chạy được.

Không commit trực tiếp các thay đổi lớn vào `main`.

---

## 3.2. Feature Branch

Mỗi tính năng nên có branch riêng.

Format:

```text
feature/<feature-name>
```

Ví dụ:

```text
feature/product-api
feature/cart
feature/checkout
feature/order-management
feature/admin-product
```

---

## 3.3. Bug Fix Branch

Format:

```text
fix/<bug-name>
```

Ví dụ:

```text
fix/cart-stock-validation
fix/order-total-calculation
fix/login-error
```

---

## 3.4. Documentation Branch

Format:

```text
docs/<document-name>
```

Ví dụ:

```text
docs/erd
docs/api-documentation
docs/environment-setup
```

---

## 3.5. Refactor Branch

Format:

```text
refactor/<module-name>
```

Ví dụ:

```text
refactor/order-service
refactor/api-client
```

---

# 4. Branch Naming Rules

Tên branch:

- viết thường;
- dùng `-` để phân tách từ;
- ngắn nhưng có ý nghĩa;
- không dùng khoảng trắng;
- không dùng tiếng Việt có dấu.

Đúng:

```text
feature/order-management
fix/cart-stock-validation
docs/environment-setup
```

Sai:

```text
Feature/New Cart
feature/GioHangMoi
mybranch
test
abc
```

---

# 5. Commit Convention

Commit message sử dụng Conventional Commits.

Format:

```text
<type>: <description>
```

Ví dụ:

```text
feat: add product CRUD API
fix: validate cart stock before checkout
docs: add environment setup guide
refactor: simplify order service
test: add checkout service tests
chore: update backend dependencies
```

---

# 6. Commit Types

| Type | Mục đích |
|---|---|
| `feat` | Thêm tính năng mới |
| `fix` | Sửa bug |
| `docs` | Thay đổi tài liệu |
| `refactor` | Refactor không thay đổi behavior |
| `test` | Thêm hoặc sửa test |
| `chore` | Công việc maintenance/config |
| `style` | Format/style, không thay đổi logic |
| `perf` | Cải thiện performance |

---

# 7. Commit Message Rules

Commit message phải:

- mô tả rõ thay đổi;
- bắt đầu bằng một type hợp lệ;
- viết ở dạng mệnh lệnh/ngắn gọn;
- không chứa thông tin thừa.

Tốt:

```text
feat: add customer registration API
```

```text
fix: prevent checkout with inactive product
```

Không tốt:

```text
update
```

```text
fix bug
```

```text
final code
```

```text
test123
```

---

# 8. Commit Scope

Một commit nên tập trung vào **một thay đổi logic chính**.

Không nên gom:

```text
Product API
Cart UI
Database migration
Documentation
```

vào một commit duy nhất nếu các thay đổi không liên quan trực tiếp.

Nên tách:

```text
feat: add product CRUD API
feat: add cart API
feat: add cart page
docs: update cart use case
```

---

# 9. Commit Frequency

Không cần commit sau từng dòng code.

Nên commit khi hoàn thành một đơn vị thay đổi có ý nghĩa.

Ví dụ:

```text
1. Tạo Product Entity
2. Tạo Product Repository
3. Tạo Product Service
4. Tạo Product Controller
5. Add Product API tests
```

Có thể tạo các commit tương ứng nếu mỗi bước đủ độc lập.

---

# 10. Workflow phát triển

Workflow cơ bản:

```text
main
  |
  +--> feature/product-api
  |
  +--> feature/cart
  |
  +--> feature/checkout
```

Quy trình:

```text
1. Pull main
2. Tạo feature branch
3. Code
4. Test
5. Commit
6. Push branch
7. Review
8. Merge vào main
```

---

# 11. Bắt đầu một Feature

Luôn lấy code mới nhất từ `main` trước khi tạo feature branch.

```bash
git checkout main
git pull origin main
```

Tạo branch:

```bash
git checkout -b feature/product-api
```

Hoặc:

```bash
git switch -c feature/product-api
```

---

# 12. Kiểm tra trước khi Commit

Kiểm tra:

```bash
git status
```

Xem thay đổi:

```bash
git diff
```

Add:

```bash
git add .
```

Kiểm tra staged changes:

```bash
git diff --cached
```

Sau đó commit:

```bash
git commit -m "feat: add product CRUD API"
```

---

# 13. Push Branch

Lần đầu:

```bash
git push -u origin feature/product-api
```

Các lần tiếp theo:

```bash
git push
```

---

# 14. Pull Request

Feature branch sau khi hoàn thành nên được đưa lên repository thông qua Pull Request.

PR cần có:

```text
Title
Description
Related requirement
What changed
How to test
Known issues
```

Ví dụ:

```text
Title:
feat: add product CRUD API
```

Description:

```text
## What changed

- Add Product entity
- Add Product repository
- Add Product service
- Add Product controller
- Add CRUD endpoints

## Related Requirements

- FR-PRODUCT
- UC-PRODUCT

## How to test

- Run backend
- Use Postman collection
- Test GET/POST/PUT/DELETE product
```

---

# 15. Pull Request Rules

PR nên:

- tập trung vào một feature/fix;
- không chứa thay đổi không liên quan;
- build thành công;
- test phù hợp đã chạy;
- không chứa secret;
- có mô tả đủ rõ để reviewer hiểu.

Không tạo một PR chứa đồng thời:

```text
Feature
Unrelated refactor
Random formatting
Documentation cleanup
```

nếu không cần thiết.

---

# 16. Pull / Sync Branch

Trước khi tiếp tục phát triển sau một khoảng thời gian, đồng bộ `main`:

```bash
git checkout main
git pull origin main
```

Sau đó cập nhật feature branch theo workflow của team.

Có thể dùng:

```bash
git merge main
```

hoặc:

```bash
git rebase main
```

Việc sử dụng `merge` hay `rebase` phải thống nhất trong team.

---

# 17. Merge Conflict

Khi conflict xảy ra:

```text
1. Xác định các file conflict
2. Đọc cả hai phía thay đổi
3. Giữ lại logic đúng
4. Xóa conflict markers
5. Chạy test
6. Commit kết quả xử lý conflict
```

Conflict markers:

```text
<<<<<<< HEAD
=======
>>>>>>> feature/...
```

Không được để các marker này còn trong source code.

---

# 18. `.gitignore`

Không commit các file/generated data không cần thiết.

Ví dụ:

```gitignore
# Java / Maven
target/

# Node
node_modules/

# Environment
.env
.env.*
!.env.example

# IDE
.idea/
.vscode/
*.iml

# Logs
*.log

# OS
.DS_Store
Thumbs.db
```

Không commit:

```text
node_modules/
target/
.env
database password
JWT secret
API key
```

---

# 19. Environment Configuration

Nếu application cần environment variable, commit file mẫu:

```text
.env.example
```

Ví dụ:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=
JWT_SECRET=
```

Không commit:

```text
.env
```

với giá trị thật.

---

# 20. Không Rewrite History tùy tiện

Không tự ý force push lên branch chung.

Hạn chế:

```bash
git push --force
```

Đặc biệt không force push:

```text
main
shared branch
```

Nếu cần rewrite history trên branch cá nhân, phải đảm bảo không ảnh hưởng người khác.

---

# 21. Tag và Release

Khi hoàn thành một milestone quan trọng có thể tạo tag.

Ví dụ:

```text
v0.1.0
v0.2.0
v1.0.0
```

Đối với project 6 tuần, có thể sử dụng milestone theo tuần:

```text
Week 1 - Requirements & Design
Week 2 - Backend Foundation
Week 3 - Product & Cart
Week 4 - Checkout & Order
Week 5 - Frontend Integration
Week 6 - Testing & Finalization
```

---

# 22. Commit mẫu cho Project

### Requirements

```bash
git commit -m "docs: finalize requirement clarification"
```

### Design

```bash
git commit -m "docs: add entity relationship diagram"
```

```bash
git commit -m "docs: add class diagram"
```

### Backend

```bash
git commit -m "feat: add product CRUD API"
```

```bash
git commit -m "feat: add customer authentication"
```

### Cart

```bash
git commit -m "feat: add cart management"
```

### Checkout

```bash
git commit -m "feat: implement checkout transaction"
```

### Bug fix

```bash
git commit -m "fix: prevent checkout with insufficient stock"
```

### Test

```bash
git commit -m "test: add order service tests"
```

---

# 23. Quy tắc cho Database Changes

Khi thay đổi database:

```text
Database Schema
+
Backend Entity
+
Repository/Service
+
Documentation
```

phải được xem xét đồng bộ.

Ví dụ nếu thêm:

```text
new column
```

cần kiểm tra:

```text
ERD
Class Diagram
Entity
DTO
API
Validation
Test
```

Không tự ý thay đổi database mà không cập nhật design liên quan.

---

# 24. Definition of Done cho một Feature

Một feature được xem là hoàn thành khi:

```text
[ ] Requirement đã được xác định
[ ] Code đã được implement
[ ] Validation đã có
[ ] Error handling đã có
[ ] Test phù hợp đã chạy
[ ] Không còn debug code
[ ] Documentation cần thiết đã cập nhật
[ ] Commit message đúng convention
[ ] Feature branch có thể build
[ ] Pull Request đã được review
```

---

# 25. Nguyên tắc cuối cùng

Git được sử dụng để lưu lại **lịch sử phát triển có ý nghĩa**, không chỉ để upload code.

Mỗi commit nên trả lời được:

```text
"Thay đổi này làm gì?"
```

Mỗi branch nên trả lời được:

```text
"Branch này đang giải quyết vấn đề gì?"
```

Mỗi Pull Request nên trả lời được:

```text
"Thay đổi này đáp ứng requirement nào và đã được kiểm thử như thế nào?"
```
