package com.Contrack.dto.Dashboard;

import com.Contrack.enums.Role;
import com.Contrack.model.Funcionario.Funcionario;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FuncionarioDashboardResponseDTO {
    public FuncionarioDashboardResponseDTO(Funcionario funcionario){
        this.nome = funcionario.getNome();
        this.cargo = funcionario.getRole();
        this.email = funcionario.getEmail();
    }
    
    @JsonProperty("nome")
    String nome;

    @JsonProperty("email")
    String email;

    @JsonProperty("cargo")
    Role cargo;
}
