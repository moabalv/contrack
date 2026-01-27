package com.Contrack.repository;

import com.Contrack.model.Documento.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
    // Busca documentos que vencem exatamente na data passada
    @Query("SELECT d FROM Documento d WHERE d.dataVencimento = :data")
    List<Documento> findByDataVencimento(
        @Param("data") LocalDate data
    );
}
