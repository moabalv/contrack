package com.Contrack.service;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;

import java.util.List;

public interface ProcessoRenovacaoService {
    ProcessoRenovacaoDTO criar(ProcessoRenovacaoRequestDTO dto);
    ProcessoRenovacaoDTO concluirEtapaAtual(Long processoId, ConclusaoEtapaDTO dto);
    List<ProcessoRenovacaoDTO> listar();
    ProcessoRenovacaoDTO buscarPorDocumento(Long id);
    ProcessoRenovacaoDTO buscar(Long id);
}
