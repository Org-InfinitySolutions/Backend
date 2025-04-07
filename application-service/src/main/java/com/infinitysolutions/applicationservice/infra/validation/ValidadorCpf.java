package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validador para a anotação @CpfValido.
 * Verifica se a String representa um CPF estruturalmente válido (11 dígitos),
 * ignorando a máscara (pontos e traço).
 *
 * NÃO realiza a validação matemática (cálculo dos dígitos verificadores).
 * Considera nulo como válido (permitindo composição com @NotNull/@NotBlank).
 * Rejeita CPFs com todos os dígitos repetidos (ex: 111.111.111-11).
 */
public class ValidadorCpf implements ConstraintValidator<CpfValido, String> {

    private static final Pattern PATTERN_APENAS_DIGITOS_11 = Pattern.compile("^\\d{11}$");
    private static final Pattern PATTERN_DIGITOS_REPETIDOS = Pattern.compile("(\\d)\\1{10}");

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {

        if (cpf == null) {
            return true;
        }
        String cpfLimpo = cpf.replaceAll("[.\\-\\s]", "");
        if (!PATTERN_APENAS_DIGITOS_11.matcher(cpfLimpo).matches()) {
            return false;
        }

        if (PATTERN_DIGITOS_REPETIDOS.matcher(cpfLimpo).matches()) {
            return false;
        }
        return true;
    }

    /**
     * (Opcional) Helper para adicionar mensagens de erro customizadas ao contexto.
     * Isso permite ter mensagens diferentes para falhas diferentes.
     */
    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation(); // Desabilita a mensagem padrão
        context.buildConstraintViolationWithTemplate(message) // Define a nova mensagem
                .addConstraintViolation(); // Adiciona a violação
    }
}