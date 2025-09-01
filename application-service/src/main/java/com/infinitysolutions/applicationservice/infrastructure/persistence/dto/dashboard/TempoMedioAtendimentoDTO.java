package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para dados de tempo médio de atendimento")
public record TempoMedioAtendimentoDTO(
        @Schema(description = "Tempo médio em dias", example = "2.7")
        Double tempoMedioDias,
        
        @Schema(description = "Quantidade de pedidos analisados", example = "150")
        Long totalPedidos
) {
}
