package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PessoaJuridicaRespostaCadastroDTO extends UsuarioRespostaCadastroDTO{
    @Schema(example = "12.345.678/0001-90")
    private String cnpj;

    @JsonProperty("razao_social")
    private String razaoSocial;

    @JsonProperty("telefone_residencial")
    private String telefoneResidencial;

    public PessoaJuridicaRespostaCadastroDTO(UUID id, String nome, String telefoneCelular, EnderecoResumidoDTO enderecoResumidoDTO, String cnpj, String razaoSocial, String telefoneResidencial) {
        super(id, nome, telefoneCelular, enderecoResumidoDTO);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.telefoneResidencial = telefoneResidencial;

    }
}
