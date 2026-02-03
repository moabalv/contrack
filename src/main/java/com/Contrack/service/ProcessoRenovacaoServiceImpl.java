package com.Contrack.service;

import com.Contrack.dto.renovacao.EtapaProcessoUpdateDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoRequestDTO;
import com.Contrack.enums.StatusEtapaRenovacao;
import com.Contrack.enums.StatusProcessoRenovacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.model.renovacao.EtapaModelo;
import com.Contrack.model.renovacao.EtapaProcesso;
import com.Contrack.model.renovacao.Fluxograma;
import com.Contrack.model.renovacao.ProcessoRenovacao;
import com.Contrack.model.renovacao.RegistroHistorico;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.EtapaProcessoRepository;
import com.Contrack.repository.FluxogramaRepository;
import com.Contrack.repository.FuncionarioRepository;
import com.Contrack.repository.ProcessoRenovacaoRepository;
import com.Contrack.repository.RegistroHistoricoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessoRenovacaoServiceImpl implements ProcessoRenovacaoService {

    private final ProcessoRenovacaoRepository processoRenovacaoRepository;
    private final FluxogramaRepository fluxogramaRepository;
    private final DocumentoRepository documentoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final EtapaProcessoRepository etapaProcessoRepository;
    private final RegistroHistoricoRepository registroHistoricoRepository;

    @Override
    @Transactional
    public ProcessoRenovacao criarProcesso(ProcessoRenovacaoRequestDTO dto) {
        Documento contrato = documentoRepository.findById(dto.getContratoId())
                .orElseThrow(() -> new IllegalArgumentException("Contrato não encontrado"));
        Fluxograma fluxograma = fluxogramaRepository.findById(dto.getFluxogramaId())
                .orElseThrow(() -> new IllegalArgumentException("Fluxograma não encontrado"));
        Funcionario responsavel = funcionarioRepository.findById(dto.getResponsavelPrincipalId())
                .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));

        LocalDate dataInicio = dto.getDataInicio() != null ? dto.getDataInicio() : LocalDate.now();

        ProcessoRenovacao processo = ProcessoRenovacao.builder()
                .contrato(contrato)
                .fluxograma(fluxograma)
                .responsavelPrincipal(responsavel)
                .dataInicio(dataInicio)
                .status(StatusProcessoRenovacao.EM_ANDAMENTO)
                .build();

        fluxograma.getEtapas().stream()
                .sorted(Comparator.comparing(EtapaModelo::getOrdem))
                .map(modelo -> instanciarEtapa(modelo, dataInicio))
                .forEach(processo::adicionarEtapa);

        processo.adicionarHistorico(criarHistorico(processo, null, "Processo criado a partir do fluxograma " + fluxograma.getNome()));

        return processoRenovacaoRepository.save(processo);
    }

    @Override
    @Transactional(readOnly = true)
    public ProcessoRenovacao buscar(Long id) {
        return processoRenovacaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProcessoRenovacao> listar() {
        return processoRenovacaoRepository.findAll();
    }

    @Override
    @Transactional
    public ProcessoRenovacao atualizarEtapa(Long etapaId, EtapaProcessoUpdateDTO dto) {
        EtapaProcesso etapa = etapaProcessoRepository.findById(etapaId)
                .orElseThrow(() -> new IllegalArgumentException("Etapa não encontrada"));

        if (dto.getStatus() == StatusEtapaRenovacao.CONCLUIDA) {
            validarSequencia(etapa);
        }

        etapa.setChecklist(dto.getChecklist());
        etapa.setComprovanteUrl(dto.getComprovanteUrl());
        etapa.setTaxaPaga(dto.getTaxaPaga());
        if (dto.getResponsavelId() != null) {
            Funcionario resp = funcionarioRepository.findById(dto.getResponsavelId())
                    .orElseThrow(() -> new IllegalArgumentException("Responsável não encontrado"));
            etapa.setResponsavel(resp);
        }

        StatusEtapaRenovacao statusAnterior = etapa.getStatus();
        etapa.setStatus(dto.getStatus());
        if (dto.getStatus() == StatusEtapaRenovacao.CONCLUIDA) {
            etapa.setDataConclusao(LocalDate.now());
            if (etapa.getDataConclusao().isAfter(etapa.getDataLimite())) {
                etapa.setStatus(StatusEtapaRenovacao.ATRASADA);
            }
        }
        etapaProcessoRepository.save(etapa);

        atualizarStatusProcesso(etapa.getProcesso());

        registroHistoricoRepository.save(criarHistorico(
                etapa.getProcesso(),
                etapa,
                "Etapa " + etapa.getNome() + " alterada de " + statusAnterior + " para " + etapa.getStatus()
        ));

        return etapa.getProcesso();
    }

    private void validarSequencia(EtapaProcesso etapa) {
        ProcessoRenovacao processo = etapa.getProcesso();
        List<EtapaProcesso> etapasOrdenadas = etapaProcessoRepository.findByProcessoOrderByOrdemAsc(processo);
        for (EtapaProcesso e : etapasOrdenadas) {
            if (e.getOrdem().equals(etapa.getOrdem())) {
                break;
            }
            if (e.getStatus() != StatusEtapaRenovacao.CONCLUIDA) {
                throw new IllegalStateException("Não é possível concluir uma etapa antes de concluir as etapas anteriores.");
            }
        }
    }

    private EtapaProcesso instanciarEtapa(EtapaModelo modelo, LocalDate dataInicio) {
        LocalDate dataLimite = dataInicio.plusDays(modelo.getPrazoDias());
        return EtapaProcesso.builder()
                .etapaModelo(modelo)
                .nome(modelo.getNome())
                .ordem(modelo.getOrdem())
                .dataLimite(dataLimite)
                .exigeDocumento(modelo.getExigeDocumento())
                .exigeTaxa(modelo.getExigeTaxa())
                .checklist(modelo.getChecklistModelo())
                .build();
    }

    private void atualizarStatusProcesso(ProcessoRenovacao processo) {
        List<EtapaProcesso> etapas = processo.getEtapas();
        boolean algumaAtrasada = etapas.stream()
                .anyMatch(e -> e.getStatus() == StatusEtapaRenovacao.ATRASADA
                        || (e.getStatus() != StatusEtapaRenovacao.CONCLUIDA && LocalDate.now().isAfter(e.getDataLimite())));

        boolean todasConcluidas = etapas.stream()
                .allMatch(e -> e.getStatus() == StatusEtapaRenovacao.CONCLUIDA
                        || (e.getStatus() == StatusEtapaRenovacao.ATRASADA && e.getDataConclusao() != null));

        if (algumaAtrasada) {
            processo.setStatus(StatusProcessoRenovacao.ATRASADO);
        } else if (todasConcluidas) {
            processo.setStatus(StatusProcessoRenovacao.CONCLUIDO);
            processo.setDataFim(LocalDate.now());
        } else {
            processo.setStatus(StatusProcessoRenovacao.EM_ANDAMENTO);
            processo.setDataFim(null);
        }

        processoRenovacaoRepository.save(processo);
    }

    private RegistroHistorico criarHistorico(ProcessoRenovacao processo, EtapaProcesso etapa, String mensagem) {
        return RegistroHistorico.builder()
                .processo(processo)
                .etapaProcesso(etapa)
                .mensagem(mensagem)
                .dataRegistro(LocalDateTime.now())
                .build();
    }
}
