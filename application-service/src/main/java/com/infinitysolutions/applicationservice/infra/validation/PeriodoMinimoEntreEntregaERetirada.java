package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PeriodoMinimoEntreEntregaERetiradaValidator.class)
public @interface PeriodoMinimoEntreEntregaERetirada {
    String message() default "A data de retirada deve ser pelo menos 3 horas após a data de entrega";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
