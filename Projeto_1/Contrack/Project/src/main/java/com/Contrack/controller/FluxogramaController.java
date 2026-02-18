package com.Contrack.controller;

import com.Contrack.dto.renovacao.FluxogramaRequestDTO;
import com.Contrack.service.FluxogramaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/fluxogramas", produces = MediaType.APPLICATION_JSON_VALUE)
public class FluxogramaController {

    private final FluxogramaService fluxogramaService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criar(@Valid @RequestBody FluxogramaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fluxogramaService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(fluxogramaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(fluxogramaService.buscar(id));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody FluxogramaRequestDTO dto) {
        return ResponseEntity.ok(fluxogramaService.atualizar(id, dto));
    }
}
