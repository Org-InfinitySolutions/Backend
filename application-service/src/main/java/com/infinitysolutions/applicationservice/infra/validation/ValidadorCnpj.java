package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidadorCnpj implements ConstraintValidator<CnpjValido, String> {

    // Padrão para validar o formato do CNPJ (com separadores)
    private static final Pattern PATTERN_FORMATADO = Pattern.compile("^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$");
    
    // Padrão para validar o formato do CNPJ (apenas dígitos)
    private static final Pattern PATTERN_NUMERICO = Pattern.compile("^\\d{14}$");
    
    @Override
    public void initialize(CnpjValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        // Verifica se o CNPJ é nulo ou vazio
        if (cnpj == null || cnpj.isEmpty()) {
            return false;
        }
        
        // Verifica se o CNPJ está no formato correto (com ou sem pontuação)
        return PATTERN_FORMATADO.matcher(cnpj).matches() || 
               PATTERN_NUMERICO.matcher(cnpj).matches();
    }
}