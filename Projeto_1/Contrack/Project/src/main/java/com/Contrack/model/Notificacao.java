package com.Contrack.model;

import com.Contrack.enums.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.Contrack.model.Documento.Documento;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Notificacoes")

public class Notificacao {

    @JsonProperty("notificacao_id")
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setor setor;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean lido;

    @Column(name = "data_notificacao", nullable = false)
    private LocalDate data;
}
