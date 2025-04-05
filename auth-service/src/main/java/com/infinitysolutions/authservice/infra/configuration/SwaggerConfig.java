package com.infinitysolutions.authservice.infra.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Autenticação - Novalocações")
                        .description(
                                """
                                        Esta API gerencia autenticação e autorização de usuários para o sistema NovaLocações.
                                        ## Recursos Principais
                                        * Cadastro de credenciais de usuário
                                        * Autenticação via JWT
                                        * Gerenciamento de perfis de acesso
                                        ## Fluxos de Autenticação
                                        1. Registre as credenciais de um usuário através de `/api/auth/cadastrar`
                                        2. Faça login para receber um token JWT
                                        3. Use o token nas requisições subsequentes"""
                        )
                        .version("0.0.1")
                        .contact(new Contact()
                                        .name("FilipeGuerreiro")
                                        .email("filipe.guerreiro@sptech.school")
//                                .url("https://www.empresa.com")
                        ))
                .addServersItem(new Server().url("/").description("Servidor atual"))
                .addSecurityItem(new SecurityRequirement().addList("JWT Auth"))
                .components(new Components()
                        .addSecuritySchemes("JWT Auth", new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Insira o token JWT com o prefixo Bearer. Ex: 'Bearer abcdef123456'")
                        )
                )
                .addTagsItem(new Tag().name("Autenticação").description("Operações relacionadas à autenticação e gerenciamento de credenciais"))
                .addTagsItem(new Tag().name("Usuários").description("Operações relacionadas a contas de usuário"));

    }
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth-api")
                .pathsToMatch("/api/auth/**")
                .displayName("API de Autenticação")
                .build();
    }

    @Bean
    public GroupedOpenApi actuatorApi() {
        return GroupedOpenApi.builder()
                .group("actuator")
                .pathsToMatch("/actuator/**")
                .displayName("Spring Actuator")
                .build();
    }
}