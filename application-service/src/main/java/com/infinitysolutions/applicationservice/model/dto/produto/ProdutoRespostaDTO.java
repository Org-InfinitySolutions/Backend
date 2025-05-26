package com.infinitysolutions.applicationservice.model.dto.produto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Informações genéricas de resposta para qualquer tipo de produto")
public class ProdutoRespostaDTO {
    
    @Schema(description = "Identificador único do produto", example = "1")
    private Integer id;

    @Schema(description = "Modelo do produto", example = "XYZ-1000")
    private String modelo;

    @Schema(description = "Marca do produto", example = "TechBrand")
    private String marca;

    @Schema(description = "URL para o site do fabricante do produto", example = "https://www.techbrand.com")
    @JsonProperty("url_fabricante")
    private String urlFabricante;

    @Schema(description = "Imagem do produto em formato de bytes", example = "[bytes]")
    private byte[] imagem;

    @Schema(description = "Descrição detalhada do produto", example = "Monitor LED 24 polegadas com resolução Full HD e taxa de atualização de 144Hz")
    private String descricao;

    @Schema(description = "Quantidade disponível do produto em estoque", example = "50")
    @JsonProperty("qtd_estoque")
    private Integer qtdEstoque;

    @Schema(description = "Categoria do produto")
    private CategoriaRespostaDTO categoria;


    @Schema(description = "Estado do produto", example = "true")
    @JsonProperty("is_ativo")
    private boolean isAtivo;
}
