package com.Contrack.dto.Dashboard;

import com.Contrack.model.Documento.Documento;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentoDashboardResponseDTO {
    public DocumentoDashboardResponseDTO(Documento documento) {
        this.id = documento.getId();
        this.tipo = documento.getTipoDocumento().toString();
        this.cliente = documento.getCliente().getNome();
        this.prazo = documento.getDataVencimento().toString();
    }

    @JsonProperty("id")
    Long id;

    @JsonProperty("tipo")
    String tipo;

    @JsonProperty("cliente")
    String cliente;

    @JsonProperty("prazo")
    String prazo;
}
