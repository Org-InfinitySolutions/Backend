package com.infinitysolutions.applicationservice.old.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidadorSenha implements ConstraintValidator<SenhaValida, String> {

    private int minLength;
    private boolean requireUppercase;
    private boolean requireSpecialChar;

    @Override
    public void initialize(SenhaValida constraintAnnotation) {
        this.minLength = constraintAnnotation.minLength();
        this.requireUppercase = constraintAnnotation.requireUppercase();
        this.requireSpecialChar = constraintAnnotation.requireSpecialChar();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if ( password == null || password.isBlank()) {
            return false;
        }

        boolean isValid = password.length() >= minLength;
        boolean hasLetter = Pattern.compile("[a-zA-Z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("\\d").matcher(password).find();

        // Validar requisitos básicos (comprimento, letras e números)
        if (!isValid || !hasLetter || !hasDigit) {
            return false;
        }

        // Validar requisitos adicionais se configurados
        if (requireUppercase && !Pattern.compile("[A-Z]").matcher(password).find()) {
            return false;
        }

        if (requireSpecialChar && !Pattern.compile("[!@#$%^&*(),.?\":{}|<>]").matcher(password).find()) {
            return false;
        }

        return true;
    }
}
