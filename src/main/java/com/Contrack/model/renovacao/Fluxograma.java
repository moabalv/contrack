package com.Contrack.model.renovacao;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Fluxogramas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fluxograma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fluxograma_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @OneToMany(mappedBy = "fluxograma", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    @Builder.Default
    private List<EtapaModelo> etapas = new ArrayList<>();

    public void adicionarEtapa(EtapaModelo etapa) {
        etapa.setFluxograma(this);
        this.etapas.add(etapa);
    }

    public void limparEtapas() {
        this.etapas.forEach(e -> e.setFluxograma(null));
        this.etapas.clear();
    }
}
