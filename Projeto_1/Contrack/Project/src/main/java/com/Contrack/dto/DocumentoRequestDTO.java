package com.Contrack.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentoRequestDTO {

    @JsonProperty("documentoId")
    @JsonAlias({"documento_id", "id"})
    private Long documentoId;

    @NotBlank
    @JsonProperty("tipoDocumento")
    private String tipoDocumento;

    @NotBlank
    @JsonProperty("prioridade")
    private String prioridade;

    @NotBlank
    @JsonProperty("valor")
    private String valor;

    @NotNull
    @JsonAlias({"cliente", "clienteId"})
    private Long clienteId;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("dataAssinatura")
    private LocalDate dataAssinatura;

    @NotNull
    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonProperty("prazo")
    private LocalDate prazo;

    @JsonProperty("colaboradores")
    private List<Long> colaboradoresIds;
}
