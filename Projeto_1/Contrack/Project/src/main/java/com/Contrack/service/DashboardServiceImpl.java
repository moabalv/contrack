package com.Contrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.Contrack.config.AuthenticationHelper;
import com.Contrack.dto.Dashboard.DashboardResponseDTO;
import com.Contrack.dto.Dashboard.DocumentoDashboardResponseDTO;
import com.Contrack.dto.Dashboard.FluxogramaDashboardResponseDTO;
import com.Contrack.dto.Dashboard.FuncionarioDashboardResponseDTO;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FluxogramaRepository;
import com.Contrack.repository.FuncionarioRepository;

import lombok.RequiredArgsConstructor;

import com.Contrack.enums.StatusDocumento;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    @Autowired
    private DocumentoRepository documentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FluxogramaRepository fluxogramaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final AuthenticationHelper authHelper;
    
    public DashboardResponseDTO getDashboardInfo(UserDetails userDetails) {


        DashboardResponseDTO dashboardInfo = new DashboardResponseDTO();

        //Se admin fica igual
        if (authHelper.isAdmin(userDetails)) {
            long totalDocumentos = documentoRepository.count();
            long totalDocumentosAtivos = documentoRepository.findByStatus(StatusDocumento.PROXIMO_DO_VENCIMENTO).size() + documentoRepository.findByStatus(StatusDocumento.DENTRO_DO_PRAZO).size();
            long totalClientes = clienteRepository.count();

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
        
        dashboardInfo.setFuncionarios(funcionarioRepository.findAll().stream().map(FuncionarioDashboardResponseDTO::new).toList());
        
    } else {

        Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
        List<Documento> meusDocs = documentoRepository.findByColaboradoresContaining(funcionario);

        dashboardInfo.setTotalDocumentos(meusDocs.size());
        dashboardInfo.setTotalDocumentosAtivos((int) meusDocs.stream()
                .filter(d -> d.getStatus() == StatusDocumento.DENTRO_DO_PRAZO ||
                             d.getStatus() == StatusDocumento.PROXIMO_DO_VENCIMENTO)
                .count()
            );

            
            long clientesUnicos = meusDocs.stream()
                .map(d -> d.getCliente().getId())
                .distinct()
                .count();
            dashboardInfo.setTotalClientes((int) clientesUnicos);


            
            dashboardInfo.setDocumentos(
                meusDocs.stream()
                    .filter(d -> d.getStatus() == StatusDocumento.PROXIMO_DO_VENCIMENTO)
                    .map(DocumentoDashboardResponseDTO::new)
                    .toList()
            );

            // Todos os fluxogramas
            dashboardInfo.setFluxogramas(
                fluxogramaRepository.findAll()
                    .stream()
                    .map(FluxogramaDashboardResponseDTO::new)
                    .toList()
            );


            dashboardInfo.setFuncionarios(
                List.of(new FuncionarioDashboardResponseDTO(funcionario))
            );
    }

        return dashboardInfo;
    }
}