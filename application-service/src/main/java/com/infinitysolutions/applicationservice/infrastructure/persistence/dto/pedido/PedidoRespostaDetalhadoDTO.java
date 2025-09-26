package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoPedidoRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
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
            description = "Data e hora de retirada do pedido",
            example = "2024-12-20T14:30:00",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string",
            format = "date-time"
    )
    private LocalDateTime dataRetirada;

    @Schema(
            description = "Tipo do pedido (INDOOR, OUTDOOR)",
            example = "INDOOR",
            requiredMode = Schema.RequiredMode.REQUIRED,
            type = "string"
    )
    private TipoPedido tipoPedido;

    @Schema(
            description = "Nova situação do pedido.",
            example = "APROVADO",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private SituacaoPedido situacao;

    @Schema(
            description = "descrição do pedido"
    )
    private String descricao;

    @Schema(
            description = "URLs de download dos documentos associados a esse pedido."
    )
    private List<DocumentoPedidoDTO> documentos;

    public record DocumentoPedidoDTO(
            String originalFilename,
            String downloadUrl,
            String mimeType
    ) {
    }
}
