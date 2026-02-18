package com.Contrack.service;

import com.Contrack.dto.Funcionario.FuncionarioRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;
import com.Contrack.enums.Role;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class FuncionarioServiceImpl implements FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listar() {
        return funcionarioRepository.findAll()
                .stream()
                .map(FuncionarioResponseDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscar(Long id) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
        return new FuncionarioResponseDTO(funcionario);
    }

    @Override
    @Transactional
    public FuncionarioResponseDTO criar(FuncionarioRequestDTO dto) {
        funcionarioRepository.findByEmail(dto.getEmail()).ifPresent(existente -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe usuário com esse e-mail");
        });

        Funcionario funcionario = Funcionario.builder()
                .nome(dto.getNome())
                .cpf(dto.getCpf())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(resolverRole(dto.getRole()))
                .build();

        return new FuncionarioResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!funcionarioRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado");
        }
        funcionarioRepository.deleteById(id);
    }

    private Role resolverRole(String role) {
        if (role == null || role.isBlank()) {
            return Role.FUNCIONARIO;
        }

        try {
            return Role.valueOf(role.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role inválida. Use ADMIN ou FUNCIONARIO");
        }
    }
}
