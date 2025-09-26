package com.infinitysolutions.applicationservice.core.usecases.produto;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import org.springframework.web.multipart.MultipartFile;


public class AtualizarImagemProduto {

    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public AtualizarImagemProduto(ArquivoMetadadoGateway arquivoMetadadoGateway) {
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }

    public ArquivoMetadado execute(MultipartFile novaImagem, Integer produtoId) {
        if (novaImagem == null || novaImagem.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de imagem é obrigatório");
        }

        System.out.printf("Iniciando atualização de imagem para produto ID: %s", produtoId);
        arquivoMetadadoGateway.deleteAllByProdutoId(produtoId);

        System.out.printf("Fazendo upload da nova imagem: %s", novaImagem.getOriginalFilename());
        return arquivoMetadadoGateway.enviarArquivoProduto(novaImagem, TipoAnexo.IMAGEM_PRODUTO, produtoId);
    }
}
