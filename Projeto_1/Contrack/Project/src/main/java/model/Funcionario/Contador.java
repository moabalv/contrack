package model.Funcionario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@DiscriminatorValue("CONTADOR")
@Data
@NoArgsConstructor

public class Contador extends Funcionario {
    //Vazia por enquanto
}
