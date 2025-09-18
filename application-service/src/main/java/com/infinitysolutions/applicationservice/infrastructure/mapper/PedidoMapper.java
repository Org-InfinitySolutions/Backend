package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.*;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;

import java.util.List;
import java.util.stream.Collectors;


public class PedidoMapper {


    private PedidoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static PedidoEntity toPedido(PedidoCadastroDTO dto, UsuarioEntity usuarioEntity, EnderecoEntity enderecoEntity) {
        return new PedidoEntity(dto, usuarioEntity, enderecoEntity);
    }


    public static PedidoRespostaCadastroDTO toPedidoRespostaCadastroDTO(PedidoEntity pedidoEntity) {
        return new PedidoRespostaCadastroDTO(
                pedidoEntity.getId(),
                pedidoEntity.getUsuarioEntity().getId(),
                pedidoEntity.getUsuarioEntity().getNome(),
                pedidoEntity.getQtdItens(),
                pedidoEntity.getSituacao(),
                pedidoEntity.getTipo(),
                pedidoEntity.getDataCriacao(),
                pedidoEntity.getDescricao()
        );
    }

    public static PedidoRespostaDTO toPedidoRespostaDTO(PedidoEntity pedidoEntity) {
        PedidoRespostaDTO dto = new PedidoRespostaDTO();
        dto.setId(pedidoEntity.getId());
        dto.setQtdItens(pedidoEntity.getQtdItens());
        dto.setDataCriacao(pedidoEntity.getDataCriacao());
        dto.setDataEntrega(pedidoEntity.getDataEntrega());
        dto.setSituacao(pedidoEntity.getSituacao());
        return dto;
    }

    public static PedidoRespostaAdminDTO toPedidoRespostaAdminDTO(PedidoEntity pedidoEntity) {
        PedidoRespostaAdminDTO dto = new PedidoRespostaAdminDTO();
        dto.setId(pedidoEntity.getId());
        dto.setQtdItens(pedidoEntity.getQtdItens());
        dto.setDataCriacao(pedidoEntity.getDataCriacao());
        dto.setDataEntrega(pedidoEntity.getDataEntrega());
        dto.setNome(pedidoEntity.getUsuarioEntity().getNome());
        dto.setSituacao(pedidoEntity.getSituacao());
        return dto;
    }

    public static PedidoRespostaDetalhadoDTO toPedidoRespostaDetalhadoAdminDTO(PedidoEntity pedidoEntity, UsuarioRespostaDTO usuarioRespostaDTO, List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> documentos) {
        PedidoRespostaDetalhadoDTO dto = new PedidoRespostaDetalhadoDTO();
        dto.setId(pedidoEntity.getId());
        dto.setUsuario(usuarioRespostaDTO);
        dto.setProdutos(pedidoEntity.getProdutosPedido().stream().map(ProdutoEntityMapper::toProdutoPedidoRespostaDTO).toList());
        dto.setEndereco(new EnderecoResumidoDTO(pedidoEntity.getEnderecoEntity().getCep(), pedidoEntity.getEnderecoEntity().getLogradouro(), pedidoEntity.getEnderecoEntity().getNumero(), pedidoEntity.getEnderecoEntity().getCidade(), pedidoEntity.getEnderecoEntity().getEstado()));
        dto.setQtdItens(pedidoEntity.getQtdItens());
        dto.setDataCriacao(pedidoEntity.getDataCriacao());
        dto.setDataEntrega(pedidoEntity.getDataEntrega());
        dto.setDataRetirada(pedidoEntity.getDataRetirada());
        dto.setSituacao(pedidoEntity.getSituacao());
        dto.setDocumentos(documentos);
        dto.setDescricao(pedidoEntity.getDescricao());
        dto.setTipoPedido(pedidoEntity.getTipo());
        return dto;
    }

    public static List<PedidoRespostaDTO> toPedidoRespostaDTOList(List<PedidoEntity> pedidoEntities) {
        return pedidoEntities.stream()
                .map(PedidoMapper::toPedidoRespostaDTO)
                .collect(Collectors.toList());
    }

    public static List<PedidoRespostaDTO> toPedidoRespostaAdminDTOList(List<PedidoEntity> pedidoEntities) {
        return pedidoEntities.stream()
                .map(PedidoMapper::toPedidoRespostaAdminDTO)
                .collect(Collectors.toList());
    }
}
