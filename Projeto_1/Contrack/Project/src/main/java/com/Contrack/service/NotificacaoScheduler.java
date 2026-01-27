package com.Contrack.service;

import com.Contrack.enums.Status_Notificacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Notificacao;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificacaoScheduler {

    private final DocumentoRepository documentoRepository;
    private final NotificacaoRepository notificacaoRepository;

    @Scheduled(cron = "0 0 7 * * *", zone = "America/Sao_Paulo") // Todo dia as 07:00
    @Transactional
    public void processarVencimentos() {
        LocalDate hoje = LocalDate.now();
        
    
        String tituloVencimentoHoje = "ATENÇÃO: Documento vence hoje.";
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = LocalDateTime.of(hoje, LocalTime.MAX);

        // Buscar documentos que vencem HOJE
        List<Documento> documentosVencendoHoje = documentoRepository.findByDataVencimento(hoje);

        for (Documento doc : documentosVencendoHoje) {
            
            // Verifica se já notificamos hoje sobre isso
            boolean jaNotificado = notificacaoRepository.existsByDocumentoAndTituloAndDataBetween(
                    doc, tituloVencimentoHoje, inicioDia, fimDia
            );

            if (!jaNotificado) {
                criarNotificacaoVencimentoHoje(doc, tituloVencimentoHoje);
            }
        }
    }

    private void criarNotificacaoVencimentoHoje(Documento doc, String titulo) {
        // --- MONTAGEM DA MENSAGEM INTELIGENTE ---
        // Padrão sugerido: {{ENTIDADE:ID|NOME_PARA_EXIBIR}}
        
        String nomeCliente = doc.getCliente().getNome();
        Long idCliente = doc.getCliente().getId();

        String mensagemDetalhada = String.format(
            "O documento referente ao cliente {{CLIENTE:%d|%s}} vence hoje. Verifique os detalhes do documento {{DOCUMENTO:%d|aqui}}.",
            idCliente, nomeCliente, // Preenche cliente
            doc.getId()             // Preenche documento
        );

        Notificacao notificacao = Notificacao.builder()
                .documento(doc)
                .setor(null) //lógica de setor
                .titulo(titulo)
                .mensagem(mensagemDetalhada)
                .status(Status_Notificacao.PENDENTE)
                .lido(false)
                .data(LocalDateTime.now())
                .build();

        notificacaoRepository.save(notificacao);
        log.info("Notificação criada para documento ID {}", doc.getId());
    }
}