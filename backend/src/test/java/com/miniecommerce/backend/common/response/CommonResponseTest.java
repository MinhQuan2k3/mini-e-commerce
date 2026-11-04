package com.miniecommerce.backend.common.response;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CommonResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        ApiResponse<String> response = ApiResponse.success(
                "Request successful",
                "Hello"
        );

        assertTrue(response.success());
        assertEquals("Request successful", response.message());
        assertEquals("Hello", response.data());
        assertNull(response.errors());
    }

    @Test
    void shouldCreateFailureResponseWithEmptyErrors() {
        ApiResponse<Void> response = ApiResponse.failure(
                "Resource not found"
        );

        assertFalse(response.success());
        assertEquals("Resource not found", response.message());
        assertNull(response.data());
        assertNotNull(response.errors());
        assertTrue(response.errors().isEmpty());
    }

    @Test
    void shouldCreateFailureResponseWithValidationErrors() {
        List<ErrorDetail> errors = List.of(
                new ErrorDetail("email", "Email must be valid"),
                new ErrorDetail("password", "Password must not be blank")
        );

        ApiResponse<Void> response = ApiResponse.failure(
                "Validation failed",
                errors
        );

        assertFalse(response.success());
        assertEquals("Validation failed", response.message());
        assertNull(response.data());
        assertEquals(2, response.errors().size());

        assertEquals("email", response.errors().get(0).field());
        assertEquals(
                "Email must be valid",
                response.errors().get(0).message()
        );

        assertEquals("password", response.errors().get(1).field());
        assertEquals(
                "Password must not be blank",
                response.errors().get(1).message()
        );
    }

    @Test
    void shouldConvertSpringPageToPageResponse() {
        List<String> content = List.of("Product A", "Product B");

        Page<String> page = new PageImpl<>(
                content,
                PageRequest.of(1, 2),
                5
        );

        PageResponse<String> response = PageResponse.from(page);

        assertEquals(content, response.content());
        assertEquals(1, response.page());
        assertEquals(2, response.size());
        assertEquals(5L, response.totalElements());
        assertEquals(3, response.totalPages());
    }
}
