package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

//Representação de um documento associado a contratos ou clientes.
public class Documento {

    @JsonProperty("documento_id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @JoinColumn(name = "cliente")
    @ManyToOne
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento;

    private LocalDate dataAssinatura;

    private LocalDate dataVencimento;
}
