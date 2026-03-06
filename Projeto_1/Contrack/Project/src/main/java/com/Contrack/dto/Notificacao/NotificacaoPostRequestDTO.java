package com.Contrack.dto.Notificacao;


import java.time.LocalDateTime;

import com.Contrack.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class NotificacaoPostRequestDTO {

    @JsonProperty("documentoId")
    private Long documentoId;

    @JsonProperty("setorId")
    private Long setorId;

    @JsonProperty("titulo")
    private String titulo;

    @JsonProperty("descricao")
    private String descricao;

    @JsonProperty("status")
    private Status status;

    @JsonProperty("lido")
    private boolean lido;

    @JsonProperty("data")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime data;
}
