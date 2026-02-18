package com.Contrack.controller;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.service.ProcessoRenovacaoService;

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
@RequestMapping(value = "/processos-renovacao", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Processos de Renovação", description = "Gerenciamento da execução das etapas de renovação de contratos. Associa documentos a fluxogramas.")
public class ProcessoRenovacaoController {

    private final ProcessoRenovacaoService processoRenovacaoService;

    @Operation(summary = "Inicia um novo processo de renovação", 
               description = "Instancia um fluxograma para um documento específico, gerando todas as etapas pendentes.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Processo iniciado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Documento ou Fluxograma não encontrado")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criar(@Valid @RequestBody ProcessoRenovacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoRenovacaoService.criar(dto));
    }

    @Operation(summary = "Lista todos os processos de renovação", description = "Retorna todos os processos de renovação cadastrados no sistema.")
    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso")
    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(processoRenovacaoService.listar());
    }

    @Operation(summary = "Busca processo por ID único")
    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(processoRenovacaoService.buscar(id));
    }

    @Operation(summary = "Busca processo vinculado a um documento", 
               description = "Retorna o processo de renovação atual associado a um contrato específico.")
    @GetMapping("/documento/{id}")
    public ResponseEntity<?> buscarPorDocumento(@PathVariable Long id) {
        return ResponseEntity.ok(processoRenovacaoService.buscarPorDocumento(id));
    }

    @Operation(summary = "Conclui a etapa atual do processo", 
               description = "Avança o índice da etapa atual e pode marcar o processo como 'ATRASADO' ou 'CONCLUÍDO'.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Etapa atualizada com sucesso"),
        @ApiResponse(responseCode = "409", description = "Todas as etapas já foram concluídas ou etapa já encerrada")
    })
    @PatchMapping(value = "/{id}/etapa/atual", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> concluirEtapaAtual(@PathVariable Long id, @RequestBody ConclusaoEtapaDTO dto) {
        return ResponseEntity.ok(processoRenovacaoService.concluirEtapaAtual(id, dto));
    }
}
