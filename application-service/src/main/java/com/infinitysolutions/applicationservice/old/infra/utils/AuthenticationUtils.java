package com.infinitysolutions.applicationservice.old.infra.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationUtils {
    private static final String ADMIN_SCOPE = "SCOPE_ROLE_ADMIN";
    private static final String FUNCIONARIO_SCOPE = "SCOPE_ROLE_FUNCIONARIO";
    private static final String USUARIO_PF_SCOPE = "SCOPE_ROLE_USUARIO_PF";
    private static final String USUARIO_PJ_SCOPE = "SCOPE_ROLE_USUARIO_PJ";


    public boolean isAuthenticated(Authentication auth){
        return auth != null && auth.isAuthenticated();
    }

    public boolean isAdmin(Authentication auth){
        if (!isAuthenticated(auth)) return false;

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(ADMIN_SCOPE));
    }
    public boolean isCustomer(Authentication auth) {
        if (!isAuthenticated(auth)) return false;

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(USUARIO_PF_SCOPE) || role.equals(USUARIO_PJ_SCOPE));
    }

    public boolean isAdminOrEmployee(Authentication auth) {
        if (!isAuthenticated(auth)) return false;

        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals(ADMIN_SCOPE) || role.equals(FUNCIONARIO_SCOPE));
    }

}
