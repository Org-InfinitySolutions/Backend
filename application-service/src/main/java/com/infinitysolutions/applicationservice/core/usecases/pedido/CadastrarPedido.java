package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.produto.BuscarProdutosPorIds;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

public class CadastrarPedido {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final ObterEndereco obterEndereco;
    private final BuscarProdutosPorIds buscarProdutosPorIds;
    private final PedidoGateway pedidoGateway;
    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public CadastrarPedido(BuscarUsuarioPorId buscarUsuarioPorId, ObterEndereco obterEndereco, BuscarProdutosPorIds buscarProdutosPorIds, PedidoGateway pedidoGateway, ArquivoMetadadoGateway arquivoMetadadoGateway) {
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.obterEndereco = obterEndereco;
        this.buscarProdutosPorIds = buscarProdutosPorIds;
        this.pedidoGateway = pedidoGateway;
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }

    public Pedido execute(CadastrarPedidoInput input, MultipartFile documentoAuxiliar, UUID uuid) {

        Usuario usuarioEncontrado = buscarUsuarioPorId.execute(uuid);

        Map<Integer, Integer> idQuantidadeMap = input.produtos().stream()
                .collect(Collectors.toMap(CadastrarPedidoInput.ProdutoPedidoInput::produtoId, CadastrarPedidoInput.ProdutoPedidoInput::quantidade));

        Set<Integer> idProdutos = new HashSet<>(idQuantidadeMap.keySet());

        Set<Produto> produtosEncontrados = buscarProdutosPorIds.execute(idProdutos);

        if (produtosEncontrados.size() != idProdutos.size()) {
            Set<Integer> idsEncontrados = produtosEncontrados.stream()
                    .map(Produto::getId).collect(Collectors.toSet());

            List<Integer> idsNaoEncontrados = idProdutos.stream()
                    .filter(id -> !idsEncontrados.contains(id)).toList();

            throw new RecursoNaoEncontradoException("Produtos não encontrados: " + idsNaoEncontrados);
        }


        Endereco enderecoEncontrado = obterEndereco.execute(input.enderecoInput());

        Pedido pedidoCriado = PedidoMapper.toPedido(input, usuarioEncontrado, enderecoEncontrado);

        // Create the product list before saving
        List<ProdutoPedido> produtosPedido = produtosEncontrados.stream()
                .map(produto -> {
                    Integer quantidade = idQuantidadeMap.get(produto.getId());
                    ProdutoPedido produtoPedido = new ProdutoPedido();
                    produtoPedido.setProduto(produto);
                    produtoPedido.setQtd(quantidade);
                    return produtoPedido;
                }).toList();

        // Set the products before saving
        pedidoCriado.setProdutosPedido(produtosPedido);

        // Save the pedido with all its data in a single operation
        Pedido pedidoSalvo = pedidoGateway.save(pedidoCriado);

        // Now that we have the saved pedido, we can associate it with the products
        for (ProdutoPedido produtoPedido : pedidoSalvo.getProdutosPedido()) {
            produtoPedido.setPedido(pedidoSalvo);
        }

        if (documentoAuxiliar != null && !documentoAuxiliar.isEmpty()) {
            enviarDocumentoAuxiliar(documentoAuxiliar, pedidoSalvo);
        }

        //TODO: fazer envio de email

        return pedidoSalvo;
    }

    private static ProdutoPedido createProdutoPedido(Produto produto, Pedido pedidoSalvo, Integer quatidade) {
        ProdutoPedido novoProdutoPedido = new ProdutoPedido();
        novoProdutoPedido.setPedido(pedidoSalvo);
        novoProdutoPedido.setProduto(produto);
        novoProdutoPedido.setQtd(quatidade);
        return novoProdutoPedido;
    }

    private void enviarDocumentoAuxiliar(MultipartFile arquivo, Pedido pedido) {
        long maxSize = 20L * 1024L * 1024L; // 20MB

        if (arquivo.getSize() > maxSize) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 20MB");
        }
        System.out.println(("Documento auxiliar está pronto para ser enviado ao bucket!!!"));
        arquivoMetadadoGateway.enviarDocumentoAuxiliar(arquivo, pedido);
    }
}
