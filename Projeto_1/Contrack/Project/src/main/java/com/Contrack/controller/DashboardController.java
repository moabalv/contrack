package com.Contrack.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Contrack.dto.Dashboard.DashboardResponseDTO;
import com.Contrack.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    
    @Operation(summary = "Recupera informações do dashboard", description = "Retorna informações resumidas sobre o dashboard.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informações do dashboard recuperadas com sucesso"),
        @ApiResponse(responseCode = "404", description = "Dashboard não encontrado")
    })
    @GetMapping("")
    public ResponseEntity<DashboardResponseDTO> getDashboardInfo() {
        DashboardResponseDTO dashboardInfo = dashboardService.getDashboardInfo();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dashboardInfo);
        }
}