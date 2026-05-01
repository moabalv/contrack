package com.Contrack.service;

import java.util.List;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Value;

import com.Contrack.dto.Notificacao.EmailNotificacaoDTO;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


     @Value("${spring.mail.username}")
    private String remetente;

       @Async
    public void enviarNotificacaoDocumento(Documento doc, String titulo, String textoTempo) {

        if (doc.getColaboradores() == null || doc.getColaboradores().isEmpty()) {
            log.warn("Nenhum colaborador encontrado para o documento ID {}", doc.getId());
            return;
        }

        for (Funcionario funcionario : doc.getColaboradores()) {
            String email = funcionario.getEmail();

            if (email == null || email.isBlank()) {
                log.warn("Funcionário ID {} não possui email cadastrado", funcionario.getId());
                continue;
            }

            try {
                EmailNotificacaoDTO dados = new EmailNotificacaoDTO(
                    funcionario.getNome(),
                    doc.getCliente().getNome(),
                    titulo,
                    textoTempo,
                    doc.getId()
                );

                String html = processarTemplate(dados);
                enviarEmail(email, titulo, html);

                log.info("Email enviado para {} - documento ID {}", email, doc.getId());
            } catch (Exception e) {
                log.error("Falha ao enviar email para {} - documento ID {}: {}", email, doc.getId(), e.getMessage());
            }
        }
    }

    private String processarTemplate(EmailNotificacaoDTO dados) {
        Context context = new Context();
        context.setVariable("nomeColaborador", dados.getNomeColaborador());
        context.setVariable("nomeCliente", dados.getNomeCliente());
        context.setVariable("titulo", dados.getTitulo());
        context.setVariable("textoTempo", dados.getTextoTempo());
        return templateEngine.process("email/notificacao", context);
    }

    private void enviarEmail(String destinatario, String assunto, String html) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

        helper.setFrom(remetente);
        helper.setTo(destinatario);
        helper.setSubject(assunto);
        helper.setText(html, true);

        mailSender.send(mime);
    }
}

