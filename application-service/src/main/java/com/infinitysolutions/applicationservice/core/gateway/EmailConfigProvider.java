package com.infinitysolutions.applicationservice.core.gateway;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;

public interface EmailConfigProvider {
    Email getAdminEmail();
}
