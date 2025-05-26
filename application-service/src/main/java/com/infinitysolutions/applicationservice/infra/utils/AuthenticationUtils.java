package com.infinitysolutions.applicationservice.infra.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationUtils {

    private static final String ADMIN_SCOPE = "SCOPE_ROLE_ADMIN";
    private static final String USUARIO_PF_SCOPE = "SCOPE_ROLE_USUARIO_PF";

    public boolean isAuthenticated(Authentication auth){
        return auth != null && auth.isAuthenticated();
    }

    public boolean isAdmin(Authentication auth){
        if (!isAuthenticated(auth)) return false;

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(ADMIN_SCOPE));
    }

}
