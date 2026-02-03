package com.Contrack.dto.renovacao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class FluxogramaRequestDTO {
    @NotBlank
    private String nome;
    private String descricao;
    @NotEmpty
    @Valid
    private List<EtapaModeloDTO> etapas;
}
