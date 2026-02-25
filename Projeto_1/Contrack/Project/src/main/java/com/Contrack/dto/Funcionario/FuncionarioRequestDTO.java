package com.Contrack.dto.Funcionario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionarioRequestDTO {

    @NotBlank
    @JsonProperty("nome")
    private String nome;

    @NotBlank
    @CPF
    @JsonProperty("cpf")
    private String cpf;

    @NotBlank
    @Email
    @JsonProperty("email")
    private String email;

    @NotBlank
    @JsonProperty("senha")
    private String senha;

    @JsonProperty("role")
    private String role;
}
