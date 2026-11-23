package com.miniecommerce.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import jakarta.servlet.DispatcherType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigIntegrationTest {

    private static final String ALLOWED_ORIGIN =
            "http://frontend.example.test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Test
    void shouldAllowRegistrationWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contextPath("/api")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldAllowGuestToAccessCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contextPath("/api"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRequireAuthenticationForAdminProductEndpoint()
            throws Exception {
        mockMvc.perform(get("/api/admin/products")
                        .contextPath("/api"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldAllowCorsPreflightRequestThroughSecurity() throws Exception {
        mockMvc.perform(options("/api/categories")
                        .contextPath("/api")
                        .header(HttpHeaders.ORIGIN, ALLOWED_ORIGIN)
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "GET")
                        .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                                "Content-Type,Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                        ALLOWED_ORIGIN))
                .andExpect(header().exists(
                        HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS));
    }

    @Test
    void shouldReturnJsonWhenAccessIsDenied() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        customAccessDeniedHandler.handle(
                request,
                response,
                new AccessDeniedException("Forbidden")
        );

        assertEquals(403, response.getStatus());
        assertTrue(response.getContentType().contains(
                MediaType.APPLICATION_JSON_VALUE));

        String responseBody = response.getContentAsString();

        assertTrue(responseBody.contains("\"success\":false"));
        assertTrue(responseBody.contains("\"message\":\"Access denied\""));
        assertTrue(responseBody.contains("\"errors\":[]"));
    }

    @Test
    void shouldAllowDirectAccessToErrorEndpoint() throws Exception {
        mockMvc.perform(get("/api/error")
                        .contextPath("/api"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldAllowErrorDispatcherType() throws Exception {
        MockHttpServletRequestBuilder errorRequest = get(
                "/api/admin/products"
        )
                .contextPath("/api")
                .with(request -> {
                    request.setDispatcherType(DispatcherType.ERROR);
                    return request;
                });

        mockMvc.perform(errorRequest)
                .andExpect(status().isOk());
    }
}
