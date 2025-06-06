package com.infinitysolutions.applicationservice.controller;

import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.dto.validacao.RespostaVerificacao;
import com.infinitysolutions.applicationservice.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    private final UsuarioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
        summary = "Cadastrar um novo usuário",
        description = "Cadastra um novo usuário no sistema, podendo ser pessoa física (PF) ou pessoa jurídica (PJ)"
    )
    public UsuarioRespostaCadastroDTO cadastrar(@Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        return service.cadastrar(usuarioCadastroDTO);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Listar todos os usuários",
        description = "Retorna uma lista de todos os usuários cadastrados no sistema"
    )
    public List<UsuarioRespostaCadastroDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Buscar usuário por ID",
        description = "Retorna os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaDTO buscarPorId(@PathVariable UUID usuarioId) {
        return service.buscarPorId(usuarioId);
    }

//    @DeleteMapping("/{usuarioId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @Operation(
//        summary = "Excluir usuário por ID",
//        description = "Remove um usuário específico do sistema com base no ID fornecido"
//    )
//    public void excluir(@PathVariable UUID usuarioId) {
//        service.excluir(usuarioId);
//    }

    @PutMapping("/{usuarioId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
        summary = "Atualizar usuário por ID",
        description = "Atualiza os detalhes de um usuário específico com base no ID fornecido"
    )
    public UsuarioRespostaCadastroDTO atualizar(@PathVariable UUID usuarioId, @Valid @RequestBody UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        return service.atualizar(usuarioId, usuarioAtualizacaoDTO);
    }

    @GetMapping("/cpf")
    @Operation(
            summary = "Verificar um CPF",
            description = "Verificar se um determinado CPF está disponível"
    )
    public ResponseEntity<?> verificarCpf(
            @RequestParam(value = "cpf_like") @Valid @NotBlank(message = "Cpf obrigatório") String cpf
    ) {
        RespostaVerificacao respostaVerificacao = service.verificarCpf(cpf);
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
        RespostaVerificacao respostaVerificacao = service.verificarCnpj(cnpj);
        if (respostaVerificacao.disponivel()){
            return ResponseEntity.ok(respostaVerificacao);
        }else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(respostaVerificacao);
        }
    }
}
