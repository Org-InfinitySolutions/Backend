package com.infinitysolutions.applicationservice.old.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidadorTelefone.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TelefoneValido {
    String message() default "Telefone inválido. Formatos aceitos: (99) 99999-9999, 99 99999-9999 ou 9999999999";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    boolean celular() default true;
    boolean fixo() default true;
}