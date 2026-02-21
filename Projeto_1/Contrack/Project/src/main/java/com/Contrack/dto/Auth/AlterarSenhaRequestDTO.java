package com.Contrack.dto.Auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlterarSenhaRequestDTO {

    @NotBlank
    @JsonProperty("senha_atual")
    private String senhaAtual;

    @NotBlank
    @Size(min = 6)
    @JsonProperty("nova_senha")
    private String novaSenha;
}
