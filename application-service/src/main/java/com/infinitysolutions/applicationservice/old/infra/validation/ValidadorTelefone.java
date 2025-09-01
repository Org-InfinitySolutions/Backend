package com.infinitysolutions.applicationservice.old.infra.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidadorTelefone implements ConstraintValidator<TelefoneValido, String> {

    private static final Pattern PATTERN_DDD = Pattern.compile("^(1[1-9]|[2-9][0-9])$");

    private static final Pattern PATTERN_CELULAR = Pattern.compile("^9[0-9]{8}$");

    private static final Pattern PATTERN_FIXO = Pattern.compile("^[2-5][0-9]{7}$");

    private static final Pattern PATTERN_FORMATADO = Pattern.compile(
            "^\\(?([1-9][0-9])\\)? ?([2-9][0-9]{3,4})\\-?([0-9]{4})$");

    private boolean validarCelular;
    private boolean validarFixo;

    @Override
    public void initialize(TelefoneValido constraintAnnotation) {
        this.validarCelular = constraintAnnotation.celular();
        this.validarFixo = constraintAnnotation.fixo();
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.isBlank()) {
            return true;
        }

        String numeroLimpo = telefone.replaceAll("[^0-9]", "");

        if (numeroLimpo.length() != 10 && numeroLimpo.length() != 11) {
            return false;
        }

        String ddd = numeroLimpo.substring(0, 2);
        String numero = numeroLimpo.substring(2);

        if (!PATTERN_DDD.matcher(ddd).matches()) {
            return false;
        }
        if (numeroLimpo.length() == 11) {
            return validarCelular && PATTERN_CELULAR.matcher(numero).matches();
        } else {
            return validarFixo && PATTERN_FIXO.matcher(numero).matches();
        }
    }
}