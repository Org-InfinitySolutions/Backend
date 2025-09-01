package com.infinitysolutions.applicationservice.infrastructure.gateway;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.port.ArquivoMetadadoGateway;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ArquivoMetadadoGatewayImpl implements ArquivoMetadadoGateway {
    @Override
    public List<ArquivoMetadado> findAllByUsuarioId(UUID id) {
        return List.of();
    }
}
