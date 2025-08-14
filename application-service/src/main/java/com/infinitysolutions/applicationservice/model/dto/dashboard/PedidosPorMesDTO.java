package com.infinitysolutions.applicationservice.model.dto.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.YearMonth;

@Schema(description = "DTO para dados do gráfico de linha - pedidos por mês")
public record PedidosPorMesDTO(
        @Schema(description = "Mês e ano", example = "2025-01")
        YearMonth mes,
        
        @Schema(description = "Quantidade de pedidos no mês", example = "35")
        Long quantidadePedidos
) {
}
