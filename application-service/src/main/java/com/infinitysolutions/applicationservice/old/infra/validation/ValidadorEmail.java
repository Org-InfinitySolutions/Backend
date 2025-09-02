package com.infinitysolutions.applicationservice.old.infra.validation;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador de email que delega a validação para o value object Email.
 * 
 * Este validador foi refatorado para usar o value object Email do domínio,
 * garantindo consistência e reutilização das regras de validação.
 */
public class ValidadorEmail implements ConstraintValidator<EmailValido, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return false;
        }
        
        // Delega a validação para o value object Email
        return Email.isValid(email);
    }
}
