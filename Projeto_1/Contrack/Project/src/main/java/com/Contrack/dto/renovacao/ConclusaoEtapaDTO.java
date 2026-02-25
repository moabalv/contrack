package com.Contrack.dto.renovacao;

import lombok.Data;

@Data
public class ConclusaoEtapaDTO {
    /**
     * Caso true, força marcar a etapa como atrasada mesmo que dentro do prazo.
     * Útil quando houve pendência documental ou taxa.
     */
    private Boolean atrasada = Boolean.FALSE;
}
