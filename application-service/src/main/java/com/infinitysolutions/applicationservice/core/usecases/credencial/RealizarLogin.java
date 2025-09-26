package com.infinitysolutions.applicationservice.core.usecases.credencial;

import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.CredenciaisGateway;
import com.infinitysolutions.applicationservice.core.gateway.ProjectConfigProvider;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class RealizarLogin {

    private final BuscarCredencial buscarCredencial;
    private final ProjectConfigProvider projectConfigProvider;
    private final JwtEncoder jwtEncoder;
    private final CredenciaisGateway credenciaisGateway;

    public RealizarLogin(BuscarCredencial buscarCredencial, ProjectConfigProvider projectConfigProvider, JwtEncoder jwtEncoder, CredenciaisGateway credenciaisGateway) {
        this.buscarCredencial = buscarCredencial;
        this.projectConfigProvider = projectConfigProvider;
        this.jwtEncoder = jwtEncoder;
        this.credenciaisGateway = credenciaisGateway;
    }

    public RespostaLogin execute(String email, String senha) {

        Credencial credencialEncontrada = buscarCredencial.execute(Email.of(email), senha);
        credencialEncontrada.registrarLogin();
        credenciaisGateway.save(credencialEncontrada);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(projectConfigProvider.getTempoExpiracaoJwt(), ChronoUnit.SECONDS);

        String scope = credencialEncontrada.getCargos().stream()
                .map(cargo -> "ROLE_" + cargo.getNome().name())
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .subject(credencialEncontrada.getUsuarioId().toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim("scope", scope)
                .build();

        String tokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new RespostaLogin(tokenValue, (int) projectConfigProvider.getTempoExpiracaoJwt());
    }
}
