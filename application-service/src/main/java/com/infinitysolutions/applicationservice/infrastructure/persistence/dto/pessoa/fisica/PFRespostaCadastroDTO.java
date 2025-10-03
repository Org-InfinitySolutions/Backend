package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PFRespostaCadastroDTO extends UsuarioRespostaCadastroDTO {
    @Schema(example = "***.***.789-00")
    private String cpf;

    public PFRespostaCadastroDTO(UUID id, String nome, String telefone, String email, EnderecoResumidoDTO enderecoResumidoDTO, String cpf) {
        super(id, nome, telefone, email, enderecoResumidoDTO);
        this.cpf = cpf;
    }
}
