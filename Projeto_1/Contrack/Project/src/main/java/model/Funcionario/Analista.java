package model.Funcionario;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@DiscriminatorValue("ANALISTA")
@Data
@NoArgsConstructor

public class Analista extends Funcionario {
    //Vazia por enquanto
}
