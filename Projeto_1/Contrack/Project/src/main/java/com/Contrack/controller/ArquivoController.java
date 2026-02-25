package com.Contrack.controller;

import com.Contrack.model.arquivo.ArquivoPDF;
import com.Contrack.service.ArquivoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

    @Operation(summary = "Upload de Arquivo PDF", description = "Envia um arquivo PDF para ser vinculado a um documento específico. O arquivo é salvo como BLOB no banco.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo enviado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao enviar arquivo (formato inválido ou tamanho excedido)"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
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

    @Operation(summary = "Verificar existência", description = "Verifica se já existe um arquivo PDF vinculado ao documento, sem precisar baixar o binário inteiro.")
    @ApiResponse(responseCode = "200", description = "Retorna true se existir, false caso contrário")
    @GetMapping("/existe/{documentoId}")
    public ResponseEntity<Boolean> verificarSeExiste(@PathVariable Long documentoId) {
        try {
            ArquivoPDF arquivo = arquivoService.buscarPorDocumentoId(documentoId);
            return ResponseEntity.ok(arquivo != null);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }

    @Operation(summary = "Excluir PDF", description = "Remove permanentemente o arquivo PDF vinculado ao documento.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo excluído com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao excluir ou arquivo não encontrado")
    })
    @DeleteMapping("/delete/{documentoId}")
    public ResponseEntity<String> deletarArquivo(@PathVariable Long documentoId) {
        try {
            arquivoService.deletarArquivo(documentoId);
            return ResponseEntity.ok("Arquivo excluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao excluir arquivo: " + e.getMessage());
        }
    }
    
    @Operation(summary = "Visualizar/Baixar PDF", description = "Recupera o binário do PDF para visualização no navegador ou download.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "PDF recuperado com sucesso", 
            content = @Content(mediaType = "application/pdf", schema = @Schema(type = "string", format = "binary"))),
        @ApiResponse(responseCode = "404", description = "Arquivo não encontrado")
    })
    @GetMapping("/visualizar/{documentoId}")
    public ResponseEntity<byte[]> visualizarArquivo(@PathVariable Long documentoId) {
        try {
            ArquivoPDF arquivo = arquivoService.buscarPorDocumentoId(documentoId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + arquivo.getNomeArquivo() + "\"")
                    .body(arquivo.getDados());
                    
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}