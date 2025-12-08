package com.Contrack.model;

import com.Contrack.enums.StatusDocumento;

import java.time.LocalDate;

public interface DocumentoState {

    long ALERTA_DIAS = 7L;

    StatusDocumento getStatus();

    DocumentoState transicionar(Documento documento, LocalDate referencia);
}
