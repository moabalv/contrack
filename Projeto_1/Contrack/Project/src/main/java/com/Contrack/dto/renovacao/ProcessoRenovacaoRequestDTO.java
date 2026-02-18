package com.Contrack.dto.renovacao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class ProcessoRenovacaoRequestDTO {

    
    @NotNull
    @Schema(example = "1", description = "ID do contrato/documento a ser renovado", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long documentoId;
    @NotNull

    @Schema(example = "1", description = "ID do modelo de fluxograma a ser seguido", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long fluxogramaId;

    @Schema(example = "10", description = "ID do funcionário responsável pelo processo. Ainda não implementado")
    private Long responsavelPrincipalId;

    @Schema(example = "2026-02-18", description = "Data de início (Padrão: data atual)")
    private LocalDate dataInicio;
}
