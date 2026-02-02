package com.Contrack.controller;


import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.service.NotificacaoService;
import com.Contrack.service.NotificacaoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    NotificacaoService notificacaoService;

    @GetMapping("/todas")
    public ResponseEntity<?> listarNotificacoes() {
        return ResponseEntity.ok(notificacaoService.listarNotificacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarNotificacoesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.obterNotificacaoPorId(id));
    }

    @PostMapping("")
    public ResponseEntity<?> criarNotificacao(
            @Valid @RequestBody NotificacaoPostRequestDTO notificacaoPostRequestDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificacaoService.criarNotificacao(notificacaoPostRequestDTO));
    }

    @GetMapping("/paginada")
    public ResponseEntity<Page<NotificacaoPostRequestDTO>> listarComPaginacao(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {

        Page<NotificacaoPostRequestDTO> resultado = notificacaoService.notificacaoPaginada(pagina, tamanho);

        return ResponseEntity.ok(resultado);
    }
}
