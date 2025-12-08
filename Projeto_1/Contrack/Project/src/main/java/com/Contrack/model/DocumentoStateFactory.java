package com.Contrack.model;

import com.Contrack.enums.StatusDocumento;

public final class DocumentoStateFactory {

    private DocumentoStateFactory() {
    }

    public static DocumentoState from(StatusDocumento status) {
        if (status == null) {
            return new DocumentoDentroPrazoState();
        }
        return switch (status) {
            case DENTRO_DO_PRAZO -> new DocumentoDentroPrazoState();
            case PROXIMO_DO_VENCIMENTO -> new DocumentoProximoVencimentoState();
            case VENCIDO -> new DocumentoVencidoState();
        };
    }
}
