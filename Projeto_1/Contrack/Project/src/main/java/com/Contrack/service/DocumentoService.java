package com.Contrack.service;

import com.Contrack.dto.DocumentoRequestDTO;
import com.Contrack.dto.DocumentoResponseDTO;

import java.util.List;

public interface DocumentoService {
    List<DocumentoResponseDTO> listarDocumentos();
    DocumentoResponseDTO buscarDocumento(Long id);
    DocumentoResponseDTO criarDocumento(DocumentoRequestDTO documentoRequestDTO);
    DocumentoResponseDTO atualizaDocumento (DocumentoRequestDTO documentoRequestDTO);
    List<DocumentoResponseDTO> getDocumentoByClienteId(Long clienteId);
    void apagarDocumento(Long id);
}
