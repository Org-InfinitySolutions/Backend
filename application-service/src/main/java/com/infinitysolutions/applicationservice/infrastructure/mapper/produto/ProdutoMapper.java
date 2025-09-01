package com.infinitysolutions.applicationservice.infrastructure.mapper.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoPedidoRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.Categoria;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedido;

public class ProdutoMapper {
    
    public static ProdutoRespostaDTO toProdutoGenericoRespostaDTO(ProdutoEntity produtoEntity) {
        return new ProdutoRespostaDTO(
                produtoEntity.getId(),
                produtoEntity.getModelo(),
                produtoEntity.getMarca(),
                produtoEntity.getUrlFrabricante(),
                produtoEntity.getImagens().stream().map(ArquivoMetadadosEntity::getBlobUrl).toList(),
                produtoEntity.getDescricao(),
                produtoEntity.getQtdEstoque(),
                produtoEntity.getCategoria() != null ?
                        CategoriaMapper.toCategoriaRespostaDTO(produtoEntity.getCategoria()) : null,
                produtoEntity.isAtivo()
        );
    }

    public static ProdutoEntity toProduto(ProdutoCriacaoDTO dto, Categoria categoria) {
        ProdutoEntity produtoEntity = new ProdutoEntity();
        produtoEntity.setModelo(dto.getModelo());
        produtoEntity.setMarca(dto.getMarca());
        produtoEntity.setUrlFrabricante(dto.getUrlFrabricante());
        produtoEntity.setDescricao(dto.getDescricao());
        produtoEntity.setQtdEstoque(dto.getQtdEstoque());
        produtoEntity.setCategoria(categoria);
        return produtoEntity;
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

    public static void atualizarProduto(ProdutoEntity produtoEntity, ProdutoAtualizacaoDTO dto, Categoria categoria) {
        produtoEntity.setModelo(dto.getModelo());
        produtoEntity.setMarca(dto.getMarca());
        produtoEntity.setUrlFrabricante(dto.getUrlFrabricante());
        produtoEntity.setAtivo(dto.isAtivo());

        produtoEntity.setDescricao(dto.getDescricao());
        produtoEntity.setQtdEstoque(dto.getQtdEstoque());
        produtoEntity.setCategoria(categoria);
    }
}
