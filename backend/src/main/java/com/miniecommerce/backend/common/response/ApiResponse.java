package com.miniecommerce.backend.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        List<ErrorDetail> errors
) {

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(
                true,
                message,
                data,
                null
        );
    }

    public static <T> ApiResponse<T> failure(
            String message,
            List<ErrorDetail> errors
    ) {
        return new ApiResponse<>(
                false,
                message,
                null,
                errors
        );
    }

    public static <T> ApiResponse<T> failure(String message) {
        return failure(message, List.of());
    }
}
