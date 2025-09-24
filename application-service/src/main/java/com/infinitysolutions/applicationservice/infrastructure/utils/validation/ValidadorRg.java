package com.infinitysolutions.applicationservice.infrastructure.utils.validation;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Rg;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para a anotação @RgValido.
 * Delega a validação para o value object Rg, que realiza validações de formato:
 * - Aceita formatos XX.XXX.XXX-X, XXXXXXXX e XXXXXXXXX
 * - Permite dígito verificador X
 * 
 * Considera nulo como válido (permitindo composição com @NotNull/@NotBlank).
 */
public class ValidadorRg implements ConstraintValidator<RgValido, String> {

    @Override
    public boolean isValid(String rg, ConstraintValidatorContext context) {
        if (rg == null) {
            return true;
        }
        
        return Rg.isValido(rg);
    }
}
