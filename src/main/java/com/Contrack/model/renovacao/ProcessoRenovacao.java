package com.Contrack.model.renovacao;

import com.Contrack.enums.StatusProcessoRenovacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Processos_Renovacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<EtapaProcesso> etapas = new ArrayList<>();

    @OneToMany(mappedBy = "processo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RegistroHistorico> historicos = new ArrayList<>();

    public void adicionarEtapa(EtapaProcesso etapa) {
        etapa.setProcesso(this);
        this.etapas.add(etapa);
    }

    public void adicionarHistorico(RegistroHistorico registro) {
        registro.setProcesso(this);
        this.historicos.add(registro);
    }
}
