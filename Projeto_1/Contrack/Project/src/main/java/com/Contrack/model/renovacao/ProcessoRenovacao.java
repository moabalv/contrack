package com.Contrack.model.renovacao;

import com.Contrack.enums.StatusEtapaRenovacao;
import com.Contrack.enums.StatusProcessoRenovacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Processos_Renovacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessoRenovacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "processo_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "documento_id")
    private Documento contrato;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fluxograma_id")
    private Fluxograma fluxograma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id")
    private Funcionario responsavelPrincipal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusProcessoRenovacao status = StatusProcessoRenovacao.EM_ANDAMENTO;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim")
    private LocalDate dataFim;

    /**
     * Snapshot simples das etapas (apenas nome + status) no momento da criação.
     */
    @ElementCollection
    private List<EtapaExecucao> etapas;

    @Column(name = "indice_etapa_atual", nullable = false)
    @Builder.Default
    private Integer indiceEtapaAtual = 0;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Embeddable
    public static class EtapaExecucao {
        private String nome;
        @Enumerated(EnumType.STRING)
        @Builder.Default
        private StatusEtapaRenovacao status = StatusEtapaRenovacao.PENDENTE;
    }
}
