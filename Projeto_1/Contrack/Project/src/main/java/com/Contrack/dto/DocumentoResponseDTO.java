package com.Contrack.dto;

import com.Contrack.enums.Prioridade;
import com.Contrack.enums.StatusDocumento;
import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DocumentoResponseDTO {

    private final Long id;
    private final TipoDocumento tipoDocumento;
    private final Prioridade prioridade;
    private final BigDecimal valor;
    private final LocalDate dataAssinatura;
    @JsonProperty("prazo")
    private final LocalDate prazo;
    private final StatusDocumento status;
    private final Long clienteId;
    @JsonProperty("colaboradores")
    private final List<Long> colaboradoresIds;

    public DocumentoResponseDTO(Documento documento) {
        this.id = documento.getId();
        this.tipoDocumento = documento.getTipoDocumento();
        this.prioridade = documento.getPrioridade();
        this.valor = documento.getValor();
        this.dataAssinatura = documento.getDataAssinatura();
        this.prazo = documento.getDataVencimento();
        this.status = documento.getStatus();
        this.clienteId = documento.getCliente() != null ? documento.getCliente().getId() : null;
        this.colaboradoresIds = documento.getColaboradores() == null
                ? Collections.emptyList()
                : documento.getColaboradores()
                .stream()
                .map(Funcionario::getId)
                .collect(Collectors.toList());
    }
}
