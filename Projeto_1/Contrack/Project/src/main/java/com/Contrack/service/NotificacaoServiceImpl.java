package com.Contrack.service;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.config.AuthenticationHelper;
import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.enums.Status;
import com.Contrack.model.Notificacao;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.NotificacaoRepository;
import com.Contrack.repository.SetorRepository;
import com.Contrack.config.AuthenticationHelper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class NotificacaoServiceImpl implements NotificacaoService {

    @Autowired
    NotificacaoRepository notificacaoRepository;
    @Autowired
    SetorRepository setorRepository;
    @Autowired
    DocumentoRepository documentoRepository;
    @Autowired
    ModelMapper modelMapper;
    private final AuthenticationHelper authHelper;

    @Override
    public List<NotificacaoGetRequestDTO> listarNotificacoes() {
        return notificacaoRepository.findAll()
                .stream()
                .map(n -> modelMapper.map(n, NotificacaoGetRequestDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NotificacaoGetResponseDTO obterNotificacaoPorId(Long id, UserDetails userDetails) {
        Notificacao notificacao = notificacaoRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

            authHelper.verificarAcessoANotificacao(notificacao, userDetails);

        return modelMapper.map(notificacao, NotificacaoGetResponseDTO.class);
    }

    @Override
    public NotificacaoGetResponseDTO marcarComoLida(Long id, UserDetails userDetails) {
        Notificacao notificacao = notificacaoRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

        authHelper.verificarAcessoANotificacao(notificacao, userDetails);

        if (!notificacao.isLido()) {
            notificacao.setLido(true);
            notificacao.setStatus(Status.RESOLVIDO);
        }
        return modelMapper.map(notificacaoRepository.save(notificacao), NotificacaoGetResponseDTO.class);
    }

    @Override
    public Page<NotificacaoPostRequestDTO> filtrarPorLido(int pagina, int tamanho, boolean lido, UserDetails userDetails) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")));
       
        Page<Notificacao> paginaNotificacoesFiltradas;

         if (authHelper.isAdmin(userDetails)) {
            paginaNotificacoesFiltradas = notificacaoRepository.findByLido(pageable, lido);
        } else {
            Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
            paginaNotificacoesFiltradas = notificacaoRepository.findByLidoAndDocumentoColaboradoresContaining(pageable, lido, funcionario);
        }

        List<NotificacaoPostRequestDTO> notificacoesDTO = paginaNotificacoesFiltradas.getContent()
                .stream()
                .map(notificacao -> modelMapper.map(notificacao, NotificacaoPostRequestDTO.class))
                .toList();

                return new PageImpl<>(notificacoesDTO, pageable, paginaNotificacoesFiltradas.getTotalElements());
    }

    @Override
    public Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho, UserDetails userDetails) {
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")));

        Page<Notificacao> paginaNotificacoes;

        if (authHelper.isAdmin(userDetails)) {
            paginaNotificacoes = notificacaoRepository.findAll(pageable);
        } else {
            Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
            paginaNotificacoes = notificacaoRepository.findByDocumentoColaboradoresContaining(pageable, funcionario);
        }

        List<NotificacaoPostRequestDTO> notificacoesDTO = paginaNotificacoes.getContent()
                .stream()
                .map(notificacao -> modelMapper.map(notificacao, NotificacaoPostRequestDTO.class))
                .toList();

        return new PageImpl<>(notificacoesDTO, pageable, paginaNotificacoes.getTotalElements());
    }
}

