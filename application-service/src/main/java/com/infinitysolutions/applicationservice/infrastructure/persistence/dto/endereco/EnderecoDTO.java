package com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnderecoDTO {
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^[0-9]{5}-?[0-9]{3}$", message = "CEP inválido")
    @Schema(description = "CEP do endereço", example = "01001-000")
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 255)
    @Schema(description = "Logradouro do endereço", example = "Praça da Sé")
    private String logradouro;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 255)
    @Schema(description = "Bairro do endereço", example = "Sé")
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 255)
    @Schema(description = "Cidade do endereço", example = "São Paulo")
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2)
    @Schema(description = "Sigla do estado", example = "SP")
    private String estado;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20)
    @Schema(description = "Número do endereço", example = "100")
    private String numero;

    @Size(max = 255)
    @Schema(description = "Complemento do endereço (opcional)", example = "Apto 101")
    private String complemento;

    public EnderecoDTO(String cep, String logradouro, String bairro, String cidade, String estado, String numero, String complemento) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.complemento = complemento;
    }
}
