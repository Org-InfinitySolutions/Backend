package com.infinitysolutions.applicationservice.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SituacaoPedido {
    EM_ANALISE("Em Análise"),
    APROVADO("Aprovado"),
    EM_EVENTO("Em Evento"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private final String nome;

}
