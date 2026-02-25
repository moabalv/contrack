package com.Contrack.dto.Dashboard;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardResponseDTO {
    @JsonProperty("total_documentos")
    private int totalDocumentos;

    @JsonProperty("total_documentos_ativos")
    private int totalDocumentosAtivos;

    @JsonProperty("total_clientes")
    private int totalClientes;

    @JsonProperty("documentos_proximo_venc")
    List<DocumentoDashboardResponseDTO> documentos;

    @JsonProperty("fluxogramas")
    List<FluxogramaDashboardResponseDTO> fluxogramas;
}
