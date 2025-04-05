package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações detalhadas de pessoa jurídica")
public class PessoaJuridicaDTO extends UsuarioRespostaDTO {
    
    @Schema(description = "CNPJ da empresa", 
            example = "12.345.678/0001-90")
    private String cnpj;

    @Schema(description = "Razão social da empresa", 
            example = "Empresa Exemplo LTDA")
    @JsonProperty("razao_social")
    private String razaoSocial;
    
    @Schema(description = "Indica se a empresa possui contrato social cadastrado")
    @JsonProperty("possui_contrato_social")
    private boolean possuiContratoSocial;
    
    @Schema(description = "Indica se a empresa possui cartão CNPJ cadastrado")
    @JsonProperty("possui_cartao_cnpj")
    private boolean possuiCartaoCnpj;
    
    @Schema(description = "Indica se o cadastro da empresa está completo")
    @JsonProperty("cadastro_completo")
    private boolean cadastroCompleto;

    @Builder
    public PessoaJuridicaDTO(
            UUID id,
            String nome,
            String telefone,
            String email,
            EnderecoDTO endereco,
            String cnpj,
            String razaoSocial,
            boolean possuiContratoSocial,
            boolean possuiCartaoCnpj,
            boolean cadastroCompleto) {
        super(id, nome, telefone, email, endereco);
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.possuiContratoSocial = possuiContratoSocial;
        this.possuiCartaoCnpj = possuiCartaoCnpj;
        this.cadastroCompleto = cadastroCompleto;
    }
}
