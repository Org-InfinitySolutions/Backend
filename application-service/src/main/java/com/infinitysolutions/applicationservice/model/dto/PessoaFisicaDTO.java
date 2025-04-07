package com.infinitysolutions.applicationservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações detalhadas de pessoa física")
public class PessoaFisicaDTO extends UsuarioRespostaDTO {
    
    @Schema(description = "CPF do usuário (parcialmente mascarado para segurança)", 
            example = "***.***.789-00")
    private String cpf;
    
    @Schema(description = "RG do usuário", 
            example = "12.345.678-9")
    private String rg;
    
    @Schema(description = "Indica se o usuário possui cópia do RG verificada")
    @JsonProperty("possui_copia_rg")
    private boolean possuiCopiaRg;
    
    @Schema(description = "Indica se o usuário está com o cadastro completo")
    @JsonProperty("cadastro_completo")
    private boolean cadastroCompleto;
    
    @Builder
    public PessoaFisicaDTO(
            UUID id, 
            String nome, 
            String telefone, 
            EnderecoDTO endereco,
            String cpf,
            String rg,
            boolean possuiCopiaRg,
            boolean cadastroCompleto) {
        super(id, nome, telefone, "PF", endereco);
        this.cpf = cpf;
        this.rg = rg;
        this.possuiCopiaRg = possuiCopiaRg;
        this.cadastroCompleto = cadastroCompleto;
    }
}
