package com.Contrack.service;

import com.Contrack.dto.DocumentoRequestDTO;
import com.Contrack.dto.DocumentoResponseDTO;
import com.Contrack.enums.Prioridade;
import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Cliente;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Documento.Factory.DocumentoFactory;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import com.Contrack.config.AuthenticationHelper;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentoServiceImpl implements DocumentoService {

    private final DocumentoRepository documentoRepository;
    private final ClienteRepository clienteRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AuthenticationHelper authHelper;

    
    @Override
    @Transactional
    public List<DocumentoResponseDTO> getDocumentoByClienteId(Long clienteId, UserDetails userDetails) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));


        List<Documento> documentos = documentoRepository.findByCliente(cliente);

        if (!authHelper.isAdmin(userDetails)) {
            Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
            documentos = documentos.stream()
                .filter(d -> d.getColaboradores().contains(funcionario))
                .collect(Collectors.toList());
    }

        return documentos.stream()
                .map(DocumentoResponseDTO::new)
                .collect(Collectors.toList());}

    @Override
    @Transactional
    public List<DocumentoResponseDTO> listarDocumentos(String ordenarPor, UserDetails userDetails) {

        LocalDate hoje = LocalDate.now();

        List<Documento> documentos;

        if (authHelper.isAdmin(userDetails)) {
            documentos = documentoRepository.findAll();
        } else {
            Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
            documentos = documentoRepository.findByColaboradoresContaining(funcionario);
        }

        documentos.forEach(doc -> doc.atualizarStatus(hoje));

        if (ordenarPor != null) {
            switch (ordenarPor.toLowerCase()) {
                case "prazo":
                    documentos = documentos.stream()
                            .sorted((d1, d2) -> d1.getDataVencimento()
                                    .compareTo(d2.getDataVencimento()))
                            .collect(Collectors.toList());
                    break;

                case "tipo":
                    documentos = documentos.stream()
                            .sorted((d1, d2) -> d1.getTipoDocumento()
                                    .name()
                                    .compareTo(d2.getTipoDocumento().name()))
                            .collect(Collectors.toList());
                    break;

                default:
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Ordenação inválida. Use 'prazo' ou 'tipo'."
                    );
            }
        }

        return documentos.stream()
                .map(DocumentoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DocumentoResponseDTO buscarDocumento(Long id, UserDetails userDetails) {
        Documento documento = documentoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));
        
        authHelper.verificarAcessoAoDocumento(documento, userDetails);
        
        documento.atualizarStatus(LocalDate.now());
        return new DocumentoResponseDTO(documento);
    }

    @Override
    @Transactional
    public DocumentoResponseDTO criarDocumento(DocumentoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        Set<Funcionario> colaboradores = carregarColaboradores(dto.getColaboradoresIds());
        TipoDocumento tipoDocumento = mapearTipoDocumento(dto.getTipoDocumento());
        Prioridade prioridade = mapearPrioridade(dto.getPrioridade());
        BigDecimal valor = normalizarValor(dto.getValor());

        Documento documento = DocumentoFactory.criarDocumento(
                tipoDocumento,
                prioridade,
                cliente,
                colaboradores,
                valor,
                dto.getDataAssinatura(),
                dto.getPrazo()
        );

        documento.atualizarStatus(LocalDate.now());

        Documento salvo = documentoRepository.save(documento);
        return new DocumentoResponseDTO(salvo);
    }

    private Set<Funcionario> carregarColaboradores(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> idsUnicos = ids.stream()
                .map(id -> {
                    if (id == null) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador de colaborador inválido");
                    }
                    return id;
                })
                .collect(Collectors.toSet());

        List<Funcionario> funcionarios = funcionarioRepository.findAllById(idsUnicos);
        Set<Long> encontrados = funcionarios.stream()
                .map(Funcionario::getId)
                .collect(Collectors.toSet());

        if (encontrados.size() != idsUnicos.size()) {
            idsUnicos.removeAll(encontrados);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Colaborador(es) não encontrado(s): " + idsUnicos
            );
        }
        return new HashSet<>(funcionarios);
    }

    private TipoDocumento mapearTipoDocumento(String valor) {
        String normalizado = normalizarTexto(valor);
        try {
            return TipoDocumento.valueOf(normalizado);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tipo de documento inválido: " + valor);
        }
    }

    private Prioridade mapearPrioridade(String valor) {
        String normalizado = normalizarTexto(valor);
        try {
            return Prioridade.valueOf(normalizado);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prioridade inválida: " + valor);
        }
    }

    private BigDecimal normalizarValor(String valorBruto) {
        if (valorBruto == null || valorBruto.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor do documento é obrigatório");
        }
        try {
            String valor = valorBruto.trim();
            if (valor.contains(",")) {
                valor = valor.replace(".", "").replace(",", ".");
            }
            return new BigDecimal(valor);
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valor do documento em formato inválido");
        }
    }

    private String normalizarTexto(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Campo textual obrigatório não informado");
        }
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("[^\\p{Alnum}\\s]", "")
                .trim()
                .replaceAll("\\s+", "_")
                .toUpperCase(Locale.ROOT);
    }

    @Override
    @Transactional
    public DocumentoResponseDTO atualizaDocumento(DocumentoRequestDTO dto, UserDetails userDetails) {
        if (dto == null || dto.getDocumentoId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificador do documento é obrigatório");
        }

        Documento documento = documentoRepository.findById(dto.getDocumentoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado"));

        authHelper.verificarAcessoAoDocumento(documento, userDetails);

        TipoDocumento tipoInformado = mapearTipoDocumento(dto.getTipoDocumento());
        if (!documento.getTipoDocumento().equals(tipoInformado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não é permitido alterar o tipo do documento");
        }

        if (dto.getClienteId() != null) {
            Long clienteAtualId = documento.getCliente() != null ? documento.getCliente().getId() : null;
            if (!dto.getClienteId().equals(clienteAtualId)) {
                Cliente novoCliente = clienteRepository.findById(dto.getClienteId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
                documento.setCliente(novoCliente);
            }
        }

        documento.setPrioridade(mapearPrioridade(dto.getPrioridade()));
        documento.setValor(normalizarValor(dto.getValor()));
        documento.setDataAssinatura(dto.getDataAssinatura());
        documento.setDataVencimento(dto.getPrazo());

        if (dto.getColaboradoresIds() != null) {
            documento.setColaboradores(carregarColaboradores(dto.getColaboradoresIds()));
        }

        documento.atualizarStatus(LocalDate.now());
        Documento atualizado = documentoRepository.save(documento);
        return new DocumentoResponseDTO(atualizado);
    }

    @Override
    @Transactional
    public void apagarDocumento(Long id) {
        if (!documentoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Documento não encontrado");
        }
        documentoRepository.deleteById(id);
    }
}
