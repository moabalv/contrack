package com.Contrack.Mapper;


import com.Contrack.dto.Documento.DocumentoResponseDTO;
import com.Contrack.model.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// Mapper manual feito para Documento
@Component
public class DocumentoMapper {


    // Converter Documento para DocumentoResponseDTO
    public DocumentoResponseDTO docToResponseDTO(Documento documento) {
        if (documento == null) {
            return null;
        }

    return DocumentoResponseDTO.builder()
            .tipoDocumento(documento.getTipoDocumento())
            .prioridade(documento.getPrioridade())
            .valor(documento.getValor())
            .cliente(documento.getCliente().getCnpj())
            .dataAssinatura(documento.getDataAssinatura())
            .prazo(documento.getPrazo())
            .colaboradores(documento.getColaboradores().stream()
                    .map(Funcionario::getEmail)
                    .toList())
            .build();
     }


     //Converter uma lista de documentos para uma lista de DocumentoResponseDTO
    public List<DocumentoResponseDTO> docToResponseDTOList(List<Documento> documentos) {
        if (documentos == null || documentos.isEmpty()) {
            return Collections.emptyList();
        }

        return documentos.stream()
                .map(this::docToResponseDTO)
                .collect(Collectors.toList());
    }
}
