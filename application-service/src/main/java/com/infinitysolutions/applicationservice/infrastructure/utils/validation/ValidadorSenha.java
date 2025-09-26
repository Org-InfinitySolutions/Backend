package com.infinitysolutions.applicationservice.infrastructure.utils.validation;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Senha;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de senha que delega a validação para o value object Senha.
 * 
 * Este validador foi refatorado para usar o value object Senha do domínio,
 * garantindo consistência e reutilização das regras de validação.
 */
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
        if (password == null || password.isBlank()) {
            return false;
        }

        // Delega a validação para o value object Senha
        return Senha.isValid(password, minLength, requireUppercase, requireSpecialChar);
    }
}
