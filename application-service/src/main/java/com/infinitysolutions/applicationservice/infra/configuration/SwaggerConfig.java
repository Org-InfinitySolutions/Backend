package com.infinitysolutions.applicationservice.infra.configuration;

import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
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
//                                .url("https://www.empresa.com")
                        ));
//                        .license(new License()
//                                .name("Licença MIT")
//                                .url("https://opensource.org/licenses/MIT")));
    }

    @Bean
    public ModelResolver modelResolver(org.springframework.context.ApplicationContext applicationContext) {
        return new ModelResolver(applicationContext.getBean(com.fasterxml.jackson.databind.ObjectMapper.class));
    }
}
