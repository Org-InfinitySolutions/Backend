package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.old.infra.validation.CnpjValido;
import com.infinitysolutions.applicationservice.old.infra.validation.TelefoneValido;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioCadastroDTO;
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
