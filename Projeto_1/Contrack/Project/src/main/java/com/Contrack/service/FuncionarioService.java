package com.Contrack.service;

import com.Contrack.dto.Auth.AlterarSenhaRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;

import java.util.List;

public interface FuncionarioService {
    List<FuncionarioResponseDTO> listar();
    FuncionarioResponseDTO buscar(Long id);
    FuncionarioResponseDTO criar(FuncionarioRequestDTO dto);
    void alterarSenha(String email, AlterarSenhaRequestDTO dto);
    void deletar(Long id);
}
