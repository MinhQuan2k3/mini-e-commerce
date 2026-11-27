package com.miniecommerce.backend.auth.service;

import com.miniecommerce.backend.user.entity.User;
import com.miniecommerce.backend.user.entity.UserRole;
import com.miniecommerce.backend.user.repository.UserRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGenerateAndDecodeValidToken() {
        User user = userRepository.saveAndFlush(
                new User(
                        "jwt-service-test@example.com",
                        "password-hash",
                        UserRole.CUSTOMER
                )
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isBlank());

        Jwt decodedJwt = jwtDecoder.decode(token);

        assertEquals(user.getId().toString(), decodedJwt.getSubject());
        assertEquals("CUSTOMER", decodedJwt.getClaimAsString("role"));
        assertNotNull(decodedJwt.getIssuedAt());
        assertNotNull(decodedJwt.getExpiresAt());

        assertTrue(decodedJwt.getExpiresAt().isAfter(Instant.now()));

        assertEquals(3600L, jwtService.getExpiration());
    }

    @Test
    void shouldRejectExpiredToken() {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("1")
                .issuedAt(now.minusSeconds(7200))
                .expiresAt(now.minusSeconds(3600))
                .claim("role", "CUSTOMER")
                .build();

        String expiredToken = jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();

        assertThrows(
                JwtException.class,
                () -> jwtDecoder.decode(expiredToken)
        );
    }

    @Test
    void shouldRejectTokenWithInvalidSignature() {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject("1")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("role", "CUSTOMER")
                .build();

        byte[] invalidKeyBytes = new byte[32];

        JwtEncoder invalidJwtEncoder = new org.springframework.security.oauth2.jwt.NimbusJwtEncoder(
                new ImmutableSecret<>(
                        new SecretKeySpec(
                                invalidKeyBytes,
                                "HmacSHA256"
                        )
                )
        );

        String invalidSignatureToken = invalidJwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();

        assertThrows(
                JwtException.class,
                () -> jwtDecoder.decode(invalidSignatureToken)
        );
    }
}
