package com.infinitysolutions.applicationservice.model.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "DTO consolidado com todos os dados do dashboard")
public record DashboardResponseDTO(
        @Schema(description = "KPI do tipo de evento mais procurado")
        KpiResponseDTO tipoEventoMaisPopular,
        
        @Schema(description = "KPI da categoria mais procurada")
        KpiResponseDTO categoriaMaisPopular,
        
        @Schema(description = "Tempo médio de atendimento")
        TempoMedioAtendimentoDTO tempoMedioAtendimento,
        
        @Schema(description = "Dados para gráfico de linha - pedidos por mês (últimos 6 meses)")
        List<PedidosPorMesDTO> pedidosPorMes,
        
        @Schema(description = "Dados para gráfico pizza - equipamentos mais procurados")
        List<EquipamentoPopularDTO> equipamentosPopulares
) {
}
