package com.Contrack.model;

import com.Contrack.enums.StatusDocumento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DocumentoDentroPrazoState implements DocumentoState {

    @Override
    public StatusDocumento getStatus() {
        return StatusDocumento.DENTRO_DO_PRAZO;
    }

    @Override
    public DocumentoState transicionar(Documento documento, LocalDate referencia) {
        LocalDate vencimento = documento.getDataVencimento();
        if (vencimento == null) {
            return this;
        }
        if (vencimento.isBefore(referencia)) {
            return new DocumentoVencidoState();
        }
        long diasRestantes = ChronoUnit.DAYS.between(referencia, vencimento);
        if (diasRestantes <= ALERTA_DIAS) {
            return new DocumentoProximoVencimentoState();
        }
        return this;
    }
}
