package com.infinitysolutions.applicationservice.infrastructure.config.usuario;

import com.infinitysolutions.applicationservice.core.usecases.credencial.CriarCredenciais;
import com.infinitysolutions.applicationservice.core.usecases.usuario.*;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.infrastructure.gateway.usuario.UsuarioGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsuarioUseCaseConfig {

    private final UsuarioGatewayImpl usuarioGatewayImpl;

    private final CriarPessoaFisica criarPessoaFisica;

    private final CriarPessoaJuridica criarPessoaJuridica;

    private final CriarCredenciais criarCredenciais;

    private final AtualizarPessoaFisica atualizarPessoaFisica;
    private final AtualizarPessoaJuridica atualizarPessoaJuridica;



    public UsuarioUseCaseConfig(
            UsuarioGatewayImpl usuarioGatewayImpl,
            CriarPessoaFisica criarPessoaFisica,
            CriarPessoaJuridica criarPessoaJuridica,
            CriarCredenciais criarCredenciais,
            AtualizarPessoaFisica atualizarPessoaFisica,
            AtualizarPessoaJuridica atualizarPessoaJuridica
    ) {
        this.usuarioGatewayImpl = usuarioGatewayImpl;
        this.criarPessoaFisica = criarPessoaFisica;
        this.criarPessoaJuridica = criarPessoaJuridica;
        this.criarCredenciais = criarCredenciais;
        this.atualizarPessoaFisica = atualizarPessoaFisica;
        this.atualizarPessoaJuridica = atualizarPessoaJuridica;
    }

    @Bean
    public CriarUsuario criarUsuario() {
        return new CriarUsuario(criarPessoaJuridica, criarPessoaFisica, criarCredenciais);
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
        return new ExcluirUsuario(usuarioGatewayImpl);
    }

    @Bean
    public AtualizarUsuario atualizarUsuario() {
        return new AtualizarUsuario(atualizarPessoaFisica, atualizarPessoaJuridica);
    }

}
