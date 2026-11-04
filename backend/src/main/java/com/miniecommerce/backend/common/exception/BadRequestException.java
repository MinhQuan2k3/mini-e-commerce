package com.miniecommerce.backend.common.exception;
/* Dùng cho yêu cầu không hợp lệ, ví dụ:
    - size > 50.
    - Không cho phép thực hiện thao tác với Category đang ở trạng thái INACTIVE theo quy tắc D-08.
*/

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
