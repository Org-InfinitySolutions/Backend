package com.infinitysolutions.applicationservice.infrastructure.config.pedido;

import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.pedido.CadastrarPedido;
import com.infinitysolutions.applicationservice.core.usecases.produto.BuscarProdutosPorIds;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoUseCaseConfig {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final ObterEndereco obterEndereco;
    private final BuscarProdutosPorIds buscarProdutosPorIds;
    private final PedidoGateway pedidoGateway;
    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public PedidoUseCaseConfig(BuscarUsuarioPorId buscarUsuarioPorId, ObterEndereco obterEndereco, BuscarProdutosPorIds buscarProdutosPorIds, PedidoGateway pedidoGateway, ArquivoMetadadoGateway arquivoMetadadoGateway) {
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.obterEndereco = obterEndereco;
        this.buscarProdutosPorIds = buscarProdutosPorIds;
        this.pedidoGateway = pedidoGateway;
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }

    @Bean
    public CadastrarPedido cadastrarPedido() {
        return new CadastrarPedido(buscarUsuarioPorId, obterEndereco, buscarProdutosPorIds, pedidoGateway, arquivoMetadadoGateway);
    }
}
