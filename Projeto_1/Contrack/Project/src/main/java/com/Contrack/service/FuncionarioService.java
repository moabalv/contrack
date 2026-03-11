package com.Contrack.service;

import com.Contrack.dto.Auth.AlterarSenhaRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;
import com.Contrack.dto.Funcionario.FuncionarioUpdateNomeDTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FuncionarioService {
    List<FuncionarioResponseDTO> listar();
    FuncionarioResponseDTO buscar(Long id);
    FuncionarioResponseDTO criar(FuncionarioRequestDTO dto);
    void alterarSenha(String email, AlterarSenhaRequestDTO dto);
    void deletar(Long id);
    FuncionarioResponseDTO atualizarNome(Long id, FuncionarioUpdateNomeDTO dto);
    void salvarFoto(Long id, MultipartFile file);
    byte[] buscarFoto(Long id);
}
