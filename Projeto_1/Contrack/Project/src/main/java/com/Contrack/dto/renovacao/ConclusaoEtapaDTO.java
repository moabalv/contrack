package com.Contrack.dto.renovacao;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Payload para conclusão de uma etapa")
public class ConclusaoEtapaDTO {
    /**
     * Caso true, força marcar a etapa como atrasada mesmo que dentro do prazo.
     * Útil quando houve pendência documental ou taxa.
     */
    @Schema(example = "false", description = "Se verdadeiro, a etapa será marcada como ATRASADA em vez de CONCLUIDA.")
    private Boolean atrasada = Boolean.FALSE;
}
