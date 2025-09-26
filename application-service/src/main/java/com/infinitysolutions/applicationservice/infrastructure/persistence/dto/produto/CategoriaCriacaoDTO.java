package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para criação de uma categoria")
public record CategoriaCriacaoDTO (
        @NotBlank(message = "O nome é obrigatório")
        @Schema(description = "Nome da categoria", example = "Eletrônicos", requiredMode = Schema.RequiredMode.REQUIRED)
        String nome
){
}
