package com.infinitysolutions.applicationservice.infrastructure.gateway;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.ProjectConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProjectConfigProviderImpl implements ProjectConfigProvider {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${jwt.expiracao-segundos}")
    private long TEMPO_EXPIRACAO_SEGUNDOS;

    @Override
    public Email getAdminEmail() {
        return Email.of(adminEmail);
    }

    @Override
    public long getTempoExpiracaoJwt() {
        return TEMPO_EXPIRACAO_SEGUNDOS;
    }
}
