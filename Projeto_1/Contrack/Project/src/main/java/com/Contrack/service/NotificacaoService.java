package com.Contrack.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;

public interface NotificacaoService {
    List<NotificacaoGetRequestDTO> listarNotificacoes();
    NotificacaoGetResponseDTO obterNotificacaoPorId(Long id, UserDetails userDetails);
    NotificacaoGetResponseDTO marcarComoLida(Long id, UserDetails userDetails);
    Page<NotificacaoPostRequestDTO> filtrarPorLido(int pagina, int tamanho, boolean lido, UserDetails userDetails);
    Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho, UserDetails userDetails);
}
