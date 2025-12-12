package com.Contrack.controller;


import com.Contrack.service.NotificacaoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    NotificacaoServiceImpl notificacaoServiceImpl;

    @GetMapping
    public ResponseEntity<?> listarNotificacoes() {
        return ResponseEntity.ok(notificacaoServiceImpl.listarNotificacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarNotificacoesPorId(Long id) {
        return ResponseEntity.ok(notificacaoServiceImpl.obterNotificacaoPorId(id));
    }
}
