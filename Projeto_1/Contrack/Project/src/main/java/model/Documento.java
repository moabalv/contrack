package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Documentos")

//Representação de um documento associado a contratos ou clientes.
public class Documento {

    @JsonProperty("documento_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_id")
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(name = "data_assinatura")
    private LocalDate dataAssinatura;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;
}
