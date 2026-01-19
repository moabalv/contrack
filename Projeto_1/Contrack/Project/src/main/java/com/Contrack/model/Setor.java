package com.Contrack.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.Contrack.model.Funcionario.Funcionario;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Setores")
public class Setor {

    @JsonProperty("setor_id")
    @Column(name = "setor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Funcionario> funcionarios = new ArrayList<>();
}
