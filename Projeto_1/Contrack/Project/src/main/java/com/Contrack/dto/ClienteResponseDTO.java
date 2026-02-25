package com.Contrack.dto;

import com.Contrack.model.Cliente;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteResponseDTO {
    @JsonProperty("id")
    @Id
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("cnpj")
    private String cnpj;

    @JsonProperty("email")
    private String email;

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
