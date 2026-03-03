package com.Contrack.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostResponseDTO;

public interface NotificacaoService {

    List<NotificacaoGetRequestDTO> listarNotificacoes();

    NotificacaoGetResponseDTO obterNotificacaoPorId(Long id);

    NotificacaoGetResponseDTO marcarComoLida(Long id);

    List<NotificacaoGetResponseDTO> filtrarPorLido(boolean estado);

    NotificacaoPostResponseDTO criarNotificacao(NotificacaoPostRequestDTO  notificacaoDTO);
    
    Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho);
}
