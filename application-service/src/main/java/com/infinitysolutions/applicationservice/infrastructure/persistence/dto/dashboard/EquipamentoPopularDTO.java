package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para dados do gráfico pizza - equipamentos mais procurados")
public record EquipamentoPopularDTO(
        @Schema(description = "Nome do equipamento/produto", example = "Notebook")
        String nomeEquipamento,
        
        @Schema(description = "Quantidade de vezes solicitado", example = "120")
        Long quantidadeSolicitacoes,
        
        @Schema(description = "Percentual do total", example = "43.5")
        Double percentual
) {
}
