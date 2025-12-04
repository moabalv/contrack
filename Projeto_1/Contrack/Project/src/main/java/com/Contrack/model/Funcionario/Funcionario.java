package com.Contrack.model.Funcionario;

import com.Contrack.model.Setor;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "Funcionarios")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_funcionario", discriminatorType = DiscriminatorType.STRING)
//Tomei a liberdade de juntar os tipos de funcionarios para ter um unico BD de Funcionarios
//No BD vai ter um atributo tipo_funcionario pra identificar. Pode mudar isso depois se não for o design desejado!

public abstract class Funcionario {

    @JsonProperty("funcionario_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("cpf")
    @Column(nullable = false, unique = true)
    @CPF
    private String cpf;

    @JsonProperty("email")
    @Column(nullable = false, unique = true)
    @Email
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "setor_id")
    private Setor setor;
}
