package com.infinitysolutions.applicationservice.infrastructure.config.pedido;

import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoMudancaStatusPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoNovoPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailPedidoConcluidoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.pedido.AtualizarSituacaoPedido;
import com.infinitysolutions.applicationservice.core.usecases.pedido.BuscarPedidoPorId;
import com.infinitysolutions.applicationservice.core.usecases.pedido.CadastrarPedido;
import com.infinitysolutions.applicationservice.core.usecases.pedido.ListarTodosPedidos;
import com.infinitysolutions.applicationservice.core.usecases.produto.BuscarProdutosPorIds;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PedidoUseCaseConfig {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final ObterEndereco obterEndereco;
    private final BuscarProdutosPorIds buscarProdutosPorIds;
    private final PedidoGateway pedidoGateway;
    private final ProdutoGateway produtoGateway;
    private final ArquivoMetadadoGateway arquivoMetadadoGateway;
    private final EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido;
    private final EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario;
    private final EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido;

    public PedidoUseCaseConfig(BuscarUsuarioPorId buscarUsuarioPorId, BuscarCredencialPorId buscarCredencialPorId, ObterEndereco obterEndereco, BuscarProdutosPorIds buscarProdutosPorIds, PedidoGateway pedidoGateway, ProdutoGateway produtoGateway, ArquivoMetadadoGateway arquivoMetadadoGateway, EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido, EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario, EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido) {
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.obterEndereco = obterEndereco;
        this.buscarProdutosPorIds = buscarProdutosPorIds;
        this.pedidoGateway = pedidoGateway;
        this.produtoGateway = produtoGateway;
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
        this.enviarEmailNotificacaoNovoPedido = enviarEmailNotificacaoNovoPedido;
        this.enviarEmailPedidoConcluidoUsuario = enviarEmailPedidoConcluidoUsuario;
        this.enviarEmailNotificacaoMudancaStatusPedido = enviarEmailNotificacaoMudancaStatusPedido;
    }

    @Bean
    public CadastrarPedido cadastrarPedido() {
        return new CadastrarPedido(buscarUsuarioPorId, buscarCredencialPorId, obterEndereco, buscarProdutosPorIds, pedidoGateway, arquivoMetadadoGateway, enviarEmailPedidoConcluidoUsuario, enviarEmailNotificacaoNovoPedido);
    }

    @Bean
    public BuscarPedidoPorId buscarPedidoPorId() {
        return new BuscarPedidoPorId(pedidoGateway);
    }

    @Bean
    public AtualizarSituacaoPedido atualizarSituacaoPedido() {
        return new AtualizarSituacaoPedido(buscarPedidoPorId(), buscarCredencialPorId, produtoGateway, pedidoGateway, enviarEmailNotificacaoMudancaStatusPedido);
    }

    @Bean
    public ListarTodosPedidos listarTodosPedidos() {
        return new ListarTodosPedidos(pedidoGateway);
    }
}
