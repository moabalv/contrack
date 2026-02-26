package com.Contrack.service;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface NotificacaoService {

    List<NotificacaoGetRequestDTO> listarNotificacoes();

    NotificacaoGetResponseDTO obterNotificacaoPorId(Long id);

    NotificacaoGetResponseDTO marcarComoLida(Long id);

    NotificacaoPostResponseDTO criarNotificacao(NotificacaoPostRequestDTO  notificacaoDTO);
    
    Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho);
}
