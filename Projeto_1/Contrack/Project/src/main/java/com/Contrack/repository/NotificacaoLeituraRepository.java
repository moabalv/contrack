package com.Contrack.repository;

import java.util.List;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Contrack.model.Notificacao;
import com.Contrack.model.NotificacaoLeitura;
import com.Contrack.model.Funcionario.Funcionario;

@Repository
public interface NotificacaoLeituraRepository extends JpaRepository<NotificacaoLeitura, Long> {

    boolean existsByNotificacaoAndFuncionario(Notificacao notificacao, Funcionario funcionario);

    List<NotificacaoLeitura> findByFuncionarioAndNotificacaoIn(Funcionario funcionario, Collection<Notificacao> notificacoes);
}
