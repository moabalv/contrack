package com.Contrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Contrack.model.Documento.Documento;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
