package com.infinitysolutions.applicationservice.infrastructure.config.rabbit;

public record MensagemRelatorioDTO(    String email,
                                       String tituloRelatorio,
                                       RelatorioDTO relatorio) {

}
