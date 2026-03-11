package com.Contrack.dto.renovacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoRenovacaoDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("indiceEtapaAtual")
    private Integer indiceEtapaAtual;

    @JsonProperty("fluxograma")
    private FluxogramaDTO fluxograma;

    @JsonProperty("etapas")
    private List<EtapaDTO> etapas;

}