package com.infinitysolutions.authservice.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidadorEmail.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailValido {
    String message() default "Formato de email inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
