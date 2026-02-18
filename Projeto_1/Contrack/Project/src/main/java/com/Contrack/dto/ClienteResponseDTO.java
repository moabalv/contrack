package com.Contrack.dto;

import com.Contrack.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Objeto de resposta de cliente")
public class ClienteResponseDTO {

    @Schema(example = "1")
    @JsonProperty("id")
    @Id
    private Long id;

    @Schema(example = "Empresa de Logística S.A.")
    @JsonProperty("nome")
    private String nome;

    @Schema(example = "Empresa de Logística S.A.")
    @JsonProperty("cnpj")
    private String cnpj;

    @Schema(example = "contato@empresa.com")
    @JsonProperty("email")
    private String email;

    @Schema(example = "11999998888")
    @JsonProperty("telefone")
    private String telefone;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cnpj = cliente.getCnpj();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
    }
}
