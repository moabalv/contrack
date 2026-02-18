package com.Contrack.controller;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.service.ClienteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping(
        value = "/clientes",
        produces = MediaType.APPLICATION_JSON_VALUE
)
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Clientes", description = "Endpoints para gerenciamento de clientes da Contrack")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Busca um cliente por ID", description = "Retorna os detalhes de um único cliente baseado no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> listarClientePorId(@PathVariable Long id){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(clienteService.listarClienteId(id));
    }
    
    @Operation(summary = "Lista todos os clientes", description = "Retorna uma lista com todos os clientes cadastrados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Clientes listados com sucesso")
    })
    @GetMapping("")
    public ResponseEntity<?> listarClientes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clienteService.listarClientes());
    }

    @Operation(summary = "Cria um novo cliente", description = "Adiciona um novo cliente ao sistema com os dados fornecidos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados de cliente inválidos")
    })
    @PostMapping("")
    public ResponseEntity<?> criarCliente(
        @Valid @RequestBody ClientePostPutRequestDTO clienteDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.criarCliente(clienteDTO));
    }
}
