package com.Contrack.service;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;

import java.util.List;

public interface NotificacaoService {

    List<NotificacaoGetRequestDTO> listarNotificacoes();

    NotificacaoGetResponseDTO obterNotificacaoPorId(Long id);


}
