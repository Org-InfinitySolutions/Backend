package com.infinitysolutions.applicationservice.mapper.produto;

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
                produto.getImagem(),
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
        produto.setImagem(dto.getImagem());
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
                .modelo(produtoPedido.getProduto().getModelo())
                .imagem(produtoPedido.getProduto().getImagem())
                .build();
        ProdutoPedidoRespostaDTO produtoPedidoRespostaDTO = new ProdutoPedidoRespostaDTO();
        produtoPedidoRespostaDTO.setProduto(produtoResumidoDTO);
        produtoPedidoRespostaDTO.setQtdAlugada(produtoPedido.getQtd());
        return produtoPedidoRespostaDTO;

    }

    public static void atualizarProduto(Produto produto, ProdutoCriacaoDTO dto, Categoria categoria) {
        produto.setModelo(dto.getModelo());
        produto.setMarca(dto.getMarca());
        produto.setUrlFrabricante(dto.getUrlFrabricante());
        
        if (dto.getImagem() != null) {
            produto.setImagem(dto.getImagem());
        }
        
        produto.setDescricao(dto.getDescricao());
        produto.setQtdEstoque(dto.getQtdEstoque());
        produto.setCategoria(categoria);
    }
}
