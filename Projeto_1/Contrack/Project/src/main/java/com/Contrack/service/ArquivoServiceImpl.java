package com.Contrack.service;

import com.Contrack.model.Documento.Documento;
import com.Contrack.model.arquivo.ArquivoPDF;
import com.Contrack.repository.ArquivoRepository;
import com.Contrack.repository.DocumentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ArquivoServiceImpl implements ArquivoService {

    @Autowired
    private ArquivoRepository arquivoRepository;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Override
    @Transactional
    public void salvarArquivo(Long documentoId, MultipartFile file) throws IOException {
        Documento doc = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new EntityNotFoundException("Documento não encontrado com ID: " + documentoId));

        ArquivoPDF arquivo = ArquivoPDF.builder()
                .nomeArquivo(file.getOriginalFilename())
                .tipoArquivo(file.getContentType())
                .dados(file.getBytes()) 
                .documento(doc)
                .build();

        arquivoRepository.save(arquivo);
    }

    @Override
    @Transactional
    public void deletarArquivo(Long documentoId) {
        ArquivoPDF arquivo = arquivoRepository.findByDocumentoId(documentoId).orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado para o documento ID: " + documentoId));
        arquivoRepository.delete(arquivo);
    }

    @Override
    @Transactional(readOnly = true)
    public ArquivoPDF buscarPorDocumentoId(Long documentoId) {
        ArquivoPDF arquivo = arquivoRepository.findByDocumentoId(documentoId)
            .orElseThrow(() -> new EntityNotFoundException("Arquivo não encontrado para o documento ID: " + documentoId));
        
        return arquivo;
    }
}