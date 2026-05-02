package com.Contrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.dto.Notificacao.NotificacaoGetRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoGetResponseDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.model.Notificacao;
import com.Contrack.model.NotificacaoLeitura;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FuncionarioRepository;
import com.Contrack.repository.NotificacaoLeituraRepository;
import com.Contrack.repository.NotificacaoRepository;
import com.Contrack.repository.SetorRepository;

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
    @Autowired
    FuncionarioRepository funcionarioRepository;
    @Autowired
    NotificacaoLeituraRepository notificacaoLeituraRepository;

    @Override
    public List<NotificacaoGetRequestDTO> listarNotificacoes() {
        Funcionario funcionario = obterFuncionarioAutenticado();
        List<Notificacao> notificacoes = notificacaoRepository.findAll();
        Set<Long> notificacoesLidas = buscarIdsNotificacoesLidas(funcionario, notificacoes);

        return notificacoes
                .stream()
                .map(n -> toGetRequestDTO(n, notificacoesLidas.contains(n.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public NotificacaoGetResponseDTO obterNotificacaoPorId(Long id) {
        Funcionario funcionario = obterFuncionarioAutenticado();
        Notificacao notificacao = notificacaoRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));
        boolean lido = notificacaoLeituraRepository.existsByNotificacaoAndFuncionario(notificacao, funcionario);
        return toGetResponseDTO(notificacao, lido);
    }

    @Override
    public NotificacaoGetResponseDTO marcarComoLida(Long id) {
        Funcionario funcionario = obterFuncionarioAutenticado();
        Notificacao notificacao = notificacaoRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notificação não encontrada"));

        if (!notificacaoLeituraRepository.existsByNotificacaoAndFuncionario(notificacao, funcionario)) {
            NotificacaoLeitura leitura = NotificacaoLeitura.builder()
                    .notificacao(notificacao)
                    .funcionario(funcionario)
                    .dataLeitura(LocalDateTime.now())
                    .build();
            notificacaoLeituraRepository.save(leitura);
        }

        return toGetResponseDTO(notificacao, true);
    }

    @Override
    public Page<NotificacaoPostRequestDTO> filtrarPorLido(int pagina, int tamanho, boolean lido) {
        Funcionario funcionario = obterFuncionarioAutenticado();
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")));
        List<Notificacao> notificacoesOrdenadas = notificacaoRepository.findAll(pageable.getSort());
        Set<Long> notificacoesLidas = buscarIdsNotificacoesLidas(funcionario, notificacoesOrdenadas);

        List<NotificacaoPostRequestDTO> notificacoesFiltradas = notificacoesOrdenadas
                .stream()
                .filter(notificacao -> notificacoesLidas.contains(notificacao.getId()) == lido)
                .map(notificacao -> toPostRequestDTO(notificacao, notificacoesLidas.contains(notificacao.getId())))
                .toList();

        int inicio = Math.min((int) pageable.getOffset(), notificacoesFiltradas.size());
        int fim = Math.min(inicio + pageable.getPageSize(), notificacoesFiltradas.size());

        return new PageImpl<>(notificacoesFiltradas.subList(inicio, fim), pageable, notificacoesFiltradas.size());
    }

    @Override
    public Page<NotificacaoPostRequestDTO> notificacaoPaginada(int pagina, int tamanho) {
        Funcionario funcionario = obterFuncionarioAutenticado();
        Pageable pageable = PageRequest.of(pagina, tamanho, Sort.by(Sort.Order.desc("data"), Sort.Order.desc("id")));

        Page<Notificacao> paginaNotificacoes = notificacaoRepository.findAll(pageable);
        Set<Long> notificacoesLidas = buscarIdsNotificacoesLidas(funcionario, paginaNotificacoes.getContent());

        List<NotificacaoPostRequestDTO> notificacoesDTO = paginaNotificacoes.getContent()
                .stream()
                .map(notificacao -> toPostRequestDTO(notificacao, notificacoesLidas.contains(notificacao.getId())))
                .toList();

        return new PageImpl<>(notificacoesDTO, pageable, paginaNotificacoes.getTotalElements());
    }

    private Funcionario obterFuncionarioAutenticado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário autenticado não encontrado"));
    }

    private Set<Long> buscarIdsNotificacoesLidas(Funcionario funcionario, List<Notificacao> notificacoes) {
        if (notificacoes.isEmpty()) {
            return Set.of();
        }

        return notificacaoLeituraRepository.findByFuncionarioAndNotificacaoIn(funcionario, notificacoes)
                .stream()
                .map(leitura -> leitura.getNotificacao().getId())
                .collect(Collectors.toSet());
    }

    private NotificacaoGetRequestDTO toGetRequestDTO(Notificacao notificacao, boolean lido) {
        NotificacaoGetRequestDTO dto = modelMapper.map(notificacao, NotificacaoGetRequestDTO.class);
        dto.setLido(lido);
        return dto;
    }

    private NotificacaoGetResponseDTO toGetResponseDTO(Notificacao notificacao, boolean lido) {
        NotificacaoGetResponseDTO dto = modelMapper.map(notificacao, NotificacaoGetResponseDTO.class);
        dto.setLido(lido);
        return dto;
    }

    private NotificacaoPostRequestDTO toPostRequestDTO(Notificacao notificacao, boolean lido) {
        NotificacaoPostRequestDTO dto = modelMapper.map(notificacao, NotificacaoPostRequestDTO.class);
        dto.setLido(lido);
        return dto;
    }
}
