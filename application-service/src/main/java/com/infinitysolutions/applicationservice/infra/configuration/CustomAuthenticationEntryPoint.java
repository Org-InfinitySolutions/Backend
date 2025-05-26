package com.infinitysolutions.applicationservice.infra.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.infinitysolutions.applicationservice.infra.exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final BearerTokenAuthenticationEntryPoint delegate = new BearerTokenAuthenticationEntryPoint();
    private final ObjectMapper objectMapper;

    public CustomAuthenticationEntryPoint() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.debug("Tratando erro de autenticação: {}", authException.getMessage());

        // Verificar se é um erro de token expirado
        Throwable cause = authException.getCause();
        if (cause instanceof JwtValidationException jwtException) {
            for (var error : jwtException.getErrors()) {
                if (error.getDescription().contains("expired") ||
                    error.getDescription().contains("Jwt expired")) {
                    handleExpiredToken(response, request);
                    return;
                }
            }
        }
        handleGenericAuthError(response, request, authException);
    }

    private void handleExpiredToken(HttpServletResponse response, HttpServletRequest request) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_EXPIRADO")
                .message("O tempo de acesso expirou, por favor refaça o login.")
                .path(request.getRequestURI())
                .build();
        writeErrorResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private void handleGenericAuthError(HttpServletResponse response, HttpServletRequest request, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("ACESSO_NAO_AUTORIZADO")
                .message("Token de acesso inválido ou ausente.")
                .path(request.getRequestURI())
                .build();
        writeErrorResponse(response, errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
