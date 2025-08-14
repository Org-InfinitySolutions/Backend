package com.infinitysolutions.applicationservice.infra.validation;

import com.infinitysolutions.applicationservice.model.dto.pedido.PedidoCadastroDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Duration;

public class PeriodoMinimoEntreEntregaERetiradaValidator implements ConstraintValidator<PeriodoMinimoEntreEntregaERetirada, PedidoCadastroDTO> {

    private static final int HORAS_MINIMAS = 3;
    private String mensagem;
    
    @Override
    public void initialize(PeriodoMinimoEntreEntregaERetirada constraintAnnotation) {
        this.mensagem = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(PedidoCadastroDTO pedido, ConstraintValidatorContext context) {
        if (pedido.dataEntrega() == null || pedido.dataRetirada() == null) {
            return true;
        }

        Duration duracao = Duration.between(pedido.dataEntrega(), pedido.dataRetirada());
        long horasDiferenca = duracao.toHours();

        boolean isValid = !duracao.isNegative() && horasDiferenca >= HORAS_MINIMAS;
        
        if (!isValid) {
            // Desabilita a mensagem padrão
            context.disableDefaultConstraintViolation();
            
            // Adiciona uma mensagem de erro personalizada para o campo dataRetirada
            context
                .buildConstraintViolationWithTemplate(mensagem)
                .addPropertyNode("dataRetirada")
                .addConstraintViolation();
        }
    
        return isValid;
    }
}
