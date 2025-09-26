package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Informações de resposta para categoria")
public record CategoriaRespostaDTO (
        @Schema(description = "Identificador único da categoria", example = "1")
        Integer id,
        @Schema(description = "Nome da categoria", example = "Eletrônicos")
        String nome
){
}
