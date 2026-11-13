package com.miniecommerce.backend.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.nio.charset.StandardCharsets;

public class PasswordByteLengthValidator
        implements ConstraintValidator<PasswordByteLength, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            return true;
        }

        int byteLength = password.getBytes(StandardCharsets.UTF_8).length;
        return byteLength <= 72;
    }
}
