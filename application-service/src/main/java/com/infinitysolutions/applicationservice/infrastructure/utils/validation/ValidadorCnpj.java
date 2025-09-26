package com.infinitysolutions.applicationservice.infrastructure.utils.validation;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cnpj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para a anotação @CnpjValido.
 * Delega a validação para o value object Cnpj, que realiza validações completas:
 * - Verificação estrutural (14 dígitos)
 * - Rejeição de dígitos repetidos
 * - Cálculo matemático dos dígitos verificadores
 */
public class ValidadorCnpj implements ConstraintValidator<CnpjValido, String> {
    
    @Override
    public void initialize(CnpjValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        // Considera null ou vazio como inválido (diferente do CPF para manter compatibilidade)
        if (cnpj == null || cnpj.isEmpty()) {
            return false;
        }
        
        return Cnpj.isValido(cnpj);
    }
}