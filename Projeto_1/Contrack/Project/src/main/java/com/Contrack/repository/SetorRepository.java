package com.Contrack.repository;

import com.Contrack.model.Notificacao;
import com.Contrack.model.Setor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Long> {
}
