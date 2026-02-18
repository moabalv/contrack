package com.Contrack.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
@Schema(description = "Dados para criação ou atualização de um cliente")
public class ClientePostPutRequestDTO {

    @Schema(example = "Empresa de Logística S.A.", description = "Nome completo ou Razão Social")
    @JsonProperty("nome")
    private String nome;

    @Schema(example = "65.603.780/0001-00", description = "CNPJ apenas números")
    @JsonProperty("cnpj")
    private String cnpj;

    @Schema(example = "contato@empresa.com", description = "E-mail de contato principal")
    @JsonProperty("email")
    private String email;

    @Schema(example = "11999998888", description = "Telefone com DDD")
    @JsonProperty("telefone")
    private String telefone;

}
