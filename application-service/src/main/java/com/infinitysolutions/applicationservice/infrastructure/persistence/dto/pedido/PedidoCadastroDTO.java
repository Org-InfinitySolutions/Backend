package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido;


import com.infinitysolutions.applicationservice.old.infra.validation.PeriodoMinimoEntreEntregaERetirada;
import com.infinitysolutions.applicationservice.old.infra.validation.ProdutosSemDuplicata;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "PedidoCadastro",
    description = "Dados necessários para criação de um novo pedido no sistema"
)
@PeriodoMinimoEntreEntregaERetirada
public record PedidoCadastroDTO(
          @Schema(
            description = "Lista de produtos com suas respectivas quantidades",
            required = true
        )
        @ProdutosSemDuplicata
        @NotEmpty(message = "Lista de produtos não pode estar vazia")
        @Valid
        List<ProdutoPedidoDTO> produtos,
        
        @Schema(
            description = "Endereço completo para entrega dos produtos",
            required = true
        )
        @NotNull(message = "Endereço de entrega é obrigatório")
        @Valid
        EnderecoDTO endereco,
        
        @Schema(
            description = "Tipo do pedido (INDOOR ou OUTDOOR)",
            example = "INDOOR",
            required = true
        )
        @NotNull(message = "Tipo do pedido é obrigatório")
        TipoPedido tipo,
          @Schema(
            description = "Data e hora programada para entrega dos equipamentos",
            example = "2024-12-25T10:00:00",
            type = "string",
            format = "date-time"
        )
        @Future
        LocalDateTime dataEntrega,
        
        @Schema(
            description = "Data e hora programada para retirada dos equipamentos (deve ser pelo menos 3 horas após a data de entrega)",
            example = "2024-12-25T14:00:00",
            type = "string",
            format = "date-time"
        )
        @Future
        LocalDateTime dataRetirada,
        
        @Schema(
            description = "Descrição detalhada do pedido e sua finalidade",
            example = "Pedido para evento de fim de ano da empresa",
            required = true,
            maxLength = 500
        )
        String descricao
) {
    
    @Schema(
        name = "ProdutoPedido",
        description = "Informações de um produto específico dentro do pedido"
    )    public record ProdutoPedidoDTO(
            
            @Schema(
                description = "Identificador único do produto no sistema",
                example = "123",
                required = true,
                minimum = "1"
            )
            @NotNull(message = "ID do produto é obrigatório")
            @Positive(message = "ID do produto deve ser positivo")
            Integer produtoId,
            
            @Schema(
                description = "Quantidade do produto solicitada no pedido",
                example = "2",
                required = true,
                minimum = "1"
            )
            @NotNull(message = "Quantidade é obrigatória")
            @Positive(message = "Quantidade deve ser positiva")
            Integer quantidade
    ) {}
}
