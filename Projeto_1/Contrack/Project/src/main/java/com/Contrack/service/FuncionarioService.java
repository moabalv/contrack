package com.Contrack.service;

import com.Contrack.dto.Funcionario.FuncionarioRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;

import java.util.List;

public interface FuncionarioService {
    List<FuncionarioResponseDTO> listar();
    FuncionarioResponseDTO buscar(Long id);
    FuncionarioResponseDTO criar(FuncionarioRequestDTO dto);
    void deletar(Long id);
}
