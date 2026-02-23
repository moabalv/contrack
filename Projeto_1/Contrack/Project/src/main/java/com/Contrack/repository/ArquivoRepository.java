package com.Contrack.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.Contrack.model.arquivo.*;

@Repository
public interface ArquivoRepository extends JpaRepository<ArquivoPDF, Long> {
    Optional<ArquivoPDF> findByDocumentoId(Long id);
}
