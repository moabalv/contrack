package com.Contrack.dto.Dashboard;

import com.Contrack.model.renovacao.Fluxograma;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FluxogramaDashboardResponseDTO {
    public FluxogramaDashboardResponseDTO(Fluxograma fluxograma) {
        this.nome = fluxograma.getNome();
        this.descricao = fluxograma.getDescricao();
    }
    
    @JsonProperty("nome")
    String nome;

    @JsonProperty("descricao")
    String descricao;
}
