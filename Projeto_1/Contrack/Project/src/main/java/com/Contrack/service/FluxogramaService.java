package com.Contrack.service;

import com.Contrack.dto.renovacao.FluxogramaRequestDTO;
import com.Contrack.model.renovacao.Fluxograma;

import java.util.List;

public interface FluxogramaService {
    Fluxograma criar(FluxogramaRequestDTO dto);
    List<Fluxograma> listar();
    Fluxograma buscar(Long id);
}
