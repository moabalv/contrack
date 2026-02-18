package com.Contrack.service;

import com.Contrack.model.arquivo.ArquivoPDF;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface ArquivoService {
    void salvarArquivo(Long documentoId, MultipartFile file) throws IOException;
    ArquivoPDF buscarPorDocumentoId(Long documentoId);
    void deletarArquivo(Long documentoId);
}