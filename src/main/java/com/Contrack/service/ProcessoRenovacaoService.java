package com.Contrack.service;

import com.Contrack.dto.renovacao.EtapaProcessoUpdateDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.model.renovacao.ProcessoRenovacao;

import java.util.List;

public interface ProcessoRenovacaoService {
    ProcessoRenovacao criarProcesso(ProcessoRenovacaoRequestDTO dto);
    ProcessoRenovacao buscar(Long id);
    List<ProcessoRenovacao> listar();
    ProcessoRenovacao atualizarEtapa(Long etapaId, EtapaProcessoUpdateDTO dto);
}
