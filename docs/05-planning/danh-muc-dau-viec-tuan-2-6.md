# Danh mục Đầu việc Tuần 2 - Tuần 6 — Dự án Mini E-commerce

## Hướng dẫn sử dụng file này

Đây là danh sách các **đầu việc lớn (epic)** cần hoàn thành trong 5 tuần còn lại của dự án, dựa trên thiết kế em đã làm ở Tuần 1. File này **không chia sẵn theo ngày** — nhiệm vụ của em là:

1. Với mỗi đầu việc lớn bên dưới, **tự chia nhỏ thành các task chi tiết hơn** (mỗi task nên đủ nhỏ để làm trong khoảng 0.5 ngày đến 1 ngày).
2. **Tự ước lượng số giờ** cần để hoàn thành từng task, dựa trên: độ phức tạp, việc có cần học thêm công nghệ mới không, có phụ thuộc vào task khác không (Estimate theo giờ).
3. Điền toàn bộ vào **Bảng tổng hợp ước lượng** ở cuối file.
4. Gửi lại bảng này cho anh để trao đổi và thống nhất kế hoạch trước khi bắt đầu Tuần 2.

> Việc ước lượng không cần chính xác tuyệt đối — mục đích là để em tập thói quen lập kế hoạch, và để anh cùng em theo dõi tiến độ thực tế so với dự kiến trong các tuần tiếp theo.

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

## BẢNG TỔNG HỢP ƯỚC LƯỢNG (Em tự điền)

