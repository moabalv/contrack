package com.Contrack.config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.model.Notificacao;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.FuncionarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationHelper {

    private final FuncionarioRepository funcionarioRepository;

    public boolean isAdmin(UserDetails userDetails) {
        return userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }

    public Funcionario obterFuncionarioAutenticado(UserDetails userDetails) {
        return funcionarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário autenticado não encontrado"));
    }

    public void verificarAcessoAoDocumento(Documento documento, UserDetails userDetails) {
        if (!isAdmin(userDetails)) {
            Funcionario funcionario = obterFuncionarioAutenticado(userDetails);
            boolean isColaborador = documento.getColaboradores().contains(funcionario);
            if (!isColaborador) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: você não tem acesso a esse documento");
            }
        }
    }

     public void verificarAcessoANotificacao(Notificacao notificacao, UserDetails userDetails) {
        if (!isAdmin(userDetails)) {
            Funcionario funcionario = obterFuncionarioAutenticado(userDetails);
            boolean isColaborador = notificacao.getDocumento() != null &&
                    notificacao.getDocumento().getColaboradores().contains(funcionario);
            if (!isColaborador) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acesso negado: você não tem acesso a essa notificação");
            }
        }
    }
}
