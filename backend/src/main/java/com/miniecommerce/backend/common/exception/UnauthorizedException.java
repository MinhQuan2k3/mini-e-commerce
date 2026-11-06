package com.miniecommerce.backend.common.exception;
// Dùng khi thông tin xác thực không hợp lệ, ví dụ sai email hoặc mật khẩu.

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
