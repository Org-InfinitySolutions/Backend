package com.infinitysolutions.applicationservice.model.dto.pessoa.fisica;

import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAtualizacaoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações para cadastro de pessoa física", allOf = {UsuarioAtualizacaoDTO.class})
public class PessoaFisicaAtualizacaoDTO extends UsuarioAtualizacaoDTO {
}
