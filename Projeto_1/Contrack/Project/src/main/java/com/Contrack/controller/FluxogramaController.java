package com.Contrack.controller;

import com.Contrack.dto.renovacao.FluxogramaRequestDTO;
import com.Contrack.service.FluxogramaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/fluxogramas", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Fluxogramas", description = "Criação de fluxos de renovação para documentos. O fluxo é genérico e pode ser aplicado a qualquer tipo de documento. Um fluxo associado a um documento é chamado de processo de renovação.")
public class FluxogramaController {

    private final FluxogramaService fluxogramaService;

    @Operation(summary = "Cria um novo fluxograma", description = "Cadastra um fluxo com nome, descrição e uma lista ordenada de etapas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Fluxograma criado com sucesso")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criar(@Valid @RequestBody FluxogramaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fluxogramaService.criar(dto));
    }

    @Operation(summary = "Lista todos os fluxogramas", description = "Retorna todos os fluxogramas cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(fluxogramaService.listar());
    }

    @Operation(summary = "Busca fluxograma por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Fluxograma encontrado"),
        @ApiResponse(responseCode = "404", description = "ID não localizado no banco de dados")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(fluxogramaService.buscar(id));
    }
}
