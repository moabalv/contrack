package com.Contrack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Contrack.dto.Dashboard.DashboardResponseDTO;
import com.Contrack.dto.Dashboard.DocumentoDashboardResponseDTO;
import com.Contrack.dto.Dashboard.FluxogramaDashboardResponseDTO;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FluxogramaRepository;
import com.Contrack.enums.StatusDocumento;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FluxogramaRepository fluxogramaRepository;
    
    public DashboardResponseDTO getDashboardInfo() {
        long totalDocumentos = documentoRepository.count();
        long totalDocumentosAtivos = documentoRepository.findByStatus(StatusDocumento.PROXIMO_DO_VENCIMENTO).size() + documentoRepository.findByStatus(StatusDocumento.DENTRO_DO_PRAZO).size();
        long totalClientes = clienteRepository.count();
        DashboardResponseDTO dashboardInfo = new DashboardResponseDTO();
        dashboardInfo.setTotalDocumentos((int) totalDocumentos);
        dashboardInfo.setTotalDocumentosAtivos((int) totalDocumentosAtivos);
        dashboardInfo.setTotalClientes((int) totalClientes);    
        
        dashboardInfo.setDocumentos(documentoRepository.findByStatus(StatusDocumento.PROXIMO_DO_VENCIMENTO)
                .stream()
                .map(DocumentoDashboardResponseDTO::new)
                .toList());

        dashboardInfo.setFluxogramas(fluxogramaRepository.findAll()
                .stream()
                .map(FluxogramaDashboardResponseDTO::new)
                .toList());
                
        return dashboardInfo;
    }


}