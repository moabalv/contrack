package com.Contrack.controller;

import com.Contrack.dto.DocumentoRequestDTO;
import com.Contrack.service.DocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/documentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentoController {

    private final DocumentoService documentoService;

    @GetMapping("")
    public ResponseEntity<?> listarDocumentos() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(documentoService.listarDocumentos());
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
}
