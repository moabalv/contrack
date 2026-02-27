package com.Contrack.controller;


import com.Contrack.service.NotificacaoService;
import com.Contrack.service.NotificacaoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificacaoController {

     @Autowired
    NotificacaoService notificacaoService;

    @GetMapping
    public ResponseEntity<?> listarNotificacoes() {
        return ResponseEntity.ok(notificacaoService.listarNotificacoes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> listarNotificacoesPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.obterNotificacaoPorId(id));
    }

    @PostMapping("/{id}/lida")
    public ResponseEntity<?> marcarComoLida(@PathVariable Long id) {
        return ResponseEntity.ok(notificacaoService.marcarComoLida(id));
    }

     @GetMapping("/filtrar")
      public ResponseEntity<?> filtrarPorLido(@RequestParam(defaultValue = "true") boolean estado ) {
        return ResponseEntity.status(HttpStatus.OK).body(notificacaoService.filtrarPorLido(estado));
      }
}
