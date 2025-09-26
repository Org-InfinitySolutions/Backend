package com.infinitysolutions.applicationservice.infrastructure.config;

import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.AtualizarImagemProduto;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AdicionarDocumentoUsuario;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ArquivoMetadadoUseCaseConfig {

    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public ArquivoMetadadoUseCaseConfig(ArquivoMetadadoGateway arquivoMetadadoGateway) {
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }

    @Bean
    public AdicionarDocumentoUsuario adicionarDocumentoUsuario() {
        return new AdicionarDocumentoUsuario(arquivoMetadadoGateway);
    }

    @Bean
    public AtualizarImagemProduto atualizarImagemProduto() {
        return new AtualizarImagemProduto(arquivoMetadadoGateway);
    }

}
