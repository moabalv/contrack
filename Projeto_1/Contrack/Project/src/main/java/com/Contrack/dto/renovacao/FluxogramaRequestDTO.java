package com.Contrack.dto.renovacao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Dados para criação de um novo processo de fluxograma")
public class FluxogramaRequestDTO {

    @NotBlank
    @Schema(example = "Renovação de Contrato Mensal", description = "Nome identificador do fluxo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(example = "Passo a passo para renovar contratos de prestação de serviço", description = "Breve explicação do objetivo deste fluxo")
    private String descricao;

    @NotEmpty
    @Schema(
        example = "[\"Análise de documentos\", \"Aprovação jurídica\", \"Assinatura digital\"]", 
        description = "Lista ordenada das etapas do processo",
        requiredMode = Schema.RequiredMode.REQUIRED
    )
    private List<@NotBlank String> etapas;
}
