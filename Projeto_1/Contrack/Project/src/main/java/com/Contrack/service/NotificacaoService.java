package com.Contrack.service;


import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.exception.NotificacaoNaoExisteException;
import jakarta.transaction.Transactional;
import com.Contrack.model.Notificacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.Contrack.repository.NotificacaoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<NotificacaoGetRequestDTO> listarNotificacoes() {
        return notificacaoRepository.findAll()
                .stream()
                .map(n -> modelMapper.map(n, NotificacaoGetRequestDTO.class))
                .collect(Collectors.toList());
    }


    public NotificacaoGetResponseDTO obterNotificacaoPorId(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id).orElseThrow(NotificacaoNaoExisteException::new);
        return modelMapper.map(notificacao, NotificacaoGetResponseDTO.class);
    }
}


