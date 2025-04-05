package com.infinitysolutions.applicationservice.model.dto;

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
    private String cep;

    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 255)
    private String logradouro;

    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 255)
    private String bairro;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 255)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2)
    private String estado;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 20)
    private String numero;

    @Size(max = 255)
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
