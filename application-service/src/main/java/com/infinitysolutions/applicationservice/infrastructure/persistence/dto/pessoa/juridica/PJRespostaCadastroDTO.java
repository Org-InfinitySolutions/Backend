package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PJRespostaCadastroDTO extends UsuarioRespostaCadastroDTO {
    @Schema(example = "12.345.678/0001-90")
    private String cnpj;

    @JsonProperty("razao_social")
    private String razaoSocial;

    @JsonProperty("telefone_residencial")
    private String telefoneResidencial;

    public PJRespostaCadastroDTO(UUID id, String nome, String telefoneCelular, String email, EnderecoResumidoDTO enderecoResumidoDTO, String cnpj, String razaoSocial, String telefoneResidencial) {
        super(id, nome, telefoneCelular, email, enderecoResumidoDTO);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.telefoneResidencial = telefoneResidencial;

    }
}
