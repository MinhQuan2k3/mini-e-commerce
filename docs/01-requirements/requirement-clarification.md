# Requirement Clarification

## 1. Information

| Item | Description |
|---|---|
| Project | Mini E-commerce / Inventory Management |
| Company | A-Software |
| Purpose | Clarify ambiguous requirements before system analysis, design and implementation |
---

## 2. User & Authentication

| # | Question | Answer | Decision |
|---|---|---|---|
| Q01 | Hệ thống có bao nhiêu loại tài khoản? | Có 2 loại tài khoản chính: Customer và Admin. | Hệ thống sẽ phân quyền chức năng dựa trên role của tài khoản. |
| Q02 | Khách vãng lai (Guest) có được xem sản phẩm không? | Có. Guest có thể xem danh sách sản phẩm, tìm kiếm sản phẩm và xem chi tiết sản phẩm. | Không yêu cầu đăng nhập đối với các chức năng xem sản phẩm. |
| Q03 | Guest có được thêm sản phẩm vào giỏ hàng không? | Có. Guest có thể chọn và thêm sản phẩm vào giỏ hàng. | Giỏ hàng của Guest được lưu tại LocalStorage/Session của trình duyệt. |
| Q04 | Customer có bắt buộc đăng nhập trước khi đặt hàng không? | Có. Người dùng phải đăng ký/đăng nhập để có thể đặt hàng. | Customer phải đăng nhập trước khi hoàn tất đặt hàng. |
| Q05 | Admin có được tự đăng ký tài khoản không? | Không. | Không mở API/Form đăng ký tài khoản Admin trên UI công khai. |
| Q06 | Admin account được tạo như thế nào? | Tạo thông qua Database Seed/Migration ban đầu hoặc do một Super Admin tạo từ trang Quản trị. | Trong MVP, hệ thống sẽ seed 1 tài khoản Admin mặc định khi khởi tạo Database. |
| Q07 | Đăng nhập sử dụng email, username hay cả hai? | Sử dụng Email và Password. | Email là định danh duy nhất của tài khoản và phải đúng định dạng email. |
| Q08 | Có yêu cầu Forgot Password / Reset Password không? | Có. | Người dùng gửi yêu cầu cho Admin/Support nếu quên mật khẩu. |
| Q09 | Có yêu cầu xác thực email khi đăng ký tài khoản không? | Có. | Cần xác thực email để đảm bảo đây là email hợp lệ/thật. |

---

## 3. Product

| # | Question | Answer | Decision |
|---|---|---|---|
| Q10 | Một sản phẩm cần những thông tin nào? | Product ID, Product Name, Description, Price, Quantity/Stock, Image, Category, Status, Created Date, Updated Date. | Bắt buộc nhập Product Name/ID, Price, Quantity/Stock và Category. |
| Q11 | Hệ thống có quản lý số lượng tồn kho (inventory/stock) không? | Có. | Mỗi sản phẩm có trường Quantity/Stock và Status đại diện cho trạng thái tồn kho khả dụng. |
| Q12 | Khi sản phẩm hết hàng, khách hàng có được đặt hàng không? | Không. | Nút "Thêm vào giỏ" / "Mua ngay" bị vô hiệu hóa khi Quantity/Stock = 0 và Status hiển thị "Hết hàng". |
| Q13 | Có cho phép Admin đặt sản phẩm thành Active/Inactive không? | Có. | Bổ sung trường Status dạng Enum. |
| Q14 | Khi Admin xóa sản phẩm, sản phẩm có bị xóa hoàn toàn khỏi database không? | Không. | Áp dụng Soft Delete, sử dụng Status = INACTIVE hoặc trường `deleted_at`, nhằm giữ tính toàn vẹn của dữ liệu đơn hàng cũ. |
| Q15 | Product có một hay nhiều hình ảnh? | Một hình ảnh đại diện chính (Primary Image) cho bản MVP. | Lưu đường dẫn hình ảnh dưới dạng chuỗi `image_url`. |
| Q16 | Giá sản phẩm có được phép bằng 0 hoặc nhỏ hơn 0 không? | Không. | Validation: `Price > 0`. |
| Q17 | Có cần SKU hoặc mã sản phẩm không? | Có. | SKU là chuỗi duy nhất (Unique), có thể tự động tạo hoặc do Admin nhập. |
| Q18 | Customer có thể lọc hoặc sắp xếp sản phẩm không? | Có. | Hỗ trợ filter theo category/price và sort theo price/newest/name. |

---

## 4. Category

