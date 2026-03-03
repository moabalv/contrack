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
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostResponseDTO;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Setor;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.SetorRepository;
import jakarta.transaction.Transactional;
import com.Contrack.enums.Status_Notificacao;
import com.Contrack.exception.NotificacaoNaoExisteException;
import com.Contrack.model.Notificacao;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.Contrack.repository.NotificacaoRepository;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotificacaoServiceImpl implements NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;
    @Autowired
    SetorRepository setorRepository;
    @Autowired
    DocumentoRepository documentoRepository;
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
        Notificacao notificacao = notificacaoRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));
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


    public NotificacaoPostResponseDTO criarNotificacao(NotificacaoPostRequestDTO notificacaoDTO) {
        Documento documento = documentoRepository.findById(notificacaoDTO.getDocumentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));

        Setor setor = setorRepository.findById(notificacaoDTO.getSetorId())
                .orElseThrow(() -> new ResponseStatusException( HttpStatus.NOT_FOUND, "Setor não encontrado"));

        Notificacao notificacao = Notificacao.builder().
                documento(documento).
                setor(setor).
                titulo(notificacaoDTO.getTitulo()).
                descricao(notificacaoDTO.getDescricao()).
                status(notificacaoDTO.getStatus()).
                lido(false).
                data(notificacaoDTO.getData()).
                build();

        Notificacao salva = notificacaoRepository.save(notificacao);
        return modelMapper.map(salva, NotificacaoPostResponseDTO.class);
    }

    //Usei PostResquest pq é o DTO que tem todos os campos da Notificação, não quis duplicar código.
    public Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")));

        Page<Notificacao> paginaNotificacoes = notificacaoRepository.findAll(pageable);

        List<NotificacaoPostRequestDTO> notificacoesDTO = paginaNotificacoes.getContent()
                .stream()
                .map(notificacao -> modelMapper.map(notificacao, NotificacaoPostRequestDTO.class))
                .toList();

        return new PageImpl<>(notificacoesDTO, pageable, paginaNotificacoes.getTotalElements());
    }
}

