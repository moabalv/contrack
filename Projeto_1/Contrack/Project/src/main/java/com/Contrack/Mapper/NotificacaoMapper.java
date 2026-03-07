package com.Contrack.Mapper;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.model.Notificacao;

// Classe que mapeia manualmente Notificacao para NotificacaoDTO.
@Component
public class NotificacaoMapper {

    @Autowired
    DocumentoMapper documentoMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    //
    public NotificacaoGetResponseDTO toResponseDTO(Notificacao notificacao) {
        if (notificacao == null) {
            return null;
        }

        return NotificacaoGetResponseDTO.builder()
                .id(notificacao.getId())
                .titulo(notificacao.getTitulo())
                .data(notificacao.getData().format(DATE_FORMATTER))
                .documento(documentoMapper.docToResponseDTO(notificacao.getDocumento()))
                .build();
    }

    public NotificacaoGetRequestDTO toRequestDTO(Notificacao notificacao) {
        if (notificacao == null) {
            return null;
        }

        return NotificacaoGetRequestDTO.builder()
                .id(notificacao.getId())
                .titulo(notificacao.getTitulo())
                .descricao(notificacao.getDescricao())
                .lido(notificacao.isLido())
                .data(notificacao.getData().format(DATE_FORMATTER))
                .build();
    }
}