| # | Question | Answer | Decision |
|---|---|---|---|
| Q19 | Một sản phẩm thuộc một hay nhiều danh mục? | Thuộc đúng 1 danh mục duy nhất. | Khóa ngoại Category nằm trong bảng Product. |
| Q20 | Category gồm những thông tin nào? | Category ID, Category Name, Description, Status. | Tạo cấu trúc bảng Category đơn giản. |
| Q21 | Có hỗ trợ category cha/con không? | Không. | Danh mục được tổ chức dưới dạng Flat Category List. |
| Q22 | Có được xóa Category đang có sản phẩm không? | Không. | Block thao tác xóa nếu Category vẫn còn Product thuộc về. |

---

## 5. Search Product

| # | Question | Answer | Decision |
|---|---|---|---|
| Q23 | Search sản phẩm dựa trên trường dữ liệu nào? | Product Name, SKU và Description. | Sử dụng SQL `LIKE %keyword%` hoặc Full-text Search cơ bản. |
| Q24 | Search có cần phân biệt chữ hoa/chữ thường không? | Không. | Quy đổi keyword về chữ thường trước khi query. |
| Q25 | Có yêu cầu pagination cho danh sách sản phẩm không? | Có. | Sử dụng offset-based pagination cơ bản, mặc định 10–12 sản phẩm/trang. |

---

## 6. Shopping Cart

| # | Question | Answer | Decision |
|---|---|---|---|
| Q26 | Guest có được sử dụng Shopping Cart không? | Có. | Cart của Guest được lưu tại Client bằng LocalStorage. Khi đăng nhập, Cart được đồng bộ/merge vào Cart của Customer trong Database. |
| Q27 | Cart được lưu ở đâu? | Guest: Browser/LocalStorage. Customer: Database. | Triển khai API đồng bộ Cart khi Customer đăng nhập thành công. |
| Q28 | Khi Customer thêm cùng một sản phẩm nhiều lần, hệ thống xử lý thế nào? | Tăng số lượng của `CartItem` đã tồn tại. | Tạo ràng buộc Unique Pair `(cart_id, product_id)` trong bảng `CartItem`. |
| Q29 | Quantity trong Cart có giới hạn theo số lượng tồn kho không? | Có. | Báo lỗi/chặn thao tác nếu `quantity_in_cart > product.stock_quantity`. |
| Q30 | Nếu sản phẩm trong Cart bị Admin inactive hoặc hết hàng thì xử lý thế nào? | Hiển thị trạng thái "Không khả dụng" trên UI Giỏ hàng. | Chặn chuyển sang Checkout nếu Cart chứa sản phẩm không khả dụng. |

---

## 7. Checkout

| # | Question | Answer | Decision |
|---|---|---|---|
| Q31 | Checkout cần những thông tin giao hàng nào? | Full Name, Phone Number, Address, Province/City, District/Ward. | Thông tin giao hàng được nhập trực tiếp dưới dạng chuỗi văn bản đơn giản. |
| Q32 | Customer có được lưu nhiều địa chỉ giao hàng không? | Không. | Customer nhập thông tin giao hàng trực tiếp mỗi lần Checkout. |
| Q33 | Có cần lựa chọn phương thức giao hàng không? | Không. | Sử dụng mặc định một hình thức "Giao hàng tiêu chuẩn". |
| Q34 | Có yêu cầu mã giảm giá / coupon không? | Out-of-scope. | Không phát triển tính năng này trong MVP. |

---

## 8. Payment

| # | Question | Answer | Decision |
|---|---|---|---|
| Q35 | Hệ thống có hỗ trợ thanh toán online không? | Có. | Tích hợp cổng thanh toán bên ngoài. |
| Q36 | Nếu có thanh toán, những phương thức nào được hỗ trợ? | COD, Bank Transfer, E-wallet và Online Payment Gateway. | Mặc định chọn COD. |
| Q37 | Nếu không tích hợp payment gateway, Order có được tạo ngay sau khi Customer xác nhận Checkout không? | Có. | Đơn hàng được tạo ở trạng thái `PENDING` ngay khi Customer nhấn "Đặt hàng". |

---

## 9. Order

