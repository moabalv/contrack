package com.Contrack.repository;

import com.Contrack.model.renovacao.Fluxograma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FluxogramaRepository extends JpaRepository<Fluxograma, Long> {
    boolean existsByNome(String nome);
}
