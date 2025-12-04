package com.Contrack.model;

import com.Contrack.enums.Prioridade;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.Contrack.enums.TipoDocumento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.Contrack.model.Funcionario.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    @JoinColumn(nullable = false, name = "cliente")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "tipo_documento")
    private TipoDocumento tipoDocumento;

    @Column(nullable = false, name = "data_assinatura")
    private LocalDate dataAssinatura;

    @Column(nullable = false,name = "data_vencimento")
    private LocalDate prazo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Prioridade prioridade;

    @ManyToMany
    @JoinTable(name = "documento_funcionarios",
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id"))
    private List<Funcionario> colaboradores;
}
