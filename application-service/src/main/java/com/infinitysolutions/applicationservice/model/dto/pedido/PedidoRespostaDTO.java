package com.infinitysolutions.applicationservice.model.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "PedidoResposta",
    description = "Dados básicos de um pedido para listagem. " +
                 "Pode ser estendido para incluir informações específicas dependendo do perfil do usuário.",
    subTypes = {PedidoRespostaAdminDTO.class}
)
@Data
@NoArgsConstructor
@JsonSubTypes({
        @JsonSubTypes.Type(value = PedidoRespostaAdminDTO.class, name = "ADM")
})
public class PedidoRespostaDTO {
    
    @Schema(
        description = "Identificador único do pedido",
        example = "123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;
    
    @Schema(
        description = "Quantidade total de itens no pedido",
        example = "5",
        requiredMode = Schema.RequiredMode.REQUIRED,
        minimum = "1"
    )
    @JsonProperty("qtd_itens")
    private Integer qtdItens;
    
    @Schema(
        description = "Data e hora de criação do pedido",
        example = "2024-12-20T14:30:00",
        requiredMode = Schema.RequiredMode.REQUIRED,
        type = "string",
        format = "date-time"
    )
    private LocalDateTime dataCriacao;


    @Schema(
            description = "Data e hora de entrega do pedido",
            example = "2024-12-20T14:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "date-time"
    )
    private LocalDateTime dataEntrega;


    @Schema(
            description = "Nova situação do pedido.",
            example = "APROVADO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private SituacaoPedido situacao;



}
