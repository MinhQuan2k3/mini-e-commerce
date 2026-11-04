package com.miniecommerce.backend.common.response;

// Sử dụng Java record để biểu diễn DTO chỉ chứa dữ liệu, tránh phải viết constructor, getter và equals() thủ công.

public record ErrorDetail(String field, String message) {

}


