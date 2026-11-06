package com.miniecommerce.backend.common.exception;

import com.miniecommerce.backend.common.exception.GlobalExceptionHandler;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TestController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn400WhenBadRequestExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid request"))
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void shouldReturn401WhenUnauthorizedExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials"))
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void shouldReturn404WhenResourceNotFoundExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Product not found"))
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void shouldReturn409WhenConflictExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/conflict"))
                .andExpect(status().isConflict())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Email already exists"))
                .andExpect(jsonPath("$.errors.length()").value(0));
    }

    @Test
    void shouldReturn500WhenUnexpectedExceptionOccurs() throws Exception {
        mockMvc.perform(get("/test/internal-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"))
                .andExpect(jsonPath("$.errors.length()").value(0))
                .andExpect(content().string(
                        org.hamcrest.Matchers.not(
                                org.hamcrest.Matchers.containsString("Sensitive internal detail")
                        )
                ));
    }

    @Test
    void shouldReturn400WithFieldErrorsWhenValidationFails() throws Exception {
        String requestBody = """
                {
                    "email": "invalid-email",
                    "password": ""
                }
                """;

        mockMvc.perform(post("/test/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.length()").value(2))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("email")
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Email must be valid")
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("password")
                ))
                .andExpect(content().string(
                        org.hamcrest.Matchers.containsString("Password must not be blank")
                ));
    }

    @RestController
    static class TestController {

        @GetMapping("/test/bad-request")
        public void badRequest() {
            throw new BadRequestException("Invalid request");
        }

        @GetMapping("/test/unauthorized")
        public void unauthorized() {
            throw new UnauthorizedException("Invalid credentials");
        }

        @GetMapping("/test/not-found")
        public void notFound() {
            throw new ResourceNotFoundException("Product not found");
        }

        @GetMapping("/test/conflict")
        public void conflict() {
            throw new ConflictException("Email already exists");
        }

        @GetMapping("/test/internal-error")
        public void internalError() {
            throw new IllegalStateException("Sensitive internal detail");
        }

        @PostMapping("/test/validation")
        public void validation(@Valid @RequestBody ValidationRequest request) {
        }
    }

    record ValidationRequest(
            @Email(message = "Email must be valid")
            String email,

            @NotBlank(message = "Password must not be blank")
            String password
    ) {
    }
}
