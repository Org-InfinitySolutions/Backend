package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.exception.OperacaoNaoPermitidaException;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoMudancaStatusPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoMudancaStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class AtualizarSituacaoPedido {

    private final BuscarPedidoPorId buscarPedidoPorId;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final ProdutoGateway produtoGateway;
    private final PedidoGateway pedidoGateway;
    private final EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido;

    public AtualizarSituacaoPedido(BuscarPedidoPorId buscarPedidoPorId, BuscarCredencialPorId buscarCredencialPorId, ProdutoGateway produtoGateway, PedidoGateway pedidoGateway, EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido) {
        this.buscarPedidoPorId = buscarPedidoPorId;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.produtoGateway = produtoGateway;
        this.pedidoGateway = pedidoGateway;
        this.enviarEmailNotificacaoMudancaStatusPedido = enviarEmailNotificacaoMudancaStatusPedido;
    }

    public Pedido execute(Integer id, UUID idUsuario, SituacaoPedido statusNovo, boolean isCustomer) {
        Pedido pedidoEncontrado = buscarPedidoPorId.execute(id, idUsuario, !isCustomer);
        Credencial credencialEncontrada = buscarCredencialPorId.execute(pedidoEncontrado.getUsuario().getId());
        SituacaoPedido statusAntigo = pedidoEncontrado.getSituacao();

        if (statusNovo == SituacaoPedido.CANCELADO && statusAntigo != SituacaoPedido.EM_ANALISE && isCustomer) {
            System.out.printf("Tentativa do usuário comum de cancelar pedido %s que não está em análise. Situação atual: %s",
                    id, statusAntigo);
            throw OperacaoNaoPermitidaException.cancelamentoPedido(id);
        }

        if (statusNovo == statusAntigo) {
            System.out.println("Tentativa de atualizar a situação do pedido para uma situação já existente");
            throw OperacaoNaoPermitidaException.statusIguais(statusNovo);
        }

        System.out.printf("Atualizando situação do pedido %s de %s para %s", id, statusAntigo, statusNovo);
        ajustarEstoque(pedidoEncontrado, statusAntigo, statusNovo);

        LocalDateTime agora = LocalDateTime.now();
        switch (statusNovo) {
            case APROVADO -> pedidoEncontrado.setDataAprovacao(agora);
            case EM_EVENTO -> pedidoEncontrado.setDataInicioEvento(agora);
            case FINALIZADO -> pedidoEncontrado.setDataFinalizacao(agora);
            case CANCELADO -> pedidoEncontrado.setDataCancelamento(agora);
            case EM_ANALISE -> {
                // Não precisa definir data específica, pois já é o estado inicial
            }
        }

        pedidoEncontrado.setSituacao(statusNovo);

        Pedido pedidoSalvo = pedidoGateway.atualizarStatus(pedidoEncontrado);
        enviarEmailNotificacaoMudancaStatusPedido.execute( new EmailNotificacaoMudancaStatus(
                pedidoEncontrado.getUsuario().getNome(),
                pedidoEncontrado.getId().toString(),
                statusAntigo.getNome(),
                statusNovo.getNome(),
                credencialEncontrada.getEmail()
        ));
        return pedidoSalvo;
    }

    private void ajustarEstoque(Pedido pedidoEntity, SituacaoPedido statusAntigo, SituacaoPedido statusNovo) {
        System.out.printf("Iniciando ajuste de estoque para pedido %s - Status: %s -> %s", pedidoEntity.getId(), statusAntigo, statusNovo);

        if (statusAntigo == SituacaoPedido.EM_ANALISE && statusNovo == SituacaoPedido.APROVADO) {
            reduzirEstoque(pedidoEntity);
        }
        else if ((statusNovo == SituacaoPedido.FINALIZADO || statusNovo == SituacaoPedido.CANCELADO) && estoqueJaFoiReduzido(statusAntigo)) {
            devolverEstoque(pedidoEntity);
        }

        System.out.println("Ajuste de estoque concluído para pedido " + pedidoEntity.getId());
    }

    private void reduzirEstoque(Pedido pedidoEntity) {
        System.out.println("Reduzindo estoque dos produtos do pedido " + pedidoEntity.getId());

        for (ProdutoPedido produtoPedidoEntity : pedidoEntity.getProdutosPedido()) {
            Produto produtoEntity = produtoPedidoEntity.getProduto();
            int quantidadePedida = produtoPedidoEntity.getQtd();
            int estoqueAtual = produtoEntity.getQtdEstoque();

            if (estoqueAtual < quantidadePedida) {
                System.out.printf("Estoque insuficiente para produto %s (ID: %s). Disponível: %s, Solicitado: %s", produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, quantidadePedida);

                throw new OperacaoNaoPermitidaException(
                        String.format("Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d", produtoEntity.getModelo(), estoqueAtual, quantidadePedida));
            }

            int novoEstoque = estoqueAtual - quantidadePedida;
            produtoEntity.setQtdEstoque(novoEstoque);
            produtoGateway.update(produtoEntity);

            System.out.printf("Estoque reduzido para produto %s (ID: %s): %s -> %s", produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, novoEstoque);
        }
    }

    private void devolverEstoque(Pedido pedidoEntity) {
        System.out.println("Devolvendo estoque dos produtos do pedido " + pedidoEntity.getId());

        for (ProdutoPedido produtoPedidoEntity : pedidoEntity.getProdutosPedido()) {
            Produto produtoEntity = produtoPedidoEntity.getProduto();
            int quantidadePedida = produtoPedidoEntity.getQtd();
            int estoqueAtual = produtoEntity.getQtdEstoque();
            int novoEstoque = estoqueAtual + quantidadePedida;

            produtoEntity.setQtdEstoque(novoEstoque);
            produtoGateway.update(produtoEntity);

            System.out.printf("Estoque devolvido para produto %s (ID: %s): %s -> %s", produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, novoEstoque);
        }
    }

    private boolean estoqueJaFoiReduzido(SituacaoPedido statusAnterior) {
        return statusAnterior == SituacaoPedido.APROVADO ||
                statusAnterior == SituacaoPedido.EM_EVENTO;
    }
}
