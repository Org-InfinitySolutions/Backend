package com.infinitysolutions.applicationservice.model.dto.pessoa.juridica;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAtualizacaoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações para atualização da pessoa jurídica", allOf = {UsuarioAtualizacaoDTO.class})
public class PessoaJuridicaAtualizacaoDTO extends UsuarioAtualizacaoDTO {
    @NotBlank(message = "A razão social é obrigatória (padrão do nome do campo: razao_social)")
    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo LTDA")
    @JsonProperty("razao_social")
    private String razaoSocial;

    @NotBlank(message = "O telefone residencial é obrigatório (padrão do nome do campo: telefone_residencial)")
    @Pattern(
            regexp = "^\\(?[1-9]{2}\\)? ?(?:[2-8]|9[1-9])[0-9]{3}\\-?[0-9]{4}$",
            message = "Telefone residencial inválido. Formatos aceitos: (99) 99999-9999 ou 99999999999"
    )
    @Schema(
            description = "Telefone residencial de contato com DDD",
            example = "(11) 98765-4321",
            pattern = "(99) 99999-9999 ou 99999999999",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @JsonProperty("telefone_residencial")
    private String telefoneResidencial;
}
