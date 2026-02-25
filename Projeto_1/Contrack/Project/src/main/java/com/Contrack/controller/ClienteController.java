package com.Contrack.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.service.ClienteService;

@RestController
@RequestMapping(
        value = "/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:5173")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> listarClientePorId(@PathVariable Long id){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clienteService.listarClienteId(id));
    }
    
    @GetMapping("")
    public ResponseEntity<?> listarClientes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.listarClientes());
    }

    @PostMapping("")
    public ResponseEntity<?> criarCliente(
        @Valid @RequestBody ClientePostPutRequestDTO clienteDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.criarCliente(clienteDTO));
    }
}
