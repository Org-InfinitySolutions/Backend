package com.infinitysolutions.applicationservice.infrastructure.config.usuario;

import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.VerificarCnpj;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridica;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.CriarPessoaJuridica;
import com.infinitysolutions.applicationservice.infrastructure.gateway.usuario.PessoaJuridicaGatewayImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PessoaJuridicaUseCaseConfig {

    private final PessoaJuridicaGatewayImpl pessoaJuridicaGateway;
    private final ObterEndereco obterEndereco;

    public PessoaJuridicaUseCaseConfig(PessoaJuridicaGatewayImpl pessoaJuridicaGateway, ObterEndereco obterEndereco) {
        this.pessoaJuridicaGateway = pessoaJuridicaGateway;
        this.obterEndereco = obterEndereco;
    }

    @Bean
    public CriarPessoaJuridica criarPessoaJuridica() {
        return new CriarPessoaJuridica(pessoaJuridicaGateway, obterEndereco);
    }

    @Bean
    public AtualizarPessoaJuridica atualizarPessoaJuridica() {
        return new AtualizarPessoaJuridica(obterEndereco, pessoaJuridicaGateway);
    }

    @Bean
    public VerificarCnpj verificarCnpj() {
        return new VerificarCnpj(pessoaJuridicaGateway);
    }
}
