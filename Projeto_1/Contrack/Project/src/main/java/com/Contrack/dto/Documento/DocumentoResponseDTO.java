package com.Contrack.dto.Documento;

import com.Contrack.enums.Prioridade;
import com.Contrack.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class DocumentoResponseDTO {

    private TipoDocumento tipoDocumento;
    private Prioridade prioridade;
    private BigDecimal valor;

    //Aqui vai receber o CNPJ
    private String cliente;

    private LocalDate dataAssinatura;
    private LocalDate prazo;

    //Aqui vai receber email colaborador
    private List<String> colaboradores;

}
