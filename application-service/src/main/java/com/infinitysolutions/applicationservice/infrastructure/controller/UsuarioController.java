package com.infinitysolutions.applicationservice.infrastructure.controller;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.usuario.*;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPFInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.VerificarCpf;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.VerificarRg;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPJInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.VerificarCnpj;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PessoaJuridicaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.core.usecases.usuario.RespostaVerificacao;
import com.infinitysolutions.applicationservice.infrastructure.utils.AuthenticationUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final BuscarCredencialPorId buscarCredencialPorIdCase;

    private final AuthenticationUtils authenticationUtils;

    private final AtualizarUsuario atualizarUsuarioCase;
    private final PromoverUsuario promoverUsuarioCase;
    private final RebaixarUsuario rebaixarUsuarioCase;
    private final VerificarCpf verificarCpfCase;
    private final VerificarRg verificarRgCase;
    private final VerificarCnpj verificarCnpjCase;
    private final UsuarioEntityMapper usuarioEntityMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
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

        Usuario usuarioSalvo = criarUsuarioCase.execute(input);
        Credencial identificacao = buscarCredencialPorIdCase.execute(usuarioSalvo.getId());
        return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(usuarioSalvo, identificacao);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados no sistema"
    )
    public List<UsuarioRespostaCadastroDTO> listarTodos() {
        List<Usuario> usuariosEncontrados = listarTodosUsuariosCase.execute();

        List<UsuarioRespostaCadastroDTO> usuarioResposta = new ArrayList<>();
        for(int i = 0; i < usuariosEncontrados.size(); i++) {
            Credencial identificacao = buscarCredencialPorIdCase.execute(usuariosEncontrados.get(i).getId());
            usuarioResposta.add(usuarioEntityMapper.toUsuarioRespostaCadastroDTO(usuariosEncontrados.get(i), identificacao));
        }

        return usuarioResposta;
    }

    @PutMapping("/promover/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Promove um usuário",
            description = "Realiza a promoção de um usuário PF para Funcionário"
    )
    public void  promoverUsuario(@PathVariable UUID usuarioId, Authentication auth) {
        if (!authenticationUtils.isAdmin(auth)) throw AutenticacaoException.acessoNegado();
        promoverUsuarioCase.execute(usuarioId);
    }

    @PutMapping("/rebaixar/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "rebaixa um usuário",
            description = "Rebaixa um usuário PF Funcionário para apenas PF"
    )
    public void  rebaixarUsuario(@PathVariable UUID usuarioId, Authentication auth) {
        if (!authenticationUtils.isAdmin(auth)) throw AutenticacaoException.acessoNegado();
        rebaixarUsuarioCase.execute(usuarioId);
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaDTO buscarPorId(@PathVariable UUID usuarioId) {
        Usuario usuarioEncontrado = buscarUsuarioPorIdCase.execute(usuarioId);
        Credencial credencialEncontrada = buscarCredencialPorIdCase.execute(usuarioId);
        return usuarioEntityMapper.toUsuarioRespostaDTO(usuarioEncontrado, credencialEncontrada);
    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Atualizar usuário por ID",
        description = "Atualiza os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaCadastroDTO atualizar(@PathVariable UUID usuarioId, @Valid @RequestBody UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        Credencial identificacao = buscarCredencialPorIdCase.execute(usuarioId);
        if (usuarioAtualizacaoDTO.getTipo().equals("PF")) {
            AtualizarPessoaFisicaInput result = (AtualizarPessoaFisicaInput) usuarioEntityMapper.toAtualizarUsuarioInput(usuarioAtualizacaoDTO);
            return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(atualizarUsuarioCase.execute(usuarioId, result), identificacao);
        } else {
            AtualizarPessoaJuridicaInput result = (AtualizarPessoaJuridicaInput) usuarioEntityMapper.toAtualizarUsuarioInput(usuarioAtualizacaoDTO);
            return usuarioEntityMapper.toUsuarioRespostaCadastroDTO(atualizarUsuarioCase.execute(usuarioId, result), identificacao);
        }
    }

    @GetMapping("/cpf")
    @Operation(
            summary = "Verificar um CPF",
            description = "Verificar se um determinado CPF está disponível"
    )
    public ResponseEntity<?> verificarCpf(
            @RequestParam(value = "cpf_like") @Valid @NotBlank(message = "Cpf obrigatório") String cpf
    ) {
        RespostaVerificacao respostaVerificacao = verificarCpfCase.execute(cpf);
        if (respostaVerificacao.disponivel()){
            return ResponseEntity.ok(respostaVerificacao);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(respostaVerificacao);
        }
    }

    @GetMapping("/rg")
    @Operation(
            summary = "Verificar um RG",
            description = "Verificar se um determinado RG está disponível"
    )
    public ResponseEntity<?> verificarRg(
            @RequestParam(value = "rg_like") @Valid @NotBlank(message = "rg obrigatório") String rg
    ) {
        RespostaVerificacao respostaVerificacao = verificarRgCase.execute(rg);
        if (respostaVerificacao.disponivel()){
            return ResponseEntity.ok(respostaVerificacao);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(respostaVerificacao);
        }
    }


    @GetMapping("/cnpj")
    @Operation(
            summary = "Verificar um CNPJ",
            description = "Verificar se um determinado CPF está disponível"
    )
    public ResponseEntity<?> verificarCnpj(
            @RequestParam(value = "cnpj_like") @Valid @NotBlank(message = "Cnpj obrigatório") String cnpj
    ) {
        RespostaVerificacao respostaVerificacao = verificarCnpjCase.execute(cnpj);
        if (respostaVerificacao.disponivel()){
            return ResponseEntity.ok(respostaVerificacao);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(respostaVerificacao);
        }
    }
}
