package com.infinitysolutions.authservice.infra.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${server.id}")
    private String serverId;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Autenticação - Novalocações")
                        .description(
                                       " Esta API gerencia autenticação e autorização de usuários para o sistema NovaLocações." +
                                               "\nInclui informações dos endpoints e exemplos."

                        )
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
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .servers(getServers());

    }

    private List<Server> getServers() {
        Server localServer = new Server()
                .url("http://" + serverId + ":8081")
                .description("Servidor de desenvolvimento local");
        Server gatewayServer = new Server()
                .url("http://" + serverId + ":8080/")
                .description("Servidor de desenvolvimento do gateway");

        return Arrays.asList(gatewayServer, localServer);
    }
}