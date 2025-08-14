package com.infinitysolutions.applicationservice.model.dto.produto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Schema(description = "DTO para edição de um produto")

public class ProdutoAtualizacaoDTO {

    @NotBlank(message = "O modelo é obrigatório")
    @Schema(description = "Modelo do produto", example = "XYZ-1000", requiredMode = Schema.RequiredMode.REQUIRED)
    private String modelo;

    @NotBlank(message = "A marca é obrigatória")
    @Schema(description = "Marca do produto", example = "TechBrand", requiredMode = Schema.RequiredMode.REQUIRED)
    private String marca;

    @Schema(description = "URL do fabricante do produto", example = "https://fabricante.com/xyz1000")
    @JsonProperty("url_fabricante")
    private String urlFrabricante;

    @NotBlank(message = "A descrição é obrigatória")
    @Schema(description = "Descrição detalhada do produto", example = "Produto de alta qualidade com garantia de 12 meses", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricao;

    @Schema(description = "Quantidade disponível em estoque", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    @Positive
    @JsonProperty("qtd_estoque")
    private Integer qtdEstoque;

    @NotNull(message = "A categoria é obrigatória")
    @Positive(message = "O ID da categoria deve ser positivo")
    @Schema(description = "ID da categoria do produto", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("categoria_id")
    private Integer categoriaId;

    @JsonProperty("is_ativo")
    private boolean isAtivo;

}
