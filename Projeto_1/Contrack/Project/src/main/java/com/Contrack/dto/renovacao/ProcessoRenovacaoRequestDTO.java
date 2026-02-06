package com.Contrack.dto.renovacao;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProcessoRenovacaoRequestDTO {
    @NotNull
    private Long contratoId;
    @NotNull
    private Long fluxogramaId;
    
    private Long responsavelPrincipalId;
    private LocalDate dataInicio;
}
