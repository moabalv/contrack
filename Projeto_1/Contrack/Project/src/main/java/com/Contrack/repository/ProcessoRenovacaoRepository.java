package com.Contrack.repository;

import com.Contrack.model.renovacao.ProcessoRenovacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessoRenovacaoRepository extends JpaRepository<ProcessoRenovacao, Long> {
    List<ProcessoRenovacao> findAllByContratoId(Long documentoId);
}
