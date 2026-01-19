package com.Contrack.model.Documento;

import com.Contrack.enums.Prioridade;
import com.Contrack.enums.StatusDocumento;
import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Cliente;
import com.Contrack.model.Documento.States.DocumentoState;
import com.Contrack.model.Documento.States.DocumentoStateFactory;
import com.Contrack.model.Funcionario.Funcionario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "Documentos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_documento", discriminatorType = DiscriminatorType.STRING)
@EqualsAndHashCode(exclude = {"cliente", "colaboradores"})
@ToString(exclude = {"cliente", "colaboradores"})
public abstract class Documento {

    @JsonProperty("documento_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "documento_id")
    @Id
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "cliente_id")
    private Cliente cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridade", nullable = false)
    private Prioridade prioridade;

    @Column(name = "valor", precision = 19, scale = 2, nullable = false)
    private BigDecimal valor;

    @Column(name = "data_assinatura", nullable = false)
    private LocalDate dataAssinatura;

    @Column(name = "data_vencimento", nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Default
    private StatusDocumento status = StatusDocumento.DENTRO_DO_PRAZO;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "documento_colaboradores",
            joinColumns = @JoinColumn(name = "documento_id"),
            inverseJoinColumns = @JoinColumn(name = "funcionario_id")
    )
    @Default
    private Set<Funcionario> colaboradores = new HashSet<>();

    @Transient
    @JsonIgnore
    private DocumentoState estadoAtual;

    public abstract TipoDocumento getTipoDocumento();

    public void atualizarStatus(LocalDate referencia) {
        DocumentoState proximoEstado = obterEstadoAtual().transicionar(this, referencia);
        this.estadoAtual = proximoEstado;
        this.status = proximoEstado.getStatus();
    }

    private DocumentoState obterEstadoAtual() {
        if (estadoAtual == null) {
            estadoAtual = DocumentoStateFactory.from(status);
        }
        return estadoAtual;
    }
}
