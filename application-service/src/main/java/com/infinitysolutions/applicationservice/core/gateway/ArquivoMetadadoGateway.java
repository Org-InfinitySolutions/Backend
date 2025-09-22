package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface ArquivoMetadadoGateway {
    List<ArquivoMetadado> findAllByUsuarioId(UUID id);
    ArquivoMetadado enviarArquivoUsuario(MultipartFile documento, TipoAnexo tipoAnexo, UUID idUsuario);
    ArquivoMetadado enviarArquivoProduto(MultipartFile documento, TipoAnexo tipoAnexo, Integer produtoId);
    ArquivoMetadado enviarDocumentoAuxiliar(MultipartFile documento, Pedido pedido);
    List<ArquivoMetadado> findAllByProdutoId(Integer produtoId);
    void deleteAllByProdutoId(Integer produtoId);
}
