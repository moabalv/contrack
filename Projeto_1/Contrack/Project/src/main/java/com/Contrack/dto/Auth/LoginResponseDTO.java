package com.Contrack.dto.Auth;

import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    @JsonProperty("mensagem")
    private String mensagem;

    @JsonProperty("usuario")
    private FuncionarioResponseDTO usuario;
}
