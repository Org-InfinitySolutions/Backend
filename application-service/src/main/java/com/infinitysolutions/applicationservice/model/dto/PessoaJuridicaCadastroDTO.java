package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.infra.validation.CnpjValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações para cadastro de pessoa jurídica", allOf = {UsuarioCadastroDTO.class})
public class PessoaJuridicaCadastroDTO extends UsuarioCadastroDTO {
    @NotBlank(message = "O CNPJ é obrigatório")
    @CnpjValido
    @Schema(description = "CNPJ válido, sem pontuação ou com pontuação", example = "12.345.678/0001-90")
    private String cnpj;

    @NotBlank(message = "A razão social é obrigatória (padrão do nome do campo: razao_social)")
    @Schema(description = "Razão social da empresa", example = "Empresa Exemplo LTDA")
    @JsonProperty("razao_social")
    private String razaoSocial;
}
