package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(
    name = "PedidoRespostaAdmin",
    description = "Dados estendidos de um pedido para visualização de administradores. " +
                 "Inclui informações adicionais como nome do usuário que criou o pedido."
)
@Data
@EqualsAndHashCode(callSuper = true)
public class PedidoRespostaAdminDTO extends PedidoRespostaDTO {
    
    @Schema(
        description = "Nome completo do usuário que criou o pedido",
        example = "João Silva",
        required = true
    )
    private String nome;
}
