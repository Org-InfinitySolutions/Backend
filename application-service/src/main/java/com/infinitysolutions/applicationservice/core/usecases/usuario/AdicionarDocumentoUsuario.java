package com.infinitysolutions.applicationservice.core.usecases.usuario;

import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public class AdicionarDocumentoUsuario {

    private final ArquivoMetadadoGateway arquivoMetadadoGateway;

    public AdicionarDocumentoUsuario(ArquivoMetadadoGateway arquivoMetadadoGateway
    ) {
        this.arquivoMetadadoGateway = arquivoMetadadoGateway;
    }

    public void execute(MultipartFile documento, TipoAnexo tipoAnexo, UUID idUsuario) {
        arquivoMetadadoGateway.enviarArquivoUsuario(documento, tipoAnexo, idUsuario);
    }
}
