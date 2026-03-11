package com.Contrack.service;

import com.Contrack.dto.renovacao.ConclusaoEtapaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.enums.StatusEtapaRenovacao;
import com.Contrack.enums.StatusProcessoRenovacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.renovacao.Fluxograma;
import com.Contrack.model.renovacao.ProcessoRenovacao;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FuncionarioRepository;
import com.Contrack.repository.FluxogramaRepository;
import com.Contrack.repository.ProcessoRenovacaoRepository;
import com.Contrack.Mapper.ProcessoRenovacaoMapper;
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

    @Override
    @Transactional
    public ProcessoRenovacaoDTO criar(ProcessoRenovacaoRequestDTO dto) {
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

        ProcessoRenovacao salvo = processoRenovacaoRepository.save(processo);

        return ProcessoRenovacaoMapper.toDTO(salvo);
    }

    @Override
    @Transactional
    public ProcessoRenovacaoDTO concluirEtapaAtual(Long processoId, ConclusaoEtapaDTO dto) {
        ProcessoRenovacao processo = buscarEntidade(processoId);
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

        ProcessoRenovacao salvo = processoRenovacaoRepository.save(processo);
        return ProcessoRenovacaoMapper.toDTO(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessoRenovacaoDTO> listar() {

        return processoRenovacaoRepository.findAll()
                .stream()
                .map(ProcessoRenovacaoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessoRenovacaoDTO buscar(Long id) {
        ProcessoRenovacao p = processoRenovacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
        return ProcessoRenovacaoMapper.toDTO(p);
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessoRenovacaoDTO buscarPorDocumento(Long id) {
        List<ProcessoRenovacao> processos = processoRenovacaoRepository.findAllByContratoId(id);
        
        if (processos.isEmpty()) {
            return null;
        }
        return ProcessoRenovacaoMapper.toDTO(processos.get(processos.size() - 1)); 
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
    private ProcessoRenovacao buscarEntidade(Long id) {

        return processoRenovacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
    }
}
