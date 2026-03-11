package com.Contrack.dto.Funcionario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FuncionarioUpdateNomeDTO {

    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;
}