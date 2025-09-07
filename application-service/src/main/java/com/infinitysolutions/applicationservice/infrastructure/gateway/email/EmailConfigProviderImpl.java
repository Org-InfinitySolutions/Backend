package com.infinitysolutions.applicationservice.infrastructure.gateway.email;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.gateway.EmailConfigProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfigProviderImpl implements EmailConfigProvider {

    @Value("${admin.email}")
    private String adminEmail;

    @Override
    public Email getAdminEmail() {
        return Email.of(adminEmail);
    }
}
