package com.infinitysolutions.applicationservice.infrastructure.config.usuario;

import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.core.gateway.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.core.usecases.cargo.ObterCargo;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredencial;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailCadastro;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoCadastro;
import com.infinitysolutions.applicationservice.core.usecases.usuario.*;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.infrastructure.gateway.credencial.CredenciaisGatewayImpl;
import com.infinitysolutions.applicationservice.infrastructure.gateway.usuario.UsuarioGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class UsuarioUseCaseConfig {

    private final UsuarioGatewayImpl usuarioGatewayImpl;
    private final CredenciaisGatewayImpl credenciaisGateway;

    private final CriarPessoaFisica criarPessoaFisica;

    private final CriarPessoaJuridica criarPessoaJuridica;

    private final CriarCredencial criarCredencial;
    private final BuscarCredencialPorId buscarCredencialPorId;

    private final AtualizarPessoaFisica atualizarPessoaFisica;
    private final AtualizarPessoaJuridica atualizarPessoaJuridica;

    private final EnviarEmailCadastro enviarEmailCadastro;
    private final EnviarEmailNotificacaoCadastro enviarEmailNotificacaoCadastro;

    private final PessoaFisicaGateway pessoaFisicaGateway;
    private final PessoaJuridicaGateway pessoaJuridicaGateway;

    private final ObterCargo obterCargo;

    public UsuarioUseCaseConfig(
            UsuarioGatewayImpl usuarioGatewayImpl,
            @Lazy CredenciaisGatewayImpl credenciaisGateway,
            CriarPessoaFisica criarPessoaFisica,
            CriarPessoaJuridica criarPessoaJuridica,
            @Lazy CriarCredencial criarCredencial,
            @Lazy BuscarCredencialPorId buscarCredencialPorId,
            AtualizarPessoaFisica atualizarPessoaFisica,
            AtualizarPessoaJuridica atualizarPessoaJuridica,
            EnviarEmailCadastro enviarEmailCadastro,
            EnviarEmailNotificacaoCadastro enviarEmailNotificacaoCadastro, PessoaFisicaGateway pessoaFisicaGateway, PessoaJuridicaGateway pessoaJuridicaGateway, ObterCargo obterCargo
    ) {
        this.usuarioGatewayImpl = usuarioGatewayImpl;
        this.credenciaisGateway = credenciaisGateway;
        this.criarPessoaFisica = criarPessoaFisica;
        this.criarPessoaJuridica = criarPessoaJuridica;
        this.criarCredencial = criarCredencial;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.atualizarPessoaFisica = atualizarPessoaFisica;
        this.atualizarPessoaJuridica = atualizarPessoaJuridica;
        this.enviarEmailCadastro = enviarEmailCadastro;
        this.enviarEmailNotificacaoCadastro = enviarEmailNotificacaoCadastro;
        this.pessoaFisicaGateway = pessoaFisicaGateway;
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
        this.obterCargo = obterCargo;
    }

    @Bean
    public CriarUsuario criarUsuario() {
        return new CriarUsuario(criarPessoaJuridica, criarPessoaFisica, criarCredencial, enviarEmailCadastro, enviarEmailNotificacaoCadastro);
    }

    @Bean
    public ListarTodosUsuarios listarTodosUsuarios() {
        return new ListarTodosUsuarios(usuarioGatewayImpl);
    }

    @Bean
    public BuscarUsuarioPorId buscarUsuarioPorId() {
        return new BuscarUsuarioPorId(usuarioGatewayImpl);
    }

    @Bean
    public ExcluirUsuario excluirUsuario() {
        return new ExcluirUsuario(usuarioGatewayImpl, pessoaFisicaGateway, pessoaJuridicaGateway);
    }

    @Bean
    public AtualizarUsuario atualizarUsuario() {
        return new AtualizarUsuario(atualizarPessoaFisica, atualizarPessoaJuridica);
    }

    @Bean
    public PromoverUsuario promoverUsuario() {
        return new PromoverUsuario(buscarUsuarioPorId(), buscarCredencialPorId, obterCargo, credenciaisGateway);
    }

    @Bean
    public RebaixarUsuario rebaixarUsuario() {
        return new RebaixarUsuario(buscarCredencialPorId, buscarUsuarioPorId(), obterCargo, credenciaisGateway);
    }

}
