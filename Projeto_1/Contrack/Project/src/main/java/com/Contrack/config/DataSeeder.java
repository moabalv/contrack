package com.Contrack.config;

import com.Contrack.enums.Role;
import com.Contrack.enums.Prioridade;
import com.Contrack.enums.TipoDocumento;
import com.Contrack.model.Cliente;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Documento.Factory.DocumentoFactory;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.repository.DocumentoRepository;
import com.Contrack.repository.FuncionarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initDatabase(
            ClienteRepository clienteRepo,
            DocumentoRepository documentoRepo,
            FuncionarioRepository funcionarioRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (funcionarioRepository.findByEmail("admin@contrack.com").isEmpty()) {
                Funcionario admin = Funcionario.builder()
                        .nome("Administrador Contrack")
                        .cpf("123.456.789-09")
                        .email("admin@contrack.com")
                        .senha(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();
                funcionarioRepository.save(admin);
                System.out.println("--- ADMIN PADRAO CRIADO: admin@contrack.com / admin123 ---");
            }

            // 1. Precisamos de um Cliente salvo antes de criar o documento
            Cliente cliente = Cliente.builder()
                    .nome("Empresa Demo Ltda")
                    .cnpj("31.132.821/0001-05")
                    .email("teste@demo.com")
                    .telefone("83988151559")
                    .build();
            
            // Salva o cliente primeiro para gerar o ID
            cliente = clienteRepo.save(cliente); 

            // 2. Criar Documentos usando a FACTORY
            
            // CENÁRIO A: Vence HOJE (Para testar sua notificação "Vence Hoje")
            Documento docVenceHoje = DocumentoFactory.criarDocumento(
                    TipoDocumento.CONTRATO,       // Tipo
                    Prioridade.ALTA,              // Prioridade
                    cliente,                      // Cliente
                    Collections.emptySet(),       // Colaboradores (vazio por enquanto)
                    new BigDecimal("1500.00"),    // Valor
                    LocalDate.now().minusMonths(6), // Assinado 6 meses atrás
                    LocalDate.now()               // VENCIMENTO = HOJE
            );

            // CENÁRIO B: Vence AMANHÃ (Para testar alertas de proximidade)
            Documento docVenceAmanha = DocumentoFactory.criarDocumento(
                    TipoDocumento.LICENCA,
                    Prioridade.MEDIA,
                    cliente,
                    null, // A factory trata null criando HashSet vazio, conforme seu código
                    new BigDecimal("350.00"),
                    LocalDate.now().minusDays(30),
                    LocalDate.now().plusDays(1)   // VENCIMENTO = AMANHÃ
            );

            // CENÁRIO C: Vence em 7 DIAS (Para testar alertas de semana)
            Documento docVenceSemanaQueVem = DocumentoFactory.criarDocumento(
                    TipoDocumento.ALVARA,
                    Prioridade.BAIXA,
                    cliente,
                    null,
                    new BigDecimal("2000.00"),
                    LocalDate.now().minusYears(1),
                    LocalDate.now().plusDays(7)   // VENCIMENTO = DAQUI 7 DIAS
            );

            // 3. Salvar tudo no banco
            documentoRepo.saveAll(Arrays.asList(docVenceHoje, docVenceAmanha, docVenceSemanaQueVem));

            System.out.println("--- BANCO POPULADO VIA FACTORY COM SUCESSO ---");
        };
    }
}
