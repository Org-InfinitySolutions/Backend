package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ProdutosSemDuplicataValidator.class)
public @interface ProdutosSemDuplicata {
    String message() default "Não é permitido incluir o mesmo produto mais de uma vez no pedido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default{};
}
