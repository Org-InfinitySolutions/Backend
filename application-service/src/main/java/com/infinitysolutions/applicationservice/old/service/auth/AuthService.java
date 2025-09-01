package com.infinitysolutions.applicationservice.old.service.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaLogin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CredencialService credencialService;
    private final JwtEncoder jwtEncoder;

    @Value("${jwt.expiracao-segundos}")
    private long TEMPO_EXPIRACAO_SEGUNDOS;

    public RespostaLogin realizarLogin(String email, String senha) {
       CredencialEntity credencialEntityEncontrada = credencialService.obterCredencial(email, senha);
        Instant now = Instant.now();
        Instant expiresAt = now.plus(TEMPO_EXPIRACAO_SEGUNDOS, ChronoUnit.SECONDS);

        String scope = credencialEntityEncontrada.getCargoEntities().stream()
                .map(cargo -> "ROLE_" + cargo.getNome().name())
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(credencialEntityEncontrada.getFkUsuario().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        log.info("Token JWT gerado com sucesso para o usuário: {}", credencialEntityEncontrada.getFkUsuario());
        return new RespostaLogin(tokenValue, (int) TEMPO_EXPIRACAO_SEGUNDOS);
    }
}
