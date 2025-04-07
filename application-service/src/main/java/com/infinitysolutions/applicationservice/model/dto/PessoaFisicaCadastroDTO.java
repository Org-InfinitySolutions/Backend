package com.infinitysolutions.applicationservice.model.dto;

import com.infinitysolutions.applicationservice.infra.validation.CpfValido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Informações para cadastro de pessoa física", allOf = {UsuarioCadastroDTO.class})
public class PessoaFisicaCadastroDTO extends UsuarioCadastroDTO {
    @NotBlank(message = "O CPF é obrigatório")
    @CpfValido
    @Pattern(regexp = "^(\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$", message = "Formato do CPF inválido")
    @Schema(description = "CPF do usuário (apenas números ou com pontuação)", example = "123.456.789-00")
    private String cpf;
    
    @NotBlank(message = "O RG é obrigatório")
    @Pattern(regexp = "^[0-9]{1,2}(\\.[0-9]{3}){2}-[0-9xX]$|^[0-9]{8,9}$", 
            message = "Formato de RG inválido. Use XX.XXX.XXX-X ou XXXXXXXX")
    @Schema(description = "RG do usuário", example = "12.345.678-9")
    private String rg;
}

