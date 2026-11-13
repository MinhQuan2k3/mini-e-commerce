package com.miniecommerce.backend.auth.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegisterRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void tearDown() {
        validatorFactory.close();
    }

    @Test
    void shouldAcceptPasswordWithExactlyEightCharacters() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "12345678");

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectPasswordWithFewerThanEightCharacters() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "1234567");

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptPasswordWithExactlySeventyTwoUtf8Bytes() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "a".repeat(72));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectPasswordWithMoreThanSeventyTwoUtf8Bytes() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "a".repeat(73));

        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void shouldAcceptUnicodePasswordWithSeventyTwoUtf8Bytes() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "é".repeat(36));

        assertTrue(validator.validate(request).isEmpty());
    }

    @Test
    void shouldRejectUnicodePasswordWithMoreThanSeventyTwoUtf8Bytes() {
        RegisterRequest request =
                new RegisterRequest("customer@example.com", "é".repeat(37));

        assertFalse(validator.validate(request).isEmpty());
    }
}
