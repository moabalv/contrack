package com.Contrack.service;

import com.Contrack.dto.renovacao.EtapaModeloDTO;
import com.Contrack.dto.renovacao.FluxogramaRequestDTO;
import com.Contrack.model.renovacao.EtapaModelo;
import com.Contrack.model.renovacao.Fluxograma;
import com.Contrack.repository.FluxogramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FluxogramaServiceImpl implements FluxogramaService {

    private final FluxogramaRepository fluxogramaRepository;

    @Override
    @Transactional
    public Fluxograma criarFluxograma(FluxogramaRequestDTO dto) {
        if (fluxogramaRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um fluxograma com esse nome.");
        }
        Fluxograma fluxograma = montarFluxograma(dto, new Fluxograma());
        return fluxogramaRepository.save(fluxograma);
    }

    @Override
    @Transactional
    public Fluxograma atualizarFluxograma(Long id, FluxogramaRequestDTO dto) {
        Fluxograma existente = fluxogramaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fluxograma não encontrado"));
        existente.limparEtapas();
        montarFluxograma(dto, existente);
        return fluxogramaRepository.save(existente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fluxograma> listar() {
        return fluxogramaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Fluxograma buscar(Long id) {
        return fluxogramaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fluxograma não encontrado"));
    }

    private Fluxograma montarFluxograma(FluxogramaRequestDTO dto, Fluxograma alvo) {
        alvo.setNome(dto.getNome());
        alvo.setDescricao(dto.getDescricao());
        dto.getEtapas().stream()
                .sorted((a, b) -> a.getOrdem().compareTo(b.getOrdem()))
                .map(this::toEntity)
                .forEach(alvo::adicionarEtapa);
        return alvo;
    }

    private EtapaModelo toEntity(EtapaModeloDTO dto) {
        return EtapaModelo.builder()
                .nome(dto.getNome())
                .ordem(dto.getOrdem())
                .prazoDias(dto.getPrazoDias())
                .exigeDocumento(dto.getExigeDocumento())
                .exigeTaxa(dto.getExigeTaxa())
                .checklistModelo(dto.getChecklistModelo())
                .build();
    }
}
