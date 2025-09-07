package com.infinitysolutions.applicationservice.infrastructure.config.credencial;

import com.infinitysolutions.applicationservice.core.gateway.CodigoAutenticacaoGateway;
import com.infinitysolutions.applicationservice.core.gateway.ProjectConfigProvider;
import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.core.usecases.credencial.*;
import com.infinitysolutions.applicationservice.core.usecases.credencial.resetsenha.EnviarCodigoResetSenha;
import com.infinitysolutions.applicationservice.core.usecases.credencial.resetsenha.ResetarSenha;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailCodigoResetSenha;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailConfirmacaoResetEmail;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailConfirmacaoResetSenha;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import com.infinitysolutions.applicationservice.core.usecases.usuario.ExcluirUsuario;
import com.infinitysolutions.applicationservice.infrastructure.gateway.credencial.CredenciaisGatewayImpl;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;

@Configuration
public class CredenciaisUseCaseConfig {

    private final CredenciaisGatewayImpl credenciaisGateway;
    private final CodigoAutenticacaoGateway codigoAutenticacaoGateway;
    private final ObterCargo obterCargo;

    private final EnviarEmailConfirmacaoResetEmail enviarEmailConfirmacaoResetEmail;
    private final EnviarEmailConfirmacaoResetSenha enviarEmailConfirmacaoResetSenha;
    private final EnviarEmailCodigoResetSenha enviarEmailCodigoResetSenha;

    private final ExcluirUsuario excluirUsuario;
    private final BuscarUsuarioPorId buscarUsuarioPorId;

    private final ProjectConfigProvider projectConfigProvider;
    private final JwtEncoder jwtEncoder;

    public CredenciaisUseCaseConfig(
            CredenciaisGatewayImpl credenciaisGateway, CodigoAutenticacaoGateway codigoAutenticacaoGateway,
            ObterCargo obterCargo,
            EnviarEmailConfirmacaoResetEmail enviarEmailConfirmacaoResetEmail,
            EnviarEmailConfirmacaoResetSenha enviarEmailConfirmacaoResetSenha,
            EnviarEmailCodigoResetSenha enviarEmailCodigoResetSenha,
            ExcluirUsuario excluirUsuario,
            BuscarUsuarioPorId buscarUsuarioPorId,
            ProjectConfigProvider projectConfigProvider,
            JwtEncoder jwtEncoder
    ) {
        this.credenciaisGateway = credenciaisGateway;
        this.codigoAutenticacaoGateway = codigoAutenticacaoGateway;
        this.obterCargo = obterCargo;
        this.enviarEmailConfirmacaoResetEmail = enviarEmailConfirmacaoResetEmail;
        this.enviarEmailConfirmacaoResetSenha = enviarEmailConfirmacaoResetSenha;
        this.enviarEmailCodigoResetSenha = enviarEmailCodigoResetSenha;
        this.excluirUsuario = excluirUsuario;
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.projectConfigProvider = projectConfigProvider;
        this.jwtEncoder = jwtEncoder;
    }

    @Bean
    @Transactional
    public CriarCredencial criarCredenciais() {
        return new CriarCredencial(credenciaisGateway, obterCargo);
    }

    @Bean
    public BuscarCredencialPorId buscarCredenciaisPorId() {
        return new BuscarCredencialPorId(credenciaisGateway);
    }

    @Bean
    public BuscarCredencialPorEmail buscarCredencialPorEmail() {
        return new BuscarCredencialPorEmail(credenciaisGateway);
    }

    @Bean
    public BuscarCredencial buscarCredencial() {
        return new BuscarCredencial(buscarCredencialPorEmail());
    }

    @Bean
    public AlterarEmailCredencial alterarEmailCredencial() {
        return new AlterarEmailCredencial(credenciaisGateway, buscarCredenciaisPorId(), enviarEmailConfirmacaoResetEmail);
    }

    @Bean
    public AlterarSenhaCredencial alterarSenhaCredencial() {
        return new AlterarSenhaCredencial(buscarCredenciaisPorId(), buscarCredencialPorEmail(), credenciaisGateway, enviarEmailConfirmacaoResetSenha);
    }

    @Bean
    public DeletarCredencial deletarCredencial() {
        return new DeletarCredencial(buscarCredenciaisPorId(), credenciaisGateway, excluirUsuario);
    }

    @Bean
    public ResetarSenha resetarSenha() {
         return new ResetarSenha(codigoAutenticacaoGateway, alterarSenhaCredencial());
    }

    @Bean
    public EnviarCodigoResetSenha enviarCodigoResetSenha() {
        return new EnviarCodigoResetSenha(credenciaisGateway, codigoAutenticacaoGateway, enviarEmailCodigoResetSenha, buscarCredencialPorEmail(), buscarUsuarioPorId);
    }

    @Bean
    public RealizarLogin realizarLogin() {
        return new RealizarLogin(buscarCredencial(), projectConfigProvider, jwtEncoder, credenciaisGateway);
    }
}
