package com.Contrack.dto.Notificacao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class NotificacaoGetRequestDTO {

    private Long id;
    private String titulo;
    private String descricao;
    private Boolean lido;
    private String data;
}
