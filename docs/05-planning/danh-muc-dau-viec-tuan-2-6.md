# Danh mục Đầu việc Tuần 2 - Tuần 6 — Dự án Mini E-commerce

## Hướng dẫn sử dụng file này

Đây là danh sách các **đầu việc lớn (epic)** cần hoàn thành trong 5 tuần còn lại của dự án, dựa trên thiết kế đã làm ở Tuần 1. File này **không chia sẵn theo ngày** — nhiệm vụ ở đây là:

1. Với mỗi đầu việc lớn bên dưới, **chia nhỏ thành các task chi tiết hơn** (mỗi task nên đủ nhỏ để làm trong khoảng 0.5 ngày đến 1 ngày).
2. **Uớc lượng số giờ** cần để hoàn thành từng task, dựa trên: độ phức tạp, việc có cần học thêm công nghệ mới không, có phụ thuộc vào task khác không (Estimate theo giờ).
3. Điền toàn bộ vào **Bảng tổng hợp ước lượng** ở cuối file.
4. Gửi lại bảng này để người ta có thể trao đổi và thống nhất kế hoạch trước khi bắt đầu Tuần 2.

> Việc ước lượng không cần chính xác tuyệt đối — mục đích ở đây là tập thói quen lập kế hoạch, và để người ta theo dõi tiến độ thực tế so với dự kiến trong các tuần tiếp theo.

---

## TUẦN 2: Backend — Nền tảng

**Mục tiêu:** Dựng khung dự án backend, kết nối được database, hoàn thành CRUD cơ bản cho sản phẩm/danh mục.

Đầu việc lớn:
- Setup project Spring Boot, kết nối MySQL, cấu hình JPA
- Cấu hình CORS để chuẩn bị cho Frontend gọi API ở Tuần 4
- Quản lý thông tin kết nối database an toàn (không hardcode, không commit thông tin nhạy cảm lên Git)
- Thiết lập cơ chế xử lý lỗi chung (Global Exception Handler)
- Tạo các Entity chính (User, Product, Category...), áp dụng OOP hợp lý
- CRUD Product + Category (tách rõ Controller - Service - Repository)
- Chuẩn bị dữ liệu mẫu (seed data): vài danh mục, vài sản phẩm, 1 tài khoản Admin mặc định
- Chức năng đăng ký / đăng nhập cơ bản (chưa cần JWT vội)

---

## TUẦN 3: Backend — Nghiệp vụ chính

**Mục tiêu:** Hoàn thiện toàn bộ nghiệp vụ lõi của hệ thống (giỏ hàng, đặt hàng, phân quyền).

Đầu việc lớn:
- Phân quyền Admin/Customer (Spring Security + JWT)
- Chức năng giỏ hàng (thêm/sửa/xóa sản phẩm trong giỏ)
- Chức năng đặt hàng: tạo đơn, tính tổng tiền (áp dụng giảm giá nếu có), trừ tồn kho khi đặt hàng thành công
- API xem danh sách đơn hàng của bản thân + hủy đơn khi đang ở trạng thái "Chờ xử lý" (hoàn lại tồn kho khi hủy)
- Tìm kiếm + lọc + sắp xếp sản phẩm (theo tên, giá, danh mục)
- Xử lý các trường hợp đặc biệt: đặt hàng khi hết hàng, giỏ hàng rỗng, mã giảm giá hết hạn
- Viết Unit Test cho các hàm tính toán nghiệp vụ quan trọng (tổng giỏ hàng, áp dụng giảm giá)
- Cập nhật bộ test API (Postman/Swagger) cho các endpoint mới, để dùng test độc lập trước khi ghép Frontend

---

## TUẦN 4: Frontend (ReactJS)

**Mục tiêu:** Xây dựng giao diện khách hàng hoàn chỉnh, kết nối được với Backend.

Đầu việc lớn:
- Setup React project, cấu hình routing, cấu hình biến môi trường (`.env`) cho API base URL
- Trang danh sách sản phẩm (hiển thị, tìm kiếm, lọc, sắp xếp)
- Trang chi tiết sản phẩm
- Giỏ hàng + Checkout (đặt hàng)
- Trang "Đơn hàng của tôi" (xem danh sách đơn, trạng thái, hủy đơn)
- Đăng nhập / đăng ký, quản lý trạng thái đăng nhập
- Xử lý trạng thái loading/error khi gọi API (tránh giao diện "đứng hình" khi lỗi)
- Áp dụng responsive cơ bản cho các trang chính

---

## TUẦN 5: Trang Admin + Testing

**Mục tiêu:** Hoàn thiện trang quản trị, tự kiểm thử toàn bộ hệ thống.

Đầu việc lớn:
- Trang Admin: quản lý sản phẩm/danh mục (CRUD)
- Trang Admin: quản lý đơn hàng (xem danh sách, cập nhật trạng thái)
- Viết Test Case cho toàn bộ chức năng (bao gồm cả các trường hợp đặc biệt đã nêu ở Tuần 3)
- Tự test, tìm bug, debug và fix
- Đối chiếu toàn bộ tính năng đã làm với checklist nghiệm thu (Definition of Done)

---

## TUẦN 6: Hoàn thiện + Demo + Báo cáo

**Mục tiêu:** Sản phẩm hoàn chỉnh, sẵn sàng demo.

Đầu việc lớn:
- Fix bug còn sót lại, polish giao diện
- Viết README hướng dẫn cách chạy project (backend + frontend + database)
- Chuẩn bị tài liệu/slide demo
- Demo sản phẩm trước team/leader
- So sánh ước lượng thời gian ban đầu (bảng dưới đây) với thời gian thực tế đã làm từng tuần, rút kinh nghiệm

---

## BẢNG TỔNG HỢP ƯỚC LƯỢNG (Cần điền)

| Tuần | Đầu việc lớn | Task chi tiết (tự chia nhỏ) | Ước lượng (giờ) | Ghi chú (phụ thuộc/rủi ro nếu có) |
|---|---|---|---|---|
| 2 | | | | |
| 2 | | | | |
| 3 | | | | |
| 3 | | | | |
| 4 | | | | |
| 4 | | | | |
| 5 | | | | |
| 5 | | | | |
| 6 | | | | |
| **Tổng cộng** | | | **___ giờ** | |

*(Có thể thêm/bớt số dòng tùy theo số task thực tế của mình)*

---

*File này dùng để tự lập kế hoạch — không cần làm đúng thứ tự tuyệt đối như liệt kê, miễn đảm bảo logic phụ thuộc (VD: phải có API trước khi Frontend gọi được).*
