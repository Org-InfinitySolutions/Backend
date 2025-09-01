package com.infinitysolutions.applicationservice.infrastructure.config.usuario;

import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.CriarPessoaFisica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.ListarPessoaFisica;
import com.infinitysolutions.applicationservice.infrastructure.gateway.usuario.PessoaFisicaGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaFisicaUseCaseConfig {

    private final PessoaFisicaGatewayImpl pessoaFisicaGateway;
    private final ObterEndereco obterEndereco;

    public PessoaFisicaUseCaseConfig(PessoaFisicaGatewayImpl pessoaFisicaGateway, ObterEndereco obterEndereco) {
        this.pessoaFisicaGateway = pessoaFisicaGateway;
        this.obterEndereco = obterEndereco;
    }

    @Bean
    public CriarPessoaFisica criarPessoaFisica() {
        return new CriarPessoaFisica(pessoaFisicaGateway, obterEndereco);
    }

    @Bean
    public ListarPessoaFisica listarPessoaFisica() {
        return new ListarPessoaFisica(pessoaFisicaGateway);
    }

    @Bean
    public AtualizarPessoaFisica atualizarPessoaFisica() {
        return new AtualizarPessoaFisica(obterEndereco, pessoaFisicaGateway);
    }
}
