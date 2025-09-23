package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.pedido.ProdutoPedido;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.core.usecases.pedido.CadastrarPedidoInput;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.*;
import com.infinitysolutions.applicationservice.infrastructure.mapper.produto.ProdutoEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedidoEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PedidoMapper {


    private PedidoMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static PedidoEntity toEntity(
            UsuarioEntity usuarioEntity, List<ProdutoPedidoEntity> produtoPedidoEntityList,
            EnderecoEntity enderecoEntity, SituacaoPedido situacaoPedido,
            TipoPedido tipoPedido, LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao, LocalDateTime dataAprovacao,
            LocalDateTime dataInicioEvento, LocalDateTime dataFinalizacao,
            LocalDateTime dataCancelamento, LocalDateTime dataEntrega,
            LocalDateTime dataRetirada, String descricao,
            List<ArquivoMetadadosEntity> documentos
    ) {
        PedidoEntity novoPedidoEntity = new PedidoEntity();
        novoPedidoEntity.setUsuarioEntity(usuarioEntity);
        novoPedidoEntity.setProdutosPedido(produtoPedidoEntityList);
        novoPedidoEntity.setEnderecoEntity(enderecoEntity);
        novoPedidoEntity.setSituacao(situacaoPedido);
        novoPedidoEntity.setTipo(tipoPedido);
        novoPedidoEntity.setDataCriacao(dataCriacao);
        novoPedidoEntity.setDataAtualizacao(dataAtualizacao);
        novoPedidoEntity.setDataAprovacao(dataAprovacao);
        novoPedidoEntity.setDataInicioEvento(dataInicioEvento);
        novoPedidoEntity.setDataFinalizacao(dataFinalizacao);
        novoPedidoEntity.setDataCancelamento(dataCancelamento);
        novoPedidoEntity.setDataEntrega(dataEntrega);
        novoPedidoEntity.setDataRetirada(dataRetirada);
        novoPedidoEntity.setDescricao(descricao);
        novoPedidoEntity.setDocumentos(documentos);
        return novoPedidoEntity;
    }

    public static Pedido toPedido(CadastrarPedidoInput input, Usuario usuario, Endereco endereco) {
        return new Pedido(input, usuario, endereco);
    }

    public static Pedido toPedido(PedidoEntity pedidoEntity, Usuario usuario, List<ProdutoPedido> produtosPedido, Endereco endereco, List<ArquivoMetadado> documentos) {
        return new Pedido(pedidoEntity.getId(), usuario, produtosPedido, endereco, pedidoEntity.getSituacao(), pedidoEntity.getTipo(), pedidoEntity.getDataCriacao(), pedidoEntity.getDataAtualizacao(), pedidoEntity.getDataAprovacao(), pedidoEntity.getDataInicioEvento(), pedidoEntity.getDataFinalizacao(), pedidoEntity.getDataCancelamento(), pedidoEntity.getDataEntrega(), pedidoEntity.getDataRetirada(), pedidoEntity.getDescricao(), documentos);
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
    public static PedidoRespostaDTO toPedidoRespostaDTO(PedidoEntity pedidoEntity) {
        PedidoRespostaDTO dto = new PedidoRespostaDTO();
        dto.setId(pedidoEntity.getId());
        dto.setQtdItens(pedidoEntity.getQtdItens());
        dto.setDataCriacao(pedidoEntity.getDataCriacao());
        dto.setDataEntrega(pedidoEntity.getDataEntrega());
        dto.setSituacao(pedidoEntity.getSituacao());
        return dto;
    }

    public static PedidoRespostaDTO toPedidoRespostaDTO(Pedido pedido) {
        PedidoRespostaDTO dto = new PedidoRespostaDTO();
        dto.setId(pedido.getId());
        dto.setQtdItens(pedido.getQtdItens());
        dto.setDataCriacao(pedido.getDataCriacao());
        dto.setDataEntrega(pedido.getDataEntrega());
        dto.setSituacao(pedido.getSituacao());
        return dto;
    }

    public static PedidoRespostaAdminDTO toPedidoRespostaAdminDTO(Pedido pedidoEntity) {
        PedidoRespostaAdminDTO dto = new PedidoRespostaAdminDTO();
        dto.setId(pedidoEntity.getId());
        dto.setQtdItens(pedidoEntity.getQtdItens());
        dto.setDataCriacao(pedidoEntity.getDataCriacao());
        dto.setDataEntrega(pedidoEntity.getDataEntrega());
        dto.setNome(pedidoEntity.getUsuario().getNome());
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

    public static PedidoRespostaDetalhadoDTO toPedidoRespostaDetalhadoAdminDTO(Pedido pedidoEntity, UsuarioRespostaDTO usuarioRespostaDTO, List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> documentos) {
        PedidoRespostaDetalhadoDTO dto = new PedidoRespostaDetalhadoDTO();
        dto.setId(pedidoEntity.getId());
        dto.setUsuario(usuarioRespostaDTO);
        dto.setProdutos(pedidoEntity.getProdutosPedido().stream().map(ProdutoEntityMapper::toProdutoPedidoRespostaDTO).toList());
        dto.setEndereco(new EnderecoResumidoDTO(pedidoEntity.getEndereco().getCep(), pedidoEntity.getEndereco().getLogradouro(), pedidoEntity.getEndereco().getNumero(), pedidoEntity.getEndereco().getCidade(), pedidoEntity.getEndereco().getEstado()));
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


    public static List<PedidoRespostaDTO> toPedidoRespostaDTOList(List<Pedido> pedidoEntities) {
        return pedidoEntities.stream()
                .map(PedidoMapper::toPedidoRespostaDTO)
                .toList();
    }

    public static List<PedidoRespostaDTO> toPedidoRespostaAdminDTOList(List<Pedido> pedidoEntities) {
        return pedidoEntities.stream()
                .map(PedidoMapper::toPedidoRespostaAdminDTO)
                .collect(Collectors.toList());
    }
}
