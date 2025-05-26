package com.infinitysolutions.applicationservice.model.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoPedidoRespostaDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "PedidoRespostaDetalhado",
    description = "Dados detalhados de um pedido para listagem. " +
                 "Pode ser estendido para incluir informações específicas dependendo do perfil do usuário."
)
@Data
@NoArgsConstructor
public class PedidoRespostaDetalhadoDTO {
    
    @Schema(
        description = "Identificador único do pedido",
        example = "123",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer id;

    private UsuarioRespostaDTO usuario;

    private List<ProdutoPedidoRespostaDTO> produtos;

    private EnderecoResumidoDTO endereco;

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
    private LocalDateTime data;

    @Schema(
            description = "Nova situação do pedido.",
            example = "APROVADO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private SituacaoPedido situacao;
}
