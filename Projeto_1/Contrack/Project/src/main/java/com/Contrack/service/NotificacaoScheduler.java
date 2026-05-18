package com.Contrack.service;

import com.Contrack.model.Documento.Documento;
import com.Contrack.enums.Status;
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

    @Scheduled(cron = "1 * * * * *", zone = "America/Sao_Paulo") // Rodando a cada minuto para testes
    @Transactional
    public void processarVencimentos() {
        LocalDate hoje = LocalDate.now();

        // --- CENÁRIO 1: Vence HOJE ---
        verificarEGerar(
            hoje, 
            "ATENÇÃO: Documento vence hoje.", 
            "vence hoje"
        );

        // --- CENÁRIO 2: Vence AMANHÃ (Daqui a 1 dia) ---
        verificarEGerar(
            hoje.plusDays(1), 
            "AVISO: Documento vence amanhã.", 
            "vence amanhã"
        );

        // --- CENÁRIO 3: Vence SEMANA QUE VEM (Daqui a 7 dias) ---
        verificarEGerar(
            hoje.plusDays(7), 
            "LEMBRETE: Documento vence em 7 dias.", 
            "vence em 7 dias"
        );
    }

    /**
     * Método genérico que busca documentos de uma data especifica e gera notificações
     */
    private void verificarEGerar(LocalDate dataBusca, String titulo, String textoTempo) {
        
        // 1. Busca documentos que vencem na data calculada (hoje, amanhã ou daqui 7 dias)
        List<Documento> documentos = documentoRepository.findByDataVencimento(dataBusca);

        // 2. Define o intervalo de "HOJE" para verificar se já rodamos o script hoje
        // Importante: A verificação de duplicidade é sempre sobre "HOJE", independente de quando o doc vence.
        LocalDateTime inicioDiaHoje = LocalDate.now().atStartOfDay();
        LocalDateTime fimDiaHoje = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        for (Documento doc : documentos) {
            
            // Verifica se já criamos uma notificação com ESSE TÍTULO para esse documento HOJE
            boolean jaNotificado = notificacaoRepository.existsByDocumentoAndTituloAndDataBetween(
                    doc, titulo, inicioDiaHoje, fimDiaHoje
            );

            if (!jaNotificado) {
                criarNotificacao(doc, titulo, textoTempo);
            }
        }
    }

    private void criarNotificacao(Documento doc, String titulo, String textoTempo) {
        // --- MONTAGEM DA MENSAGEM INTELIGENTE ---
        String nomeCliente = doc.getCliente().getNome();
        Long idCliente = doc.getCliente().getId();

        // Monta a mensagem dinâmica: "... vence hoje" ou "... vence amanhã", etc.
        String mensagemDetalhada = String.format(
            "O documento referente ao cliente {{CLIENTE:%d|%s}} %s. Verifique os detalhes do documento {{DOCUMENTO:%d|aqui}}.",
            idCliente, nomeCliente, 
            textoTempo, // Aqui entra o "vence hoje", "vence amanhã", etc.
            doc.getId()
        );

        Notificacao notificacao = Notificacao.builder()
                .documento(doc)
                .setor(null) // ou null, conforme sua lógica
                .titulo(titulo)
                .descricao(mensagemDetalhada)
                .status(Status.PENDENTE)
                .data(LocalDateTime.now()) // Data de criação da notificação é AGORA
                .build();

        notificacaoRepository.save(notificacao);
        log.info("Notificação gerada: '{}' para documento ID {}", titulo, doc.getId());
    }
}
