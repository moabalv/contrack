package com.Contrack.repository;

import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Notificacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {

    boolean existsByDocumentoAndTituloAndDataBetween(
        Documento documento, 
        String titulo, 
        LocalDateTime dataInicio, 
        LocalDateTime dataFim
    );

    Page<Notificacao> findAll(Pageable pageable);
}
