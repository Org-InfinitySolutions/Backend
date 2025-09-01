package com.infinitysolutions.applicationservice.old.infra.validation;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Cpf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validador para a anotação @CpfValido.
 * Delega a validação para o value object Cpf, que realiza validações completas:
 * - Verificação estrutural (11 dígitos)
 * - Rejeição de dígitos repetidos
 * - Cálculo matemático dos dígitos verificadores
 *
 * Considera nulo como válido (permitindo composição com @NotNull/@NotBlank).
 */
public class ValidadorCpf implements ConstraintValidator<CpfValido, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return true;
        }
        
        return Cpf.isValido(cpf);
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