| # | Question | Answer | Decision |
|---|---|---|---|
| Q38 | Một Order cần lưu những thông tin nào? | Order ID, Customer, Order Items, Total Amount, Shipping Information, Order Status, Payment Status, Created Date, Updated Date. | Tạo cấu trúc bảng `Order` và `OrderItem`. |
| Q39 | Order có những trạng thái nào? | `PENDING`, `CONFIRMED`, `SHIPPING`, `DELIVERED`, `CANCELLED`. | Sử dụng Enum cho trạng thái đơn hàng. |
| Q40 | Customer có được hủy Order không? | Có. | Chỉ cho phép hủy khi Order ở trạng thái `PENDING`. |
| Q41 | Admin có được thay đổi trạng thái Order không? | Có. | Admin có quyền cập nhật Order theo luồng trạng thái được phép. |
| Q42 | Admin có được chỉnh sửa Order sau khi Customer đã đặt hàng không? | Không. | Admin chỉ được thay đổi trạng thái, không được chỉnh sửa Product/Quantity hoặc địa chỉ của Order. |
| Q43 | Order có cần lưu lại giá sản phẩm tại thời điểm mua không? | Bắt buộc. | Lưu trường `price` trực tiếp trong bảng `OrderItem` để bảo toàn lịch sử giá. |
| Q44 | Khi Order được tạo, tồn kho có được trừ ngay không? | Có. | Giảm `stock_quantity` ngay khi Checkout thành công. |
| Q45 | Nếu Order bị hủy, số lượng tồn kho có được hoàn lại không? | Có. | Cộng ngược số lượng vào `stock_quantity` khi Order chuyển sang `CANCELLED`. |

---

## 10. Customer Order History

| # | Question | Answer | Decision |
|---|---|---|---|
| Q46 | Customer có được xem danh sách các Order của chính mình không? | Có. | Cung cấp API `GET /api/orders/my-orders`. |
| Q47 | Customer có được xem chi tiết từng Order không? | Có. | Cung cấp API `GET /api/orders/{id}` và kiểm tra quyền sở hữu Order. |
| Q48 | Customer có được xem lại các Order đã hủy hoặc đã hoàn thành không? | Có. | Màn hình danh sách Order hỗ trợ filter theo status. |

---

## 11. Admin - Product Management

| # | Question | Answer | Decision |
|---|---|---|---|
| Q49 | Admin có thể thực hiện những thao tác nào với Product? | View, Search, Create, Update, Delete (Soft Delete), Activate/Deactivate. | Cấp đầy đủ API quản trị Product cho Admin. |
| Q50 | Admin có được quản lý số lượng tồn kho trực tiếp không? | Có. | Admin cập nhật `stock_quantity` thông qua màn hình Edit Product. |

---

## 12. Admin - Category Management

| # | Question | Answer | Decision |
|---|---|---|---|
| Q51 | Admin có thể thực hiện những thao tác nào với Category? | View, Create, Update, Delete. Category chỉ được xóa khi không có Product thuộc về. | Cấp đầy đủ API quản trị Category cho Admin. |

---

## 13. Admin - Order Management

| # | Question | Answer | Decision |
|---|---|---|---|
| Q52 | Admin có thể xem toàn bộ Order của hệ thống không? | Có. | Cung cấp API `GET /api/admin/orders`. |
| Q53 | Admin có thể tìm kiếm Order theo những thông tin nào? | Order ID, Customer Name, Customer Email và Phone Number. | Bổ sung parameter search vào API Admin Order. |
| Q54 | Admin có thể filter Order theo status không? | Có. | Hỗ trợ lọc Order theo trạng thái. |
| Q55 | Admin có thể xem chi tiết Order không? | Có. | Hiển thị thông tin người nhận, danh sách Item và giá trị Order. |

---

## 14. Authorization & Security

| # | Question | Answer | Decision |
|---|---|---|---|
| Q56 | Customer có được phép truy cập Admin page/API không? | Không. | Các chức năng Admin phải được bảo vệ bằng cơ chế authorization và kiểm tra role ở Backend. |
| Q57 | Admin có được truy cập các chức năng dành riêng cho Customer không? | Được phép xem các trang public như Product List và Product Detail. | Nếu muốn mua hàng, Admin nên sử dụng một tài khoản Customer riêng. |
| Q58 | Password được lưu trữ như thế nào? | Hashing bảo mật. | Sử dụng `BCrypt` hoặc `Argon2`; không lưu password dạng plain text. |
| Q59 | Backend sử dụng cơ chế authentication nào? | JWT (JSON Web Token). | Sử dụng Stateless Authentication. |

---

## 15. UI / UX

| # | Question | Answer | Decision |
|---|---|---|---|
| Q60 | Website có yêu cầu responsive trên mobile/tablet không? | Có. | Thiết kế Responsive UI cơ bản bằng Tailwind CSS hoặc Bootstrap. |
| Q61 | Có yêu cầu giao diện đặc biệt về branding của A-Software không? | Không. | Sử dụng giao diện E-commerce hiện đại, sạch sẽ và tập trung vào usability. |
| Q62 | Có cần hiển thị các trạng thái Empty / Loading / Error cho các màn hình không? | Có. | Thiết kế UI component nhất quán cho các trạng thái này. |

