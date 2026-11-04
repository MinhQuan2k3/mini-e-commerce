package com.miniecommerce.backend.common.exception;
// Dùng khi không tìm thấy tài nguyên được yêu cầu, ví dụ Product hoặc Category không tồn tại.

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
