package com.infinitysolutions.authservice.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import com.infinitysolutions.authservice.model.dto.RequisicaoCadastro;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.infinitysolutions.authservice.service.AuthService;
import com.infinitysolutions.authservice.service.CredencialService;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação e gerenciamento de credenciais")
public class AuthController {

    private final CredencialService credencialService;
    private final AuthService authService;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody RequisicaoLogin requisicaoLogin){
//        try {
//            return ResponseEntity.ok(authService.autenticar);
//        } catch (AuthenticationException e) {
//            return new ResponseEntity<>(new ErrorResponse("credenciais_invalidas", "Credenciais invalidas"), HttpStatus.UNAUTHORIZED);
//        }
//    }


    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public void cadastrar(@Valid @RequestBody RequisicaoCadastro requisicaoCadastro){
        credencialService.criarCredencialUsuario(requisicaoCadastro.idUsuario(), requisicaoCadastro.email(), requisicaoCadastro.senha());
    }
}
