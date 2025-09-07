package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public interface EmailGateway {
    void enviarEmailHtmlAsync(Email email, String s, String conteudoEmail);
}