| Tuần | Đầu việc lớn | Task chi tiết (tự chia nhỏ) | Ước lượng (giờ) | Ghi chú (phụ thuộc/rủi ro nếu có) |
|---|---|---|---:|---|
| 2 | Setup Backend | Khởi tạo Spring Boot project, cấu hình dependencies và cấu trúc package | 2 | Nền tảng cho toàn bộ Backend |
| 2 | Database & JPA | Cấu hình kết nối MySQL, JPA/Hibernate và kiểm tra kết nối | 3 | Phụ thuộc database đã chuẩn bị |
| 2 | Configuration | Cấu hình environment variables và bảo vệ thông tin kết nối, không commit secret | 2 | Cần thống nhất cách cấu hình local |
| 2 | Entity | Tạo Entity User, Product, Category, Cart, CartItem, Order, OrderItem và mapping quan hệ | 4 | Phụ thuộc ERD và Class Diagram |
| 2 | Repository | Tạo Repository cho các Entity chính | 2 | Phụ thuộc Entity |
| 2 | Global Exception Handling | Xây dựng Global Exception Handler và chuẩn hóa success/error response | 3 | Phải thống nhất format với API Specification trước khi implement CRUD |
| 2 | CORS | Cấu hình CORS cho phép Frontend gọi API | 1 | Cần thiết cho Tuần 4 |
| 2 | Category CRUD | Implement API tạo, xem, cập nhật và vô hiệu hóa Category theo business rule | 4 | Category không được xóa nếu đang được Product tham chiếu; dùng INACTIVE để ẩn |
| 2 | Product CRUD | Implement Admin Product API: list/detail, tạo, cập nhật, soft delete và validation | 7 | Bao gồm ACTIVE/INACTIVE, search/filter cơ bản và validation |
| 2 | Seed Data | Tạo dữ liệu mẫu: Category, Product và tài khoản Admin mặc định | 2 | Phụ thuộc Entity và Repository |
| 2 | Authentication cơ bản | Implement Register/Login, password hashing và kiểm tra thông tin đăng nhập | 8 | Bao gồm validation password tối thiểu 8 ký tự; là nền tảng cho JWT |
| 2 | API Test Setup | Chuẩn bị Postman/Swagger collection cơ bản và test các API nền tảng | 1 | Tạo sớm để hỗ trợ kiểm thử Backend trong các tuần sau |
|  | **Tổng Tuần 2** | | **39 giờ** | |
| 3 | Spring Security | Cấu hình Spring Security và authentication flow | 3 | Phụ thuộc Authentication cơ bản Tuần 2 |
| 3 | JWT | Implement tạo, validate và parse JWT | 4 | Phụ thuộc Spring Security |
| 3 | Authorization | Phân quyền ADMIN/CUSTOMER và bảo vệ các endpoint | 3 | Phụ thuộc JWT |
| 3 | Cart | Tạo API lấy/tạo Cart và thêm Product vào Cart | 4 | Phụ thuộc User/Product |
| 3 | Cart | Cập nhật số lượng và xóa CartItem | 3 | Phụ thuộc Cart |
| 3 | Cart Validation | Kiểm tra Product ACTIVE, quantity > 0 và quantity không vượt stock | 2 | Phụ thuộc Product/Cart |
| 3 | Checkout | Tạo Order + OrderItem từ Cart và lưu snapshot thông tin sản phẩm | 4 | Phụ thuộc Cart |
| 3 | Checkout | Tính tổng tiền và validate shipping information | 2 | Shipping gồm Recipient Name, Phone, Address, Province/City, District, Ward |
| 3 | Inventory Transaction | Trừ stock bằng conditional update, kiểm tra affected rows và rollback toàn bộ Checkout nếu thất bại | 5 | Quan trọng; cần xử lý concurrency và transaction |
| 3 | Customer Order | Implement API xem danh sách và chi tiết Order của Customer | 3 | Có ownership check; Customer chỉ xem Order của chính mình |
| 3 | Cancel Order | Hủy Order PENDING và restore stock đúng một lần trong cùng transaction | 4 | Không được restore stock lần thứ hai nếu Order đã CANCELLED |
| 3 | Product Search | Implement search theo Name/SKU/Description, filter, sort và pagination | 3 | Keyword không phân biệt hoa thường; page mặc định 10, tối đa 50 |
| 3 | Admin Order | Implement API Admin Order list/search/filter theo Order ID, Customer Email, Recipient Name, Phone | 3 | Phụ thuộc Order/User và shipping snapshot |
| 3 | Admin Order | Implement Admin Order Detail và cập nhật Order Status theo transition hợp lệ | 3 | PENDING → CONFIRMED → SHIPPING → DELIVERED; PENDING → CANCELLED |
| 3 | Admin Order | Xử lý Admin Cancel: restore stock transactional và cập nhật payment rule khi DELIVERED | 3 | DELIVERED tự động chuyển paymentStatus=PAID và ghi paidAt |
| 3 | Edge Cases | Xử lý Cart rỗng, hết hàng, Product INACTIVE, invalid input và các lỗi nghiệp vụ | 2 | Phụ thuộc các nghiệp vụ tương ứng |
| 3 | Unit Test | Viết Unit Test cho validation, tính toán, stock, Cancel Order và Order Status Transition | 3 | Tập trung vào business logic quan trọng |
| 3 | API Test | Cập nhật Postman/Swagger collection và kiểm thử các API Backend mới | 2 | Dùng để test Backend độc lập |
|  | **Tổng Tuần 3** | | **53 giờ** | |
| 4 | Frontend Setup | Khởi tạo React project, routing và cấu hình environment/API base URL | 3 | Phụ thuộc Backend API |
| 4 | API Layer | Xây dựng API client/service layer để gọi Backend | 3 | Phụ thuộc API Specification |
| 4 | Authentication UI | Xây dựng Login/Register và kết nối API | 4 | Phụ thuộc Auth API |
| 4 | Authentication State | Quản lý trạng thái đăng nhập, JWT và protected routes | 4 | Phụ thuộc Login/JWT |
| 4 | Product List | Xây dựng trang danh sách sản phẩm | 4 | Phụ thuộc Product API |
| 4 | Product Search/Filter | Kết nối search, filter, sort và pagination | 3 | Phụ thuộc Product List API |
| 4 | Product Detail | Xây dựng trang chi tiết sản phẩm + Add to Cart + trạng thái unavailable/out-of-stock | 3 | Phụ thuộc Product API và Cart API |
| 4 | Cart UI | Xây dựng giỏ hàng + chỉnh sửa quantity + xóa sản phẩm + availability state | 4 | Disable Checkout khi Cart không hợp lệ |
| 4 | Checkout UI | Xây dựng Checkout + shipping information + Order Summary | 4 | Không có payment method selection |
| 4 | My Orders | Xây dựng Order History + Order Detail + trạng thái + Cancel khi PENDING | 4 | Phụ thuộc Order API |
| 4 | Loading/Error State | Xử lý loading, error, empty state và thông báo kết quả API | 2 | Áp dụng cho các màn hình chính |
| 4 | Responsive | Responsive cơ bản cho các trang Customer chính | 3 | Không yêu cầu UI quá phức tạp |
|  | **Tổng Tuần 4** | | **41 giờ** | |
| 5 | Admin Layout | Xây dựng Admin layout, navigation và protected route | 3 | Phụ thuộc JWT/RBAC |
| 5 | Admin Product | Trang quản lý Product: danh sách, tìm kiếm/lọc và thao tác CRUD | 5 | Phụ thuộc Admin Product API |
| 5 | Admin Product Form | Form tạo/cập nhật Product + validation | 3 | Phụ thuộc Product API |
| 5 | Admin Category | Trang quản lý Category + CRUD + trạng thái ACTIVE/INACTIVE | 4 | Phụ thuộc Category API |
| 5 | Admin Order | Trang danh sách Order + search/filter | 3 | Phụ thuộc Admin Order API |
| 5 | Admin Order Detail | Xem chi tiết Order và cập nhật trạng thái theo transition hợp lệ | 4 | Phụ thuộc Order Status Rules |
| 5 | Test Case | Viết Test Case cho Customer, Admin và các edge cases | 5 | Phụ thuộc chức năng chính đã hoàn thành |
| 5 | Execute Testing | Thực thi Test Case, ghi nhận kết quả và tạo danh sách bug | 3 | Phụ thuộc Test Case |
| 5 | Debug & Fix | Phân tích, sửa bug và regression test các chức năng bị ảnh hưởng | 4 | Estimate có thể tăng nếu phát sinh bug phức tạp |
| 5 | DoD Review | Đối chiếu toàn bộ chức năng với Definition of Done/checklist nghiệm thu | 3 | Thực hiện cuối tuần |
|  | **Tổng Tuần 5** | | **37 giờ** | |
| 6 | Regression Testing | Test lại toàn bộ Customer/Admin flow sau các thay đổi Tuần 5 | 4 | Phụ thuộc kết quả Tuần 5 |
| 6 | Final Bug Fix | Fix các bug còn sót lại và regression test | 5 | Có rủi ro tăng thời gian nếu còn bug nghiêm trọng |
| 6 | UI Polish | Chỉnh layout, spacing, consistency và các lỗi UI nhỏ | 3 | Không mở rộng thêm feature |
| 6 | Project Configuration | Kiểm tra lại configuration và khả năng chạy Backend/Frontend/Database | 2 | Phụ thuộc environment setup |
| 6 | README | Viết README hướng dẫn setup và chạy toàn bộ project | 3 | Phụ thuộc cấu hình cuối cùng |
| 6 | Demo Preparation | Chuẩn bị kịch bản và dữ liệu cho demo | 3 | Bao phủ các Customer/Admin flow chính |
| 6 | Slides/Documentation | Chuẩn bị slide và tài liệu phục vụ demo | 3 | Nội dung lấy từ project hiện tại |
| 6 | Demo & Feedback | Demo sản phẩm, ghi nhận feedback và thực hiện chỉnh sửa nhỏ nếu cần | 3 | Tăng từ estimate cũ 1h để có thời gian xử lý feedback |
| 6 | Retrospective | So sánh Estimate với Actual Time và ghi nhận bài học kinh nghiệm | 1 | Thực hiện cuối dự án |
| 6 | Buffer | Dự phòng cho bug phát sinh, integration issue, rework và các task vượt estimate | 5 | Không dùng để mở rộng scope; chỉ sử dụng khi có phát sinh |
|  | **Tổng Tuần 6** | | **36 giờ** | |
| **Tổng cộng** | | | **206 giờ** | **Baseline Estimate sau khi cập nhật theo review và scope đã chốt** |

*(Em có thể thêm/bớt số dòng tùy theo số task thực tế của mình)*

---

*File này dùng để em tự lập kế hoạch — không cần làm đúng thứ tự tuyệt đối như liệt kê, miễn đảm bảo logic phụ thuộc (VD: phải có API trước khi Frontend gọi được).*
