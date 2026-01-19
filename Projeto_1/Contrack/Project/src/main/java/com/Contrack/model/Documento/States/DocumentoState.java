package com.Contrack.model.Documento.States;

import com.Contrack.enums.StatusDocumento;
import com.Contrack.model.Documento.Documento;

import java.time.LocalDate;

public interface DocumentoState {

    long ALERTA_DIAS = 7L;

    StatusDocumento getStatus();

    DocumentoState transicionar(Documento documento, LocalDate referencia);
}
