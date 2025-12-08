package com.Contrack.model;

import com.Contrack.enums.StatusDocumento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DocumentoVencidoState implements DocumentoState {

    @Override
    public StatusDocumento getStatus() {
        return StatusDocumento.VENCIDO;
    }

    @Override
    public DocumentoState transicionar(Documento documento, LocalDate referencia) {
        LocalDate vencimento = documento.getDataVencimento();
        if (vencimento == null) {
            return new DocumentoDentroPrazoState();
        }

        if (vencimento.isBefore(referencia)) {
            return this;
        }

        long diasRestantes = ChronoUnit.DAYS.between(referencia, vencimento);
        if (diasRestantes <= ALERTA_DIAS) {
            return new DocumentoProximoVencimentoState();
        }
        return new DocumentoDentroPrazoState();
    }
}
