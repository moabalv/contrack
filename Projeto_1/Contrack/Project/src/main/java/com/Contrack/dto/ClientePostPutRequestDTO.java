package com.Contrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class ClientePostPutRequestDTO {
    @JsonProperty("id")
    @Id
    private Long id;

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("email")
    private String email;

    @JsonProperty("telefone")
    private String telefone;

    @JsonProperty("cnpj")
    private String cnpj;

}
