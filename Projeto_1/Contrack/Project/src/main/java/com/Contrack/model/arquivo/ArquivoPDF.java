package com.Contrack.model.arquivo;

import com.Contrack.model.Documento.Documento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Arquivos_PDF")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoPDF {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;
    
    private String tipoArquivo;

    @Lob 
    @Column(columnDefinition = "LONGBLOB")
    private byte[] dados;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_id")
    private Documento documento;
}