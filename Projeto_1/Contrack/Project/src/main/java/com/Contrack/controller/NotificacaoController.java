package com.Contrack.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Contrack.dto.Notificacao.NotificacaoPostRequestDTO;
import com.Contrack.service.NotificacaoService;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificacaoController {

    @Autowired
    NotificacaoService notificacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> listarNotificacoesPorId(@PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificacaoService.obterNotificacaoPorId(id, userDetails));
    }
    
    @PostMapping("/{id}/lida")
    public ResponseEntity<?> marcarComoLida(@PathVariable Long id,
        @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id, userDetails));
    }

    @GetMapping("")
    public ResponseEntity<?> listarNotificacoes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "5") int tamanho,
            @RequestParam(required = false) Boolean lido,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (lido != null) {
            Page<NotificacaoPostRequestDTO> resultado = notificacaoService.filtrarPorLido(pagina, tamanho, lido, userDetails);
            return ResponseEntity.ok(resultado);
        }

        Page<NotificacaoPostRequestDTO> resultado =
                notificacaoService.notificacaoPaginada(pagina, tamanho, userDetails);

        return ResponseEntity.ok(resultado);
    }
}