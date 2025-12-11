package com.Contrack.model;

import com.Contrack.enums.Status_Notificacao;
import jakarta.persistence.EnumType;


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
    private String mensagem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status_Notificacao status;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private boolean lido;

    @Column(name = "data_notificao", nullable = false)
    private LocalDateTime data;
}
