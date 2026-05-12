package com.Contrack.service;

import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.ClienteResponseDTO;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface ClienteService {
    List<ClienteResponseDTO> listarClientes(String ordenarPor, UserDetails userDetails);
    ClienteResponseDTO listarClienteId(Long id);
    ClienteResponseDTO criarCliente(ClientePostPutRequestDTO clienteDTO);
    List<ClienteResponseDTO> listarClientesPorNome(String nome);
}
