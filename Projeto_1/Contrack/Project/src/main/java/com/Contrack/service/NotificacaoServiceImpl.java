package com.Contrack.service;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.enums.Status_Notificacao;
import com.Contrack.exception.NotificacaoNaoExisteException;
import com.Contrack.model.Notificacao;
import com.Contrack.repository.NotificacaoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificacaoServiceImpl implements NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override 
    public List<NotificacaoGetRequestDTO> listarNotificacoes() {
        return notificacaoRepository.findAll()
                .stream()
                .map(n -> modelMapper.map(n, NotificacaoGetRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NotificacaoGetResponseDTO obterNotificacaoPorId(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id).orElseThrow(NotificacaoNaoExisteException::new);
        return modelMapper.map(notificacao, NotificacaoGetResponseDTO.class);
    }

    @Override
    public NotificacaoGetResponseDTO marcarComoLida(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id).orElseThrow(NotificacaoNaoExisteException::new);
        if (!notificacao.isLido()) {
            notificacao.setLido(true);
            notificacao.setStatus(Status_Notificacao.RESOLVIDO);
        }
        return modelMapper.map(notificacaoRepository.save(notificacao), NotificacaoGetResponseDTO.class);
    }

    @Override
    public List<NotificacaoGetResponseDTO> filtrarPorLido(boolean estado) {
        List<Notificacao> notificacoesEncontradas = notificacaoRepository.findByLido(estado);

        if(notificacoesEncontradas.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nenhuma notificação encontrada");
        }

       return notificacoesEncontradas.stream()
                .map(n -> modelMapper.map(n, NotificacaoGetResponseDTO.class))
                .collect(Collectors.toList());
    }
}
