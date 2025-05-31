package com.infinitysolutions.gateway.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class FiltroAgregacaoUsuario extends AbstractGatewayFilterFactory<Object> {


    private final WebClient.Builder webClientBuilder;

    @Autowired
    public FiltroAgregacaoUsuario(WebClient.Builder webClientBuilder) {
        super(Object.class); // Use Object.class se não precisar de configuração específica na rota
        this.webClientBuilder = webClientBuilder;
    }    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            // 1. Obter o userId do path da requisição original do front-end
            String path = exchange.getRequest().getURI().getPath();
            String userId = path.substring("/api/usuarios/".length()).split("/")[0];
            log.info("Id do usuário: {}", userId);            // Obter o token de autorização da requisição original (se existir)
            String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
            log.debug("Authorization header: {}", authorizationHeader);
            
            // 2. Fazer a chamada para o application-service
            String applicationServiceUri = "http://localhost:8082/api/usuarios/" + userId;
            log.debug("Chamando application-service em: {}", applicationServiceUri);
            
            WebClient.RequestHeadersSpec<?> profileRequest = webClientBuilder.build()
                    .get()
                    .uri(applicationServiceUri);
            
            // Adicionar o token de autorização se existir
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                profileRequest = profileRequest.header("Authorization", authorizationHeader);
            }
            
            Mono<JsonNode> profileInfoMono = profileRequest
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .doOnSuccess(response -> log.debug("Resposta do application-service: {}", response))
                    .doOnError(error -> log.error("Erro ao chamar application-service: {}", error.getMessage(), error));

            // 3. Fazer a chamada para o auth-service (para o endpoint que retorna apenas o email)
            String authServiceUri = "http://localhost:8081/auth/credenciais/" + userId + "/email";
            log.debug("Chamando auth-service em: {}", authServiceUri);
            
            WebClient.RequestHeadersSpec<?> emailRequest = webClientBuilder.build()
                    .get()
                    .uri(authServiceUri);
            
            // Adicionar o token de autorização se existir
            if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
                emailRequest = emailRequest.header("Authorization", authorizationHeader);
            }
            
            Mono<JsonNode> emailInfoMono = emailRequest
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .doOnSuccess(response -> log.debug("Resposta do auth-service: {}", response))
                    .doOnError(error -> log.error("Erro ao chamar auth-service: {}", error.getMessage(), error));// 4. Combinar os resultados das duas chamadas de forma reativa (em paralelo)
            // Usamos Mono.zip para combinar os resultados de dois Monos
            log.info("Combinando resultados dos serviços para o userId: {}", userId);
            Mono<JsonNode> aggregatedResponseMono = Mono.zip(profileInfoMono, emailInfoMono)
                    .map(tuple -> {
                        JsonNode profileNode = tuple.getT1(); // Resultado do profileInfoMono
                        JsonNode emailNode = tuple.getT2();   // Resultado do emailInfoMono
                        log.debug("Dados do perfil: {}", profileNode);
                        log.debug("Dados do email: {}", emailNode);
                        
                        // Verificar se os nós JSON são válidos
                        if (profileNode == null) {
                            throw new RuntimeException("Dados do perfil vieram nulos do application-service");
                        }

                        // Criar um novo objeto JSON combinando os dados
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        ObjectNode resultNode = mapper.createObjectNode();
                        
                        // Copiar todos os campos do perfil
                        if (profileNode.isObject()) {
                            profileNode.fields().forEachRemaining(entry -> 
                                resultNode.set(entry.getKey(), entry.getValue())
                            );
                        }
                        
                        // Adiciona o campo email do segundo resultado
                        if (emailNode != null && emailNode.has("email")) {
                            resultNode.set("email", emailNode.get("email"));
                        }
                        
                        log.debug("Resposta combinada: {}", resultNode);
                        return (JsonNode) resultNode;
                    })
                    // Melhorando o tratamento de erros
                    .onErrorResume(e -> {
                        log.error("Erro ao agregar perfil do usuário: {}", e.getMessage(), e);
                        
                        // Criando uma resposta de erro detalhada
                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                        ObjectNode errorNode = mapper.createObjectNode();
                        errorNode.put("error", "Erro ao processar perfil completo");
                        errorNode.put("message", e.getMessage());
                        errorNode.put("timestamp", java.time.LocalDateTime.now().toString());
                        
                        exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
                        
                        // Retornar uma mensagem de erro mais detalhada em vez de Mono.empty()
                        return Mono.just(errorNode);
                    });

            // 5. Escrever a resposta agregada de volta para o cliente
            return aggregatedResponseMono.flatMap(jsonNode ->
                    exchange.getResponse().writeWith(
                            Mono.just(exchange.getResponse().bufferFactory().wrap(jsonNode.toString().getBytes()))
                    )
            )
                    // Importante: Não chame chain.filter(exchange) aqui, pois este filtro está produzindo a resposta final.
                    // chain.filter(exchange) é usado para passar a requisição para o próximo filtro na cadeia.
                    // Se você quiser que outros filtros (como de autenticação/autorização) rodem ANTES deste,
                    // configure a ordem dos filtros.
                    ;
        };
    }
}
