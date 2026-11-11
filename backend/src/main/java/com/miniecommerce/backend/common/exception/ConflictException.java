package com.miniecommerce.backend.common.exception;
/* Dùng khi thao tác xung đột với dữ liệu hiện có hoặc quy tắc nghiệp vụ, ví dụ:
    - Email đã tồn tại.
    - SKU đã tồn tại.
    - Tên Category đã tồn tại.
    - Category đang được sử dụng nên không thể xóa.
*/

public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
