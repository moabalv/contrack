package com.Contrack.controller;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.service.ProcessoRenovacaoService;
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
public class ProcessoRenovacaoController {

    private final ProcessoRenovacaoService processoRenovacaoService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> criar(@Valid @RequestBody ProcessoRenovacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(processoRenovacaoService.criar(dto));
    }

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(processoRenovacaoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(processoRenovacaoService.buscar(id));
    }

    @GetMapping("/documento/{id}")
    public ResponseEntity<?> buscarPorDocumento(@PathVariable Long id) {
        ProcessoRenovacaoDTO processo = processoRenovacaoService.buscarPorDocumento(id);
    
        if (processo == null) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(processo);
    }
    
    @PatchMapping(value = "/{id}/etapa/atual", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> concluirEtapaAtual(@PathVariable Long id, @RequestBody ConclusaoEtapaDTO dto) {
        return ResponseEntity.ok(processoRenovacaoService.concluirEtapaAtual(id, dto));
    }
}
