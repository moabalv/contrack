package com.Contrack.controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.Contrack.model.Documento.Documento;
import com.Contrack.model.arquivo.ArquivoPDF;
import com.Contrack.repository.ArquivoRepository;
import com.Contrack.repository.DocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/arquivos")
public class ArquivoController {

    @Autowired
    private ArquivoRepository arquivoRepository;
    
    @Autowired
    private DocumentoRepository documentoRepository;

    @PostMapping("/upload/{documentoId}")
    public ResponseEntity<String> uploadArquivo(@PathVariable Long documentoId, 
                                                @RequestParam("file") MultipartFile file) {
        try {
            Documento doc = documentoRepository.findById(documentoId)
                .orElseThrow(() -> new RuntimeException("Doc não encontrado"));

            ArquivoPDF arquivo = ArquivoPDF.builder()
                .nomeArquivo(file.getOriginalFilename())
                .tipoArquivo(file.getContentType())
                .dados(file.getBytes())
                .documento(doc)
                .build();

            arquivoRepository.save(arquivo);
            return ResponseEntity.ok("Arquivo enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao enviar: " + e.getMessage());
        }
    }

    // 2. VISUALIZAR (DOWNLOAD/STREAM)
    @GetMapping("/visualizar/{documentoId}")
    public ResponseEntity<byte[]> visualizarArquivo(@PathVariable Long documentoId) {
        ArquivoPDF arquivo = arquivoRepository.findByDocumentoId(documentoId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.getNomeArquivo() + "\"") 
                .body(arquivo.getDados());
    }
}