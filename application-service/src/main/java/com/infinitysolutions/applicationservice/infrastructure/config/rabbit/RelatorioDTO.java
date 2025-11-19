package com.infinitysolutions.applicationservice.infrastructure.config.rabbit;

import java.time.LocalDateTime;

public record RelatorioDTO(
        String mesAno,
        Integer totalPedidos,
        Integer pedidosEmAnalise,
        Integer pedidosAprovados,
        Integer pedidosEmEvento,
        Integer pedidosFinalizados,
        Integer pedidosCancelados,
        Integer pedidosIndoor,
        Integer pedidosOutdoor,
        Integer totalItensLocados,
        LocalDateTime dataGeracao
) {
}
