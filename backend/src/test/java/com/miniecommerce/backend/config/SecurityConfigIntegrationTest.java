package com.miniecommerce.backend.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigIntegrationTest {

    private static final String ALLOWED_ORIGIN =
            "http://frontend.example.test";

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(status().isUnauthorized());
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
}
