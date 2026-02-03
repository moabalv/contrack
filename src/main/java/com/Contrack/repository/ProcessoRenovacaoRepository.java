package com.Contrack.repository;

import com.Contrack.model.Documento.Documento;
import com.Contrack.model.renovacao.ProcessoRenovacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProcessoRenovacaoRepository extends JpaRepository<ProcessoRenovacao, Long> {
    List<ProcessoRenovacao> findByContrato(Documento contrato);
}
