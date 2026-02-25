package com.Contrack.model.renovacao;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(length = 500)
    private String descricao;

    /**
     * Lista encadeada de etapas apenas por nome.
     * OrderColumn preserva a sequência definida pelo administrador.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "Fluxograma_Etapas", joinColumns = @JoinColumn(name = "fluxograma_id"))
    @OrderColumn(name = "ordem")
    private List<String> etapas;
}
