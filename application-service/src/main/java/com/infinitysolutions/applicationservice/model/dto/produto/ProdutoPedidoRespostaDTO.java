package com.infinitysolutions.applicationservice.model.dto.produto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProdutoPedidoRespostaDTO {
        private Integer qtdAlugada;
        private ProdutoResumidoDTO produto;

        @Data
        @Builder
        public static class ProdutoResumidoDTO {
            private Integer id;

            private String modelo;

            private String marca;

            private String urlFrabricante;

            private Integer qtdDisponivel;

            private List<String> imagens;
        }
}
