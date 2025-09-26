package com.infinitysolutions.applicationservice.core.usecases.pedido;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.domain.usuario.Credencial;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.core.gateway.PedidoGateway;
import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoNovoPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailPedidoConcluidoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoPedidoConcluido;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailPedidoConcluidoAdmin;
import com.infinitysolutions.applicationservice.core.usecases.endereco.ObterEndereco;
import com.infinitysolutions.applicationservice.core.usecases.produto.BuscarProdutosPorIds;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

public class CadastrarPedido {

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final BuscarCredencialPorId buscarCredencialPorId;
    private final ObterEndereco obterEndereco;
    private final BuscarProdutosPorIds buscarProdutosPorIds;
    private final PedidoGateway pedidoGateway;
    private final ArquivoMetadadoGateway arquivoMetadadoGateway;
    private final EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario;
    private final EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido;

    public CadastrarPedido(BuscarUsuarioPorId buscarUsuarioPorId, BuscarCredencialPorId buscarCredencialPorId, ObterEndereco obterEndereco, BuscarProdutosPorIds buscarProdutosPorIds, PedidoGateway pedidoGateway, ArquivoMetadadoGateway arquivoMetadadoGateway, EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario, EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido) {
        this.buscarUsuarioPorId = buscarUsuarioPorId;
        this.buscarCredencialPorId = buscarCredencialPorId;
        this.obterEndereco = obterEndereco;
        this.buscarProdutosPorIds = buscarProdutosPorIds;
        this.pedidoGateway = pedidoGateway;
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
        this.enviarEmailPedidoConcluidoUsuario = enviarEmailPedidoConcluidoUsuario;
        this.enviarEmailNotificacaoNovoPedido = enviarEmailNotificacaoNovoPedido;
    }

    public Pedido execute(CadastrarPedidoInput input, MultipartFile documentoAuxiliar, UUID uuid) {

        Usuario usuarioEncontrado = buscarUsuarioPorId.execute(uuid);
        Credencial credencialEncontrada = buscarCredencialPorId.execute(uuid);

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

        List<ProdutoPedido> produtosPedido = produtosEncontrados.stream()
                .map(produto -> {
                    Integer quantidade = idQuantidadeMap.get(produto.getId());
                    ProdutoPedido produtoPedido = new ProdutoPedido();
                    produtoPedido.setProduto(produto);
                    produtoPedido.setQtd(quantidade);
                    return produtoPedido;
                }).toList();

        pedidoCriado.setProdutosPedido(produtosPedido);

        Pedido pedidoSalvo = pedidoGateway.save(pedidoCriado);

        for (ProdutoPedido produtoPedido : pedidoSalvo.getProdutosPedido()) {
            produtoPedido.setPedido(pedidoSalvo);
        }

        if (documentoAuxiliar != null && !documentoAuxiliar.isEmpty()) {
            enviarDocumentoAuxiliar(documentoAuxiliar, pedidoSalvo);
        }

        enviarEmailPedidoConcluidoUsuario.execute(new EmailNotificacaoPedidoConcluido(
                usuarioEncontrado.getNome(),
                pedidoSalvo.getId().toString(),
                credencialEncontrada.getEmail(),
                String.valueOf(pedidoSalvo.getQtdItens())
        ));

        String descricaoPedido = pedidoSalvo.getDescricao() != null && !pedidoSalvo.getDescricao().trim().isEmpty()
                ? pedidoSalvo.getDescricao()
                : "Sem descrição";

        enviarEmailNotificacaoNovoPedido.execute(new EmailPedidoConcluidoAdmin(
                usuarioEncontrado.getNome(),
                pedidoSalvo.getId().toString(),
                credencialEncontrada.getEmail(),
                String.valueOf(pedidoSalvo.getQtdItens()),
                usuarioEncontrado.getTelefoneCelular(),
                descricaoPedido
        ));

        return pedidoSalvo;
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
