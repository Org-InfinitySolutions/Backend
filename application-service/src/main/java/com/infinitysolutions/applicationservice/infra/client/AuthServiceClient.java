package com.infinitysolutions.applicationservice.infra.client;

import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.auth.DesativarCredenciaisRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.auth.RespostaEmail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "auth-service", url = "${services.auth.url}", path = "/auth")
public interface AuthServiceClient {
    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    void registrarCredenciais(@RequestBody AuthServiceCadastroRequestDTO requestDTO, @RequestHeader("AUTH-API-KEY") String authApiKey);

    @PostMapping("/credenciais/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void desativarCredenciais(@PathVariable UUID usuarioId, @RequestHeader("AUTH-API-KEY") String authApiKey, @RequestBody DesativarCredenciaisRequestDTO requestDTO);

    @GetMapping("/credenciais/{usuarioId}/email")
    @ResponseStatus(HttpStatus.OK)
    RespostaEmail buscarEmail(@PathVariable UUID usuarioId, @RequestHeader("AUTH-API-KEY") String authApiKey);
}
