package com.Contrack.service;

import com.Contrack.dto.renovacao.FluxogramaRequestDTO;
import com.Contrack.model.renovacao.Fluxograma;
import com.Contrack.repository.FluxogramaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FluxogramaServiceImpl implements FluxogramaService {

    private final FluxogramaRepository fluxogramaRepository;

    @Override
    @Transactional
    public Fluxograma criar(FluxogramaRequestDTO dto) {
        Fluxograma fluxograma = Fluxograma.builder()
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .etapas(dto.getEtapas())
                .build();
        return fluxogramaRepository.save(fluxograma);
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

    @Override
    @Transactional
    public Fluxograma atualizar(Long id, FluxogramaRequestDTO dto) {
        Fluxograma fluxograma = fluxogramaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fluxograma não encontrado"));

        fluxograma.setNome(dto.getNome());
        fluxograma.setDescricao(dto.getDescricao());
        fluxograma.setEtapas(dto.getEtapas());

        return fluxogramaRepository.save(fluxograma);
    }
}
