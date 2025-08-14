package com.infinitysolutions.applicationservice.mapper.produto;

import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoPedidoRespostaDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.ProdutoRespostaDTO;
import com.infinitysolutions.applicationservice.model.produto.Categoria;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;

public class ProdutoMapper {
    
    public static ProdutoRespostaDTO toProdutoGenericoRespostaDTO(Produto produto) {
        return new ProdutoRespostaDTO(
                produto.getId(), 
                produto.getModelo(), 
                produto.getMarca(),
                produto.getUrlFrabricante(),
                produto.getImagens().stream().map(ArquivoMetadados::getBlobUrl).toList(),
                produto.getDescricao(),
                produto.getQtdEstoque(),
                produto.getCategoria() != null ?
                        CategoriaMapper.toCategoriaRespostaDTO(produto.getCategoria()) : null,
                produto.isAtivo()
        );
    }

    public static Produto toProduto(ProdutoCriacaoDTO dto, Categoria categoria) {
        Produto produto = new Produto();
        produto.setModelo(dto.getModelo());
        produto.setMarca(dto.getMarca());
        produto.setUrlFrabricante(dto.getUrlFrabricante());
        produto.setDescricao(dto.getDescricao());
        produto.setQtdEstoque(dto.getQtdEstoque());
        produto.setCategoria(categoria);
        return produto;
    }

    public static ProdutoPedidoRespostaDTO toProdutoPedidoRespostaDTO(ProdutoPedido produtoPedido){
        ProdutoPedidoRespostaDTO.ProdutoResumidoDTO produtoResumidoDTO = ProdutoPedidoRespostaDTO.ProdutoResumidoDTO.builder()
                .id(produtoPedido.getProduto().getId())
                .marca(produtoPedido.getProduto().getMarca())
                .urlFrabricante(produtoPedido.getProduto().getUrlFrabricante())
                .qtdDisponivel(produtoPedido.getProduto().getQtdEstoque())
                .modelo(produtoPedido.getProduto().getModelo())
                .imagens(produtoPedido.getProduto().getImagens().stream().map(ArquivoMetadados::getBlobUrl).toList())
                .build();
        ProdutoPedidoRespostaDTO produtoPedidoRespostaDTO = new ProdutoPedidoRespostaDTO();
        produtoPedidoRespostaDTO.setProduto(produtoResumidoDTO);
        produtoPedidoRespostaDTO.setQtdAlugada(produtoPedido.getQtd());
        return produtoPedidoRespostaDTO;

    }

    public static void atualizarProduto(Produto produto, ProdutoAtualizacaoDTO dto, Categoria categoria) {
        produto.setModelo(dto.getModelo());
        produto.setMarca(dto.getMarca());
        produto.setUrlFrabricante(dto.getUrlFrabricante());
        produto.setAtivo(dto.isAtivo());

        produto.setDescricao(dto.getDescricao());
        produto.setQtdEstoque(dto.getQtdEstoque());
        produto.setCategoria(categoria);
    }
}
