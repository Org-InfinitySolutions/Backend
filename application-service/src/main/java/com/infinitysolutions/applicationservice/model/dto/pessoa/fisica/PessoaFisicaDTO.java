package com.infinitysolutions.applicationservice.model.dto.pessoa.fisica;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
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


    @Schema(description = "Indica se o usuário possui comprovante de endereço")
    @JsonProperty("possui_comprovante_endereco")
    private boolean possuiComprovanteEndereco;


    @Schema(description = "Indica se o usuário está com o cadastro completo")
    @JsonProperty("cadastro_completo")
    private boolean cadastroCompleto;
    
    @Builder
    public PessoaFisicaDTO(
            UUID id,
            String nome,
            String email,
            String telefone,
            EnderecoDTO endereco,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            String cpf,
            String rg,
            boolean possuiCopiaRg,
            boolean possuiComprovanteEndereco,
            boolean cadastroCompleto,
            List<DocumentoUsuarioDTO> documentosUsuario) {
        super(id, nome, email, telefone, "PF", endereco, dataCriacao, dataAtualizacao, documentosUsuario);
        this.cpf = cpf;
        this.rg = rg;
        this.possuiCopiaRg = possuiCopiaRg;
        this.possuiComprovanteEndereco = possuiComprovanteEndereco;
        this.cadastroCompleto = cadastroCompleto;
    }
}
