package com.Contrack.dto.Notificacao;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.Contrack.dto.Documento.DocumentoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacaoGetResponseDTO {

    private Long id;
    private String titulo;
    private String data;

    private DocumentoResponseDTO documento;
}
