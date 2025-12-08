package com.Contrack.model;

import com.Contrack.enums.Prioridade;
import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Funcionario.Funcionario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public final class DocumentoFactory {

    private DocumentoFactory() {
    }

    public static Documento criarDocumento(
            TipoDocumento tipoDocumento,
            Prioridade prioridade,
            Cliente cliente,
            Set<Funcionario> colaboradores,
            BigDecimal valor,
            LocalDate dataAssinatura,
            LocalDate dataVencimento
    ) {
        Set<Funcionario> colaboradoresClonados = colaboradores == null
                ? new HashSet<>()
                : new HashSet<>(colaboradores);

        return switch (tipoDocumento) {
            case LICENCA -> LicencaDocumento.builder()
                    .cliente(cliente)
                    .prioridade(prioridade)
                    .valor(valor)
                    .dataAssinatura(dataAssinatura)
                    .dataVencimento(dataVencimento)
                    .colaboradores(colaboradoresClonados)
                    .build();
            case CONTRATO -> ContratoDocumento.builder()
                    .cliente(cliente)
                    .prioridade(prioridade)
                    .valor(valor)
                    .dataAssinatura(dataAssinatura)
                    .dataVencimento(dataVencimento)
                    .colaboradores(colaboradoresClonados)
                    .build();
            case ALVARA -> AlvaraDocumento.builder()
                    .cliente(cliente)
                    .prioridade(prioridade)
                    .valor(valor)
                    .dataAssinatura(dataAssinatura)
                    .dataVencimento(dataVencimento)
                    .colaboradores(colaboradoresClonados)
                    .build();
        };
    }
}
