package com.infinitysolutions.applicationservice.model.dto.pedido;

import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import com.infinitysolutions.applicationservice.model.enums.TipoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(
    name = "PedidoRespostaCadastro",
    description = "Dados retornados após a criação bem-sucedida de um pedido"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRespostaCadastroDTO {
    
    @Schema(
        description = "Identificador único do pedido criado",
        example = "123",
        required = true
    )
    private Integer id;
    
    @Schema(
        description = "Identificador único do usuário que criou o pedido",
        example = "550e8400-e29b-41d4-a716-446655440000",
        required = true
    )
    private UUID usuarioId;
    
    @Schema(
        description = "Nome completo do usuário que criou o pedido",
        example = "João Silva",
        required = true
    )
    private String nomeUsuario;
    
    @Schema(
        description = "Quantidade total de itens no pedido",
        example = "5",
        required = true,
        minimum = "1"
    )
    private Integer qtdItens;
    
    @Schema(
        description = "Situação atual do pedido no sistema",
        example = "AGUARDANDO_APROVACAO",
        required = true
    )
    private SituacaoPedido situacao;
    
    @Schema(
        description = "Tipo do pedido criado",
        example = "ALUGUEL",
        required = true
    )
    private TipoPedido tipo;
    
    @Schema(
        description = "Data e hora de criação do pedido",
        example = "2024-12-20T14:30:00",
        required = true,
        type = "string",
        format = "date-time"
    )
    private LocalDateTime dataCriacao;
    
    @Schema(
        description = "Descrição fornecida pelo usuário para o pedido",
        example = "Pedido para evento de fim de ano da empresa",
        required = true
    )
    private String descricao;
}
