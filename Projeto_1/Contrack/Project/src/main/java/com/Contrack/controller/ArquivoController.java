package com.Contrack.controller;

import com.Contrack.model.arquivo.ArquivoPDF;
import com.Contrack.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/arquivos")
@CrossOrigin(origins = "http://localhost:5173")
public class ArquivoController {

    @Autowired
    private ArquivoService arquivoService;

    @PostMapping("/upload/{documentoId}")
    public ResponseEntity<String> uploadArquivo(@PathVariable Long documentoId,
                                                @RequestParam("file") MultipartFile file) {
        try {
            arquivoService.salvarArquivo(documentoId, file);
            return ResponseEntity.ok("Arquivo enviado com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao enviar arquivo: " + e.getMessage());
        }
    }

    @GetMapping("/visualizar/{documentoId}")
    public ResponseEntity<byte[]> visualizarArquivo(@PathVariable Long documentoId) {
        try {
            ArquivoPDF arquivo = arquivoService.buscarPorDocumentoId(documentoId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    // Usando a constante correta do Spring
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.getNomeArquivo() + "\"")
                    .body(arquivo.getDados());
                    
        } catch (Exception e) {
            // Retorna 404 Not Found se não achar
            return ResponseEntity.notFound().build();
        }
    }
}