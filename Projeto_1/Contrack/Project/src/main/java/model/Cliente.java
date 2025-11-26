package model;


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

//Representação de um cliente no sistema.
public class Cliente {

    @JsonProperty("cliente_id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JsonProperty("nome")
    @Column(nullable = false)
    private String nome;

    @JsonProperty("cnpj")
    @Column(nullable = false)
    @CNPJ
    private String cnpj;

    @JsonProperty("email")
    @Email
    @Column(nullable = false)
    private String email;

    @JsonProperty("telefone")
    @Column(nullable = false)
    private String telefone;

//0 ou mais contratos - voltar aqui depois
    //@ManyToOne
    //array de contratos em Cliente?
}
