package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.mapper.ProdutoMapper;
import com.infinitysolutions.applicationservice.core.domain.produto.Categoria;
import com.infinitysolutions.applicationservice.core.domain.produto.Produto;
import com.infinitysolutions.applicationservice.core.gateway.ProdutoGateway;
import com.infinitysolutions.applicationservice.core.usecases.produto.categoria.BuscarCategoriaPorId;
import org.springframework.web.multipart.MultipartFile;

public class CriarProduto {

    private final BuscarCategoriaPorId buscarCategoriaPorId;
    private final AtualizarImagemProduto atualizarImagemProduto;
    private final ProdutoGateway produtoGateway;

    public CriarProduto(BuscarCategoriaPorId buscarCategoriaPorId,
                        AtualizarImagemProduto atualizarImagemProduto,
                        ProdutoGateway produtoGateway
    ) {
        this.buscarCategoriaPorId = buscarCategoriaPorId;
        this.atualizarImagemProduto = atualizarImagemProduto;
        this.produtoGateway = produtoGateway;
    }


    public Produto execute(CriarProdutoInput dto, MultipartFile imagem) {
        Categoria categoria = buscarCategoriaPorId.execute(dto.categoriaId());
        Produto novoProduto = ProdutoMapper.toDomain(dto, categoria);
        Produto produtoCriado = produtoGateway.save(novoProduto);

        if (imagem != null && !imagem.isEmpty()) {
            ArquivoMetadado imagemProduto = atualizarImagemProduto.execute(imagem, produtoCriado.getId());
            produtoCriado.adicionarImagem(imagemProduto);
        }
        return produtoCriado;
    }
}
