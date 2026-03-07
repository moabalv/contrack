package com.Contrack.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;

public interface NotificacaoService {
    List<NotificacaoGetRequestDTO> listarNotificacoes();
    NotificacaoGetResponseDTO obterNotificacaoPorId(Long id);
    NotificacaoGetResponseDTO marcarComoLida(Long id);
    Page<NotificacaoPostRequestDTO> filtrarPorLido(int pagina, int tamanho, boolean lido);
    Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho);
}
