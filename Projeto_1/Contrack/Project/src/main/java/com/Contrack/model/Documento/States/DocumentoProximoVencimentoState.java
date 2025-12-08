package com.Contrack.model.Documento.States;

import com.Contrack.enums.StatusDocumento;
import com.Contrack.model.Documento.Documento;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DocumentoProximoVencimentoState implements DocumentoState {

    @Override
    public StatusDocumento getStatus() {
        return StatusDocumento.PROXIMO_DO_VENCIMENTO;
    }

    @Override
    public DocumentoState transicionar(Documento documento, LocalDate referencia) {
        LocalDate vencimento = documento.getDataVencimento();
        if (vencimento == null) {
            return new DocumentoDentroPrazoState();
        }
        if (vencimento.isBefore(referencia)) {
            return new DocumentoVencidoState();
        }
        long diasRestantes = ChronoUnit.DAYS.between(referencia, vencimento);
        if (diasRestantes > ALERTA_DIAS) {
            return new DocumentoDentroPrazoState();
        }
        return this;
    }
}
