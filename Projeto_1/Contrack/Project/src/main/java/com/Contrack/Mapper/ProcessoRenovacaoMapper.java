package com.Contrack.Mapper;

import com.Contrack.dto.renovacao.EtapaDTO;
import com.Contrack.dto.renovacao.FluxogramaDTO;
import com.Contrack.dto.renovacao.ProcessoRenovacaoDTO;
import com.Contrack.model.renovacao.ProcessoRenovacao;

import java.util.stream.Collectors;

public class ProcessoRenovacaoMapper {

    public static ProcessoRenovacaoDTO toDTO(ProcessoRenovacao processo) {

        return ProcessoRenovacaoDTO.builder()
                .id(processo.getId())
                .status(processo.getStatus().name())
                .indiceEtapaAtual(processo.getIndiceEtapaAtual())

                .fluxograma(
                        FluxogramaDTO.builder()
                                .nome(processo.getFluxograma().getNome())
                                .build()
                )

                .etapas(
                        processo.getEtapas().stream()
                                .map(e -> EtapaDTO.builder()
                                        .nome(e.getNome())
                                        .status(e.getStatus().name())
                                        .build())
                                .collect(Collectors.toList())
                )

                .build();
    }
}
