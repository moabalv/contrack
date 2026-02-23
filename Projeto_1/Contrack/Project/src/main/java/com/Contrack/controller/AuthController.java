package com.Contrack.controller;

import com.Contrack.dto.Auth.AlterarSenhaRequestDTO;
import com.Contrack.dto.Auth.LoginRequestDTO;
import com.Contrack.dto.Auth.LoginResponseDTO;
import com.Contrack.dto.Funcionario.FuncionarioRequestDTO;
import com.Contrack.dto.Funcionario.FuncionarioResponseDTO;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.FuncionarioRepository;
import com.Contrack.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioService funcionarioService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getSenha())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Funcionario funcionario = funcionarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));

        return ResponseEntity.ok(LoginResponseDTO.builder()
                .mensagem("Login realizado com sucesso")
                .usuario(new FuncionarioResponseDTO(funcionario))
                .build());
    }

    @PostMapping(value = "/cadastro", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDTO> cadastrar(@Valid @RequestBody FuncionarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionarioService.criar(dto));
    }

    @PatchMapping(value = "/alterar-senha", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> alterarSenha(@Valid @RequestBody AlterarSenhaRequestDTO dto, Authentication authentication) {
        funcionarioService.alterarSenha(authentication.getName(), dto);
        return ResponseEntity.noContent().build();
    }
}
