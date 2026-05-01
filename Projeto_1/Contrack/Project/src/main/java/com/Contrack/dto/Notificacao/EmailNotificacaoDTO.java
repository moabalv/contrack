package com.Contrack.dto.Notificacao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailNotificacaoDTO {
    
    private String titulo;
    private String nomeColaborador;
    private String nomeCliente;
    private String textoTempo;
    private Long idDocumento;
}
