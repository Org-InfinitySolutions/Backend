package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PessoaJuridicaRespostaCadastroDTO extends UsuarioRespostaCadastroDTO{
    @Schema(example = "12.345.678/0001-90")
    private String cnpj;

    @JsonProperty("razao_social")
    private String razaoSocial;
}
