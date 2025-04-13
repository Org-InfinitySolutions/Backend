package com.infinitysolutions.authservice.infra.configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiKeyAuthInterceptor implements HandlerInterceptor {

    @Value("${spring.security.apiKey}")
    private String authApiKeyEsperada;

    private static final String API_KEY_HEADER = "AUTH-API-KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authApiKey = request.getHeader(API_KEY_HEADER);

        if (authApiKey == null || !authApiKey.equals(authApiKeyEsperada)) {
            log.warn("Tentativa de acesso com API KEY inválida ou ausente para {}", request.getRequestURI());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acesso não autorizado, API KEY inválida ou ausente");
            return false;
        }
        return true;
    }
}
