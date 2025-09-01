package com.infinitysolutions.applicationservice.core.port;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;

import java.util.List;
import java.util.UUID;

public interface ArquivoMetadadoGateway {
    List<ArquivoMetadado> findAllByUsuarioId(UUID id);
}
