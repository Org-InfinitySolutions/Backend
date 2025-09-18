package com.infinitysolutions.applicationservice.infrastructure.mapper.produto;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.mapper.CategoriaMapper;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.infrastructure.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoPedidoRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedido;

public class ProdutoEntityMapper {
    
    public static ProdutoRespostaDTO toProdutoGenericoRespostaDTO(ProdutoEntity produtoEntity) {
        return new ProdutoRespostaDTO(
                produtoEntity.getId(),
                produtoEntity.getModelo(),
                produtoEntity.getMarca(),
                produtoEntity.getUrlFrabricante(),
                produtoEntity.getImagens().stream().map(ArquivoMetadadosEntity::getBlobUrl).toList(),
                produtoEntity.getDescricao(),
                produtoEntity.getQtdEstoque(),
                produtoEntity.getCategoriaEntity() != null ?
                        CategoriaEntityMapper.toCategoriaRespostaDTO(produtoEntity.getCategoriaEntity()) : null,
                produtoEntity.isAtivo()
        );
    }

    public static ProdutoRespostaDTO toProdutoGenericoRespostaDTO(Produto produto) {
        return new ProdutoRespostaDTO(
                produto.getId(),
                produto.getModelo(),
                produto.getMarca(),
                produto.getUrlFabricante(),
                produto.getImagens().stream().map(ArquivoMetadado::getBlobUrl).toList(),
                produto.getDescricao(),
                produto.getQtdEstoque(),
                produto.getCategoria() != null ?
                        CategoriaEntityMapper.toCategoriaRespostaDTO(produto.getCategoria()) : null,
                produto.isAtivo()
        );
    }

    public static ProdutoEntity toProdutoEntity(Produto produtoDomain) {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setModelo(produtoDomain.getModelo());
        produtoEntity.setMarca(produtoDomain.getMarca());
        produtoEntity.setUrlFrabricante(produtoDomain.getUrlFabricante());
        produtoEntity.setDescricao(produtoDomain.getDescricao());
        produtoEntity.setQtdEstoque(produtoDomain.getQtdEstoque());
        CategoriaEntity categoriaEntity = CategoriaEntityMapper.toCategoria(produtoDomain.getCategoria().getNome());
        produtoEntity.setCategoriaEntity(categoriaEntity);
        return produtoEntity;
    }

    public static Produto toDomain(ProdutoEntity entity) {
        return new Produto(
                entity.getId(),
                entity.getModelo(),
                entity.getMarca(),
                entity.getUrlFrabricante(),
                entity.getImagens().stream().map(ArquivoMetadadosMapper::toDomain).toList(),
                entity.getDescricao(),
                entity.getQtdEstoque(),
                entity.isAtivo(),
                CategoriaMapper.toCategoria(
                        entity.getCategoriaEntity().getId(),
                        entity.getCategoriaEntity().getNome(),
                        entity.getCategoriaEntity().isAtivo(),
                        null)
        );
    }

    public static ProdutoPedidoRespostaDTO toProdutoPedidoRespostaDTO(ProdutoPedido produtoPedido){
        ProdutoPedidoRespostaDTO.ProdutoResumidoDTO produtoResumidoDTO = ProdutoPedidoRespostaDTO.ProdutoResumidoDTO.builder()
                .id(produtoPedido.getProdutoEntity().getId())
                .marca(produtoPedido.getProdutoEntity().getMarca())
                .urlFrabricante(produtoPedido.getProdutoEntity().getUrlFrabricante())
                .qtdDisponivel(produtoPedido.getProdutoEntity().getQtdEstoque())
                .modelo(produtoPedido.getProdutoEntity().getModelo())
                .imagens(produtoPedido.getProdutoEntity().getImagens().stream().map(ArquivoMetadadosEntity::getBlobUrl).toList())
                .build();
        ProdutoPedidoRespostaDTO produtoPedidoRespostaDTO = new ProdutoPedidoRespostaDTO();
        produtoPedidoRespostaDTO.setProduto(produtoResumidoDTO);
        produtoPedidoRespostaDTO.setQtdAlugada(produtoPedido.getQtd());
        return produtoPedidoRespostaDTO;

    }

    public static void atualizarProduto(ProdutoEntity produtoEntity, Produto produto, CategoriaEntity categoriaEntity) {
        produtoEntity.setModelo(produto.getModelo());
        produtoEntity.setMarca(produto.getMarca());
        produtoEntity.setUrlFrabricante(produto.getUrlFabricante());
        produtoEntity.setAtivo(produto.isAtivo());

        produtoEntity.setDescricao(produto.getDescricao());
        produtoEntity.setQtdEstoque(produto.getQtdEstoque());
        produtoEntity.setCategoriaEntity(categoriaEntity);
    }
}
