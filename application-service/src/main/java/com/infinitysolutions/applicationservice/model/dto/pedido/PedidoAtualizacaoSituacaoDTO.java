package com.infinitysolutions.applicationservice.model.dto.pedido;

import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "PedidoAtualizacaoSituacao",
        description = "DTO para a atualização da situação de um pedido"
)
public record PedidoAtualizacaoSituacaoDTO(
    @Schema(
            description = "Nova situação do pedido.",
            example = "APROVADO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotNull(message = "Situação é obrigatória")
    SituacaoPedido situacao
) {
}
