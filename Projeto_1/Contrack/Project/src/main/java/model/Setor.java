package model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.Funcionario.Funcionario;

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
    //Nao tinha ID no UML, mas achei que era necessario pra ser PK no BD
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    //Ja inicializa aqui?
    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Funcionario> funcionarios;
}
