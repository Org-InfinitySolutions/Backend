package com.infinitysolutions.applicationservice.model.dto.pessoa.fisica;

import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PessoaFisicaRespostaCadastroDTO extends UsuarioRespostaCadastroDTO {
    @Schema(example = "***.***.789-00")
    private String cpf;

    public PessoaFisicaRespostaCadastroDTO(UUID id, String nome, String telefone, EnderecoResumidoDTO enderecoResumidoDTO, String cpf) {
        super(id, nome, telefone, enderecoResumidoDTO);
        this.cpf = cpf;
    }
}