---

## 16. Non-functional Requirements

| # | Question | Answer | Decision |
|---|---|---|---|
| Q63 | Có yêu cầu cụ thể về performance không? | Mức tiêu chuẩn. | Mục tiêu thời gian phản hồi API < 500ms cho các tác vụ thông thường. |
| Q64 | Có yêu cầu cụ thể về số lượng người dùng hoặc quy mô dữ liệu không? | Quy mô nhỏ/Demo project. | Hướng tới quy mô < 1.000 sản phẩm và < 100 người dùng truy cập đồng thời. |
| Q65 | Những trình duyệt nào cần được hỗ trợ? | Chrome, Firefox, Edge, Safari. | Hỗ trợ các trình duyệt hiện đại theo chuẩn web hiện hành. |
| Q66 | Có yêu cầu logging/auditing cho các thao tác của Admin không? | Out-of-scope. | Chỉ thực hiện application logging cho lỗi ở phía Server bằng Console/File Logs. |

---

## 17. Testing & Error Handling

| # | Question | Answer | Decision |
|---|---|---|---|
| Q67 | Có yêu cầu viết unit test cho Backend không? | Có, ở mức cơ bản. | Viết Unit Test cho các Business Logic quan trọng như Checkout, trừ tồn kho và tính tổng tiền. |
| Q68 | Có yêu cầu integration test hoặc API test không? | Sử dụng Postman / Swagger Collection để kiểm thử API. | Chuẩn bị Postman Collection hỗ trợ kiểm thử API. |
| Q69 | API error response có cần một format thống nhất không? | Có. | Sử dụng format thống nhất: `success`, `message` và `errors`. |
| Q70 | Khi thao tác thất bại, UI cần hiển thị thông báo lỗi như thế nào? | Toast notification hoặc Alert banner. | Sử dụng Toast Message thống nhất trên UI. |

### Standard API Error Response

```json
{
  "success": false,
  "message": "Error description",
  "errors": []
}
```

---

## 18. Scope & Constraints

| # | Question | Answer | Decision |
|---|---|---|---|
| Q71 | Trong phạm vi 6 tuần, mức độ hoàn thiện mong muốn của hệ thống là MVP hay production-ready? | MVP (Minimum Viable Product) hoạt động hoàn chỉnh end-to-end. | Tập trung hoàn thiện 100% các luồng nghiệp vụ chính. |
| Q72 | Có yêu cầu deploy hệ thống lên server/cloud không? | Có. | Deploy lên nền tảng miễn phí/chi phí thấp như Render, Vercel, Supabase hoặc Docker. |
| Q73 | Có yêu cầu CI/CD không? | Out-of-scope hoặc có thể triển khai đơn giản bằng GitHub Actions. | CI/CD không bắt buộc đối với đánh giá chính của dự án. |

---

# 19. Requirement Clarification Summary

### Customer / Guest

- Guest xem danh sách sản phẩm.
- Guest tìm kiếm sản phẩm.
- Guest xem chi tiết sản phẩm.
- Guest thêm sản phẩm vào Cart.
- Guest Cart được lưu trên Client.
- Customer đăng ký/đăng nhập.
- Guest Cart được merge vào Customer Cart sau khi login.
- Customer Checkout và đặt hàng.
- Customer cung cấp thông tin giao hàng khi Checkout.
- Customer xem lịch sử Order.
- Customer xem Order Detail.
- Customer chỉ được Cancel Order ở trạng thái `PENDING`.

### Admin

- Admin account không được đăng ký công khai.
- Một Admin mặc định được seed khi khởi tạo Database.
- Admin quản lý Product.
- Admin quản lý Category.
- Admin quản lý Order.
- Admin cập nhật Order Status.
- Admin quản lý Stock.
- Admin không được chỉnh sửa nội dung Order đã tạo.

### Product & Inventory

- Product thuộc đúng một Category.
- Product có Stock.
- Product có SKU unique.
- Product có một Primary Image trong MVP.
- Product có Active/Inactive status.
- Product sử dụng Soft Delete.
- Không cho phép đặt hàng khi hết hàng.

### Order

- Order có `OrderItem`.
- `OrderItem` lưu giá tại thời điểm mua.
- Stock được trừ khi Order được tạo thành công.
- Stock được hoàn lại khi Order bị Cancel.
- Order Status gồm:
  - `PENDING`
  - `CONFIRMED`
  - `SHIPPING`
  - `DELIVERED`
  - `CANCELLED`

### MVP Out-of-scope

- Coupon / Promotion
- Admin auditing
- Các chức năng E-commerce nâng cao chưa được xác định trong scope
- CI/CD bắt buộc
