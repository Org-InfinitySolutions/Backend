package com.infinitysolutions.applicationservice.infra.configuration;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Documentação da API da Application service")
                        .description("Esta é a documentação detalhada da API para o backend do projeto de PI." +
                                "\nInclui informações dos endpoints e exemplos.")
                        .version("0.0.1")
                        .contact(new Contact()
                                .name("FilipeGuerreiro")
                                .email("filipe.guerreiro@sptech.school")
                        ))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira um token JWT válido para autenticar")
                        ))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }

    @Bean
    public ModelResolver modelResolver(org.springframework.context.ApplicationContext applicationContext) {
        return new ModelResolver(applicationContext.getBean(com.fasterxml.jackson.databind.ObjectMapper.class));
    }
}
