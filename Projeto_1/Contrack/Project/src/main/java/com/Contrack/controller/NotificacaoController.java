package com.Contrack.controller;


import com.Contrack.service.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<?> listarNotificacoes() {
        return ResponseEntity.ok(notificacaoService.listarNotificacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarNotificacoesPorId(Long id) {
        return ResponseEntity.ok(notificacaoService.obterNotificacaoPorId(id));
    }
}
