package com.infinitysolutions.applicationservice.model.dto.produto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

            private byte[] imagem;
        }
}
