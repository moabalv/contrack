package com.Contrack.service;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.enums.StatusEtapaRenovacao;
import com.Contrack.enums.StatusProcessoRenovacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.model.renovacao.Fluxograma;
import com.Contrack.model.renovacao.ProcessoRenovacao;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FuncionarioRepository;
import com.Contrack.repository.FluxogramaRepository;
import com.Contrack.repository.ProcessoRenovacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProcessoRenovacaoServiceImpl implements ProcessoRenovacaoService {

    private final ProcessoRenovacaoRepository processoRenovacaoRepository;
    private final FluxogramaRepository fluxogramaRepository;
    private final DocumentoRepository documentoRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional
    public ProcessoRenovacao criar(ProcessoRenovacaoRequestDTO dto) {
        Documento contrato = documentoRepository.findById(dto.getDocumentoId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado"));
        Fluxograma fluxograma = fluxogramaRepository.findById(dto.getFluxogramaId())
                .orElseThrow(() -> new IllegalArgumentException("Fluxograma não encontrado"));

        LocalDate inicio = dto.getDataInicio() != null ? dto.getDataInicio() : LocalDate.now();

        ProcessoRenovacao processo = ProcessoRenovacao.builder()
                .contrato(contrato)
                .fluxograma(fluxograma)
                .dataInicio(inicio)
                .status(StatusProcessoRenovacao.EM_ANDAMENTO)
                .build();

        processo.setEtapas(
                fluxograma.getEtapas().stream()
                        .map(nome -> ProcessoRenovacao.EtapaExecucao.builder()
                                .nome(nome)
                                .status(StatusEtapaRenovacao.PENDENTE)
                                .build())
                        .collect(Collectors.toList())
        );

        return processoRenovacaoRepository.save(processo);
    }

    @Override
    @Transactional
    public ProcessoRenovacao concluirEtapaAtual(Long processoId, ConclusaoEtapaDTO dto) {
        ProcessoRenovacao processo = buscar(processoId);
        List<ProcessoRenovacao.EtapaExecucao> etapas = processo.getEtapas();

        if (processo.getIndiceEtapaAtual() >= etapas.size()) {
            throw new IllegalStateException("Todas as etapas já foram concluídas.");
        }

        ProcessoRenovacao.EtapaExecucao etapaAtual = etapas.get(processo.getIndiceEtapaAtual());

        if (etapaAtual.getStatus() == StatusEtapaRenovacao.CONCLUIDA
                || etapaAtual.getStatus() == StatusEtapaRenovacao.ATRASADA) {
            throw new IllegalStateException("A etapa atual já está concluída.");
        }

        boolean marcarAtrasada = Boolean.TRUE.equals(dto.getAtrasada());
        etapaAtual.setStatus(marcarAtrasada ? StatusEtapaRenovacao.ATRASADA : StatusEtapaRenovacao.CONCLUIDA);

        int proximoIndice = processo.getIndiceEtapaAtual() + 1;
        processo.setIndiceEtapaAtual(proximoIndice);

        atualizarStatusProcesso(processo, marcarAtrasada);
        return processoRenovacaoRepository.save(processo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessoRenovacao> listar() {
        return processoRenovacaoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessoRenovacao buscar(Long id) {
        return processoRenovacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
    }

    private void atualizarStatusProcesso(ProcessoRenovacao processo, boolean etapaAtrasada) {
        boolean algumaAtrasada = etapaAtrasada
                || processo.getEtapas().stream().anyMatch(e -> e.getStatus() == StatusEtapaRenovacao.ATRASADA);

        if (algumaAtrasada) {
            processo.setStatus(StatusProcessoRenovacao.ATRASADO);
        } else if (processo.getIndiceEtapaAtual() >= processo.getEtapas().size()) {
            processo.setStatus(StatusProcessoRenovacao.CONCLUIDO);
            processo.setDataFim(LocalDate.now());
        } else {
            processo.setStatus(StatusProcessoRenovacao.EM_ANDAMENTO);
            processo.setDataFim(null);
        }
    }
}
