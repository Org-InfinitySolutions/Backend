package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidadorCpf implements ConstraintValidator<CpfValido, String> {

    // Padrão para validar o formato do CPF (com separadores)
    private static final Pattern PATTERN_FORMATADO = Pattern.compile("^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$");
    
    // Padrão para validar o formato do CPF (apenas dígitos)
    private static final Pattern PATTERN_NUMERICO = Pattern.compile("^\\d{11}$");
    
    @Override
    public void initialize(CpfValido constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        // Verifica se o CPF é nulo ou vazio
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }
        
        // Verifica se o CPF está no formato correto (com ou sem pontuação)
        return PATTERN_FORMATADO.matcher(cpf).matches() || 
               PATTERN_NUMERICO.matcher(cpf).matches();
    }
}