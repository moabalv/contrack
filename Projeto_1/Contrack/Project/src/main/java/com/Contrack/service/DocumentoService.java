package com.Contrack.service;

import com.Contrack.dto.DocumentoRequestDTO;
import com.Contrack.dto.DocumentoResponseDTO;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface DocumentoService {
    List<DocumentoResponseDTO> listarDocumentos(String ordenarPor, UserDetails userDetails);
    DocumentoResponseDTO buscarDocumento(Long id, UserDetails userDetails);
    DocumentoResponseDTO criarDocumento(DocumentoRequestDTO documentoRequestDTO);
    DocumentoResponseDTO atualizaDocumento (DocumentoRequestDTO documentoRequestDTO, UserDetails userDetails);
    List<DocumentoResponseDTO> getDocumentoByClienteId(Long clienteId, UserDetails userDetails);
    void apagarDocumento(Long id);
}
