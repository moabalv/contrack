package com.Contrack.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Clientes")

//Representação de um cliente no sistema.
public class Cliente {

    @JsonProperty("cliente_id")
    @Column(name = "cliente_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("cnpj")
    @Column(nullable = false)
    @CNPJ
    private String cnpj;

    @JsonProperty("email")
    @Column(nullable = false)
    @Email
    private String email;

    @JsonProperty("telefone")
    @Column(nullable = false)
    private String telefone;

//0 ou mais contratos - voltar aqui depois
    //@ManyToOne
    //atributo de documento em Cliente?
}
