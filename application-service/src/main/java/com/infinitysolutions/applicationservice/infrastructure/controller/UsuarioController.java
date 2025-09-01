package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.core.domain.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.usecases.usuario.*;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PessoaJuridicaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
@Validated
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final CriarUsuario criarUsuarioCase;
    private final ListarTodosUsuarios listarTodosUsuariosCase;
    private final BuscarUsuarioPorId buscarUsuarioPorIdCase;
    private final ExcluirUsuario excluirUsuarioCase;
    private final AtualizarUsuario atualizarUsuarioCase;

    private final UsuarioEntityMapper usuarioEntityMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cadastra um novo usuário no sistema, podendo ser pessoa física (PF) ou pessoa jurídica (PJ)"
    )
    public UsuarioRespostaCadastroDTO cadastrar(@Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        EnderecoDTO endereco = usuarioCadastroDTO.getEndereco();
        EnderecoInput enderecoInput = new EnderecoInput(
                endereco.getCep(),
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getNumero(),
                endereco.getComplemento()
        );

        CriarUsuarioInput input = switch (usuarioCadastroDTO.getTipo()) {
            case "PF" -> new CriarPFInput(
                    usuarioCadastroDTO.getNome(),
                    usuarioCadastroDTO.getTelefoneCelular(),
                    usuarioCadastroDTO.getTipo(),
                    usuarioCadastroDTO.getEmail(),
                    usuarioCadastroDTO.getSenha(),
                    enderecoInput,
                    ((PessoaFisicaCadastroDTO) usuarioCadastroDTO).getCpf(),
                    ((PessoaFisicaCadastroDTO) usuarioCadastroDTO).getRg()
                    );
            case "PJ" -> new CriarPJInput(
                    usuarioCadastroDTO.getNome(),
                    usuarioCadastroDTO.getTelefoneCelular(),
                    usuarioCadastroDTO.getTipo(),
                    usuarioCadastroDTO.getEmail(),
                    usuarioCadastroDTO.getSenha(),
                    enderecoInput,
                    ((PessoaJuridicaCadastroDTO) usuarioCadastroDTO).getCnpj(),
                    ((PessoaJuridicaCadastroDTO) usuarioCadastroDTO).getRazaoSocial(),
                    ((PessoaJuridicaCadastroDTO) usuarioCadastroDTO).getTelefoneResidencial()
            );
            default -> throw RecursoNaoEncontradoException.estrategiaNaoEncontrada(usuarioCadastroDTO.getTipo());
        };

        return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(criarUsuarioCase.execute(input));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados no sistema"
    )
    public List<UsuarioRespostaCadastroDTO> listarTodos() {
        List<Usuario> usuariosEncontrados = listarTodosUsuariosCase.execute();
        return usuariosEncontrados.stream().map(usuarioEntityMapper::toUsuarioRespostaCadastroDTO).toList();
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaDTO buscarPorId(@PathVariable UUID usuarioId) {
        return usuarioEntityMapper.toUsuarioRespostaDTO(buscarUsuarioPorIdCase.execute(usuarioId));
    }
//
//    @DeleteMapping("/{usuarioId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @Operation(
//        summary = "Excluir usuário por ID",
//        description = "Remove um usuário específico do sistema com base no ID fornecido"
//    )
//    public void excluir(@PathVariable UUID usuarioId) {
//        excluirUsuarioCase.execute(usuarioId);
//    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Atualizar usuário por ID",
        description = "Atualiza os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaCadastroDTO atualizar(@PathVariable UUID usuarioId, @Valid @RequestBody UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        if (usuarioAtualizacaoDTO.getTipo().equals("PF")) {
            AtualizarPessoaFisicaInput result = (AtualizarPessoaFisicaInput) usuarioEntityMapper.toAtualizarUsuarioInput(usuarioAtualizacaoDTO);
            return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(atualizarUsuarioCase.execute(usuarioId, result));
        } else {
            AtualizarPessoaJuridicaInput result = (AtualizarPessoaJuridicaInput) usuarioEntityMapper.toAtualizarUsuarioInput(usuarioAtualizacaoDTO);
            return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(atualizarUsuarioCase.execute(usuarioId, result));
        }
    }

//    @GetMapping("/cpf")
//    @Operation(
//            summary = "Verificar um CPF",
//            description = "Verificar se um determinado CPF está disponível"
//    )
//    public ResponseEntity<?> verificarCpf(
//            @RequestParam(value = "cpf_like") @Valid @NotBlank(message = "Cpf obrigatório") String cpf
//    ) {
//        RespostaVerificacao respostaVerificacao = service.verificarCpf(cpf);
//        if (respostaVerificacao.disponivel()){
//            return ResponseEntity.ok(respostaVerificacao);
//        }else {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(respostaVerificacao);
//        }
//    }

//    @GetMapping("/rg")
//    @Operation(
//            summary = "Verificar um RG",
//            description = "Verificar se um determinado RG está disponível"
//    )
//    public ResponseEntity<?> verificarRg(
//            @RequestParam(value = "rg_like") @Valid @NotBlank(message = "rg obrigatório") String rg
//    ) {
//        RespostaVerificacao respostaVerificacao = service.verificarRg(rg);
//        if (respostaVerificacao.disponivel()){
//            return ResponseEntity.ok(respostaVerificacao);
//        }else {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(respostaVerificacao);
//        }
//    }


//    @GetMapping("/cnpj")
//    @Operation(
//            summary = "Verificar um CNPJ",
//            description = "Verificar se um determinado CPF está disponível"
//    )
//    public ResponseEntity<?> verificarCnpj(
//            @RequestParam(value = "cnpj_like") @Valid @NotBlank(message = "Cnpj obrigatório") String cnpj
//    ) {
//        RespostaVerificacao respostaVerificacao = service.verificarCnpj(cnpj);
//        if (respostaVerificacao.disponivel()){
//            return ResponseEntity.ok(respostaVerificacao);
//        }else {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(respostaVerificacao);
//        }
//    }
}
