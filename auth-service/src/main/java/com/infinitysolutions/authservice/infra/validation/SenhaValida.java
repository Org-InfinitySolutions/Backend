package com.infinitysolutions.authservice.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidadorSenha.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SenhaValida {
    String message() default "Senha inválida: deve conter pelo menos 8 caracteres, incluindo letras e números.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    int minLength() default 8;
    boolean requireUppercase() default false;
    boolean requireSpecialChar() default false;
}
