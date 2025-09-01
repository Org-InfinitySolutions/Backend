package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.infra.validation.TelefoneValido;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAtualizacaoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações para atualização da pessoa jurídica", allOf = {UsuarioAtualizacaoDTO.class})
public class PJAtualizacaoDTO extends UsuarioAtualizacaoDTO {
    @NotBlank(message = "A razão social é obrigatória (padrão do nome do campo: razao_social)")
    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo LTDA")
    @JsonProperty("razao_social")
    private String razaoSocial;

    @NotBlank(message = "O telefone residencial é obrigatório")
    @TelefoneValido(message = "Telefone residencial inválido. Formatos aceitos: (99) 2222-2222 ou (99) 99999-9999")
    @Schema(
            description = "Telefone residencial de contato com DDD",
            example = "(11) 2222-2222",
            pattern = "(99) 9999-9999 ou (99) 99999-9999",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("telefone_residencial")
    private String telefoneResidencial;
}
