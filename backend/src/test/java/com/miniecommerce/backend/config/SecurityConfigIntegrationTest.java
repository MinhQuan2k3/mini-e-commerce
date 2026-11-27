package com.miniecommerce.backend.config;

import com.miniecommerce.backend.auth.service.JwtService;
import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityConfigIntegrationTest {

    private static final String ALLOWED_ORIGIN =
            "http://frontend.example.test";

    private static final String AUTH_ME_ENDPOINT = "/api/auth/me";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtEncoder jwtEncoder;

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
    void shouldPermitErrorDispatchForOriginalApplicationError()
            throws Exception {
        mockMvc.perform(get("/api/error")
                        .contextPath("/api")
                        .requestAttr(
                                RequestDispatcher.ERROR_STATUS_CODE,
                                HttpStatus.INTERNAL_SERVER_ERROR.value()
                        )
                        .requestAttr(
                                RequestDispatcher.ERROR_REQUEST_URI,
                                "/api/admin/products"
                        )
                        .requestAttr(
                                RequestDispatcher.ERROR_MESSAGE,
                                "Internal Server Error"
                        )
                        .with(request -> {
                            request.setDispatcherType(DispatcherType.ERROR);
                            return request;
                        }))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.path")
                        .value("/api/admin/products"));
    }

    @Test
    void shouldReturnUnauthorizedWhenBearerTokenIsMissing()
            throws Exception {
        mockMvc.perform(get(AUTH_ME_ENDPOINT)
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
    void shouldReturnUnauthorizedWhenBearerTokenIsMalformed()
            throws Exception {
        mockMvc.perform(get(AUTH_ME_ENDPOINT)
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer not-a-valid-jwt"
                        ))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldReturnCurrentUserWhenBearerTokenIsValid()
            throws Exception {
        User user = createUser(UserRole.CUSTOMER);
        String token = jwtService.generateToken(user);

        mockMvc.perform(get(AUTH_ME_ENDPOINT)
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id")
                        .value(user.getId()))
                .andExpect(jsonPath("$.data.email")
                        .value(user.getEmail()))
                .andExpect(jsonPath("$.data.role")
                        .value("CUSTOMER"))
                .andExpect(jsonPath("$.data.passwordHash").doesNotExist())
                .andExpect(jsonPath("$.data.password_hash").doesNotExist());
    }

    @Test
    void shouldReturnUnauthorizedWhenJwtIsExpired()
            throws Exception {
        String expiredToken = createExpiredToken();

        mockMvc.perform(get(AUTH_ME_ENDPOINT)
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + expiredToken
                        ))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void shouldReturnUnauthorizedWhenJwtSignatureIsInvalid()
            throws Exception {
        String invalidToken = createTokenWithInvalidSignature();

        mockMvc.perform(get(AUTH_ME_ENDPOINT)
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + invalidToken
                        ))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message")
                        .value("Authentication required"))
                .andExpect(jsonPath("$.errors").isArray());
    }


    @Test
    void shouldForbidCustomerFromAccessingAdminEndpoint()
            throws Exception {
        User customer = createUser(UserRole.CUSTOMER);
        String token = jwtService.generateToken(customer);

        mockMvc.perform(get("/api/admin/products")
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldAllowAdminToAccessAdminEndpoint()
            throws Exception {
        User admin = createUser(UserRole.ADMIN);
        String token = jwtService.generateToken(admin);

        mockMvc.perform(get("/api/admin/categories")
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isOk());
    }

    @Test
    void shouldForbidAdminFromAccessingCartEndpoint()
            throws Exception {
        User admin = createUser(UserRole.ADMIN);
        String token = jwtService.generateToken(admin);

        mockMvc.perform(get("/api/cart")
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldForbidAdminFromAccessingOrderEndpoint()
            throws Exception {
        User admin = createUser(UserRole.ADMIN);
        String token = jwtService.generateToken(admin);

        mockMvc.perform(get("/api/orders")
                        .contextPath("/api")
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                "Bearer " + token
                        ))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    private User createUser(UserRole role) {
        String email = "security-test-" + UUID.randomUUID()
                + "@example.com";

        return userRepository.saveAndFlush(
                new User(
                        email,
                        "test-password-hash",
                        role
                )
        );
    }

    private String createExpiredToken() {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("1")
                .issuedAt(now.minusSeconds(7200))
                .expiresAt(now.minusSeconds(3600))
                .claim("role", "CUSTOMER")
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }

    private String createTokenWithInvalidSignature() {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("1")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("role", "CUSTOMER")
                .build();

        byte[] invalidKeyBytes = new byte[32];

        JwtEncoder invalidJwtEncoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(
                        new SecretKeySpec(
                                invalidKeyBytes,
                                "HmacSHA256"
                        )
                )
        );

        return invalidJwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}
