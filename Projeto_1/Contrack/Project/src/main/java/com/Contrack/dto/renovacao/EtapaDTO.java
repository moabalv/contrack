package com.Contrack.dto.renovacao;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtapaDTO {

    @JsonProperty("nome")
    private String nome;

    @JsonProperty("status")
    private String status;

} 
