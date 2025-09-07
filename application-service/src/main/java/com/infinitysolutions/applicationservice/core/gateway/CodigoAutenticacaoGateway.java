package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

import java.util.Map;

public interface CodigoAutenticacaoGateway {

    String gerarCodigo(Email email);

    Map.Entry<Boolean, String> validarCodigoAutenticacao(Email email, String codigo);
}
