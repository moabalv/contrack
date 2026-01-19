package com.Contrack.dto.Notificacao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificacaoGetRequestDTO {

    private Long id;
    private String titulo;
    private Boolean lido;
    private String data;
}
