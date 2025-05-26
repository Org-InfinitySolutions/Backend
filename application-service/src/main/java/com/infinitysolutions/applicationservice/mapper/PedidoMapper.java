package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.mapper.produto.ProdutoMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.model.dto.pedido.*;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;

import java.util.List;
import java.util.stream.Collectors;

public class PedidoMapper {

    private PedidoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Pedido toPedido(PedidoCadastroDTO dto, Usuario usuario, Endereco endereco) {
        return new Pedido(dto, usuario, endereco);
    }


    public static PedidoRespostaCadastroDTO toPedidoRespostaCadastroDTO(Pedido pedido) {
        return new PedidoRespostaCadastroDTO(
                pedido.getId(),
                pedido.getUsuario().getId(),
                pedido.getUsuario().getNome(),
                pedido.getQtdItens(),
                pedido.getSituacao(),
                pedido.getTipo(),
                pedido.getDataCriacao(),
                pedido.getDescricao()
        );
    }

    public static PedidoRespostaDTO toPedidoRespostaDTO(Pedido pedido) {
        PedidoRespostaDTO dto = new PedidoRespostaDTO();
        dto.setId(pedido.getId());
        dto.setQtdItens(pedido.getQtdItens());
        dto.setData(pedido.getDataCriacao());
        dto.setSituacao(pedido.getSituacao());
        return dto;
    }

    public static PedidoRespostaAdminDTO toPedidoRespostaAdminDTO(Pedido pedido) {
        PedidoRespostaAdminDTO dto = new PedidoRespostaAdminDTO();
        dto.setId(pedido.getId());
        dto.setQtdItens(pedido.getQtdItens());
        dto.setData(pedido.getDataCriacao());
        dto.setNome(pedido.getUsuario().getNome());
        dto.setSituacao(pedido.getSituacao());
        return dto;
    }

    public static PedidoRespostaDetalhadoDTO toPedidoRespostaDetalhadoAdminDTO(Pedido pedido, UsuarioRespostaDTO usuarioRespostaDTO) {
        PedidoRespostaDetalhadoDTO dto = new PedidoRespostaDetalhadoDTO();
        dto.setId(pedido.getId());
        dto.setUsuario(usuarioRespostaDTO);
        dto.setProdutos(pedido.getProdutosPedido().stream().map(ProdutoMapper::toProdutoPedidoRespostaDTO).toList());
        dto.setEndereco(new EnderecoResumidoDTO(pedido.getEndereco().getCep(), pedido.getEndereco().getLogradouro(), pedido.getEndereco().getNumero(), pedido.getEndereco().getCidade(), pedido.getEndereco().getEstado()));
        dto.setQtdItens(pedido.getQtdItens());
        dto.setData(pedido.getDataCriacao());
        dto.setSituacao(pedido.getSituacao());
        return dto;
    }

    public static List<PedidoRespostaDTO> toPedidoRespostaDTOList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(PedidoMapper::toPedidoRespostaDTO)
                .collect(Collectors.toList());
    }

    public static List<PedidoRespostaDTO> toPedidoRespostaAdminDTOList(List<Pedido> pedidos) {
        return pedidos.stream()
                .map(PedidoMapper::toPedidoRespostaAdminDTO)
                .collect(Collectors.toList());
    }
}
