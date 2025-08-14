package com.infinitysolutions.authservice.infra.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyAuthInterceptor apiKeyAuthInterceptor;    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyAuthInterceptor)
                .addPathPatterns("/auth/cadastrar", "/auth/credenciais/*/email")
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**", "/actuator/**");
    }

}
