package com.Contrack.service;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.model.renovacao.ProcessoRenovacao;

import java.util.List;

public interface ProcessoRenovacaoService {
    ProcessoRenovacao criar(ProcessoRenovacaoRequestDTO dto);
    ProcessoRenovacao concluirEtapaAtual(Long processoId, ConclusaoEtapaDTO dto);
    List<ProcessoRenovacao> listar();
    ProcessoRenovacao buscar(Long id);
}
