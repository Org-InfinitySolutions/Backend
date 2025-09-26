package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para resposta de KPIs do dashboard")
public record KpiResponseDTO(
        @Schema(description = "Nome/tipo do KPI", example = "OUTDOOR")
        String nome,
        
        @Schema(description = "Valor do KPI", example = "45")
        Long valor,
        
        @Schema(description = "Percentual do total", example = "65.2")
        Double percentual
) {
}
