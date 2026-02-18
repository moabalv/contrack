package com.Contrack.model.Funcionario;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import com.Contrack.model.Setor;

import org.hibernate.validator.constraints.br.CPF;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "Funcionarios")
public class Funcionario {

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
