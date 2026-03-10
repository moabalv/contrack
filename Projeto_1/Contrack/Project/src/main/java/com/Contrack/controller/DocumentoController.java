package com.Contrack.controller;

import com.Contrack.dto.DocumentoRequestDTO;
import com.Contrack.service.DocumentoService;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/documentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("")
    public ResponseEntity<?> listarDocumentos(@Parameter(
                description = "Critério de ordenação dos documentos",
                example = "prazo | tipo"
        )
            @RequestParam(required = false) String ordenarPor) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.listarDocumentos(ordenarPor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarDocumento(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.buscarDocumento(id));
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criarDocumento(@Valid @RequestBody DocumentoRequestDTO documentoRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(documentoService.criarDocumento(documentoRequestDTO));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getDocumentoByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.getDocumentoByClienteId(clienteId));
    }

    @PostMapping(value = "/atualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> atualizarDocumento(@Valid @RequestBody DocumentoRequestDTO documentoRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.atualizaDocumento(documentoRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> apagarDocumento(@PathVariable Long id) {
        documentoService.apagarDocumento(id);
        return ResponseEntity.noContent().build();
    }
}
