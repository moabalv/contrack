package com.Contrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Contrack.model.Cliente;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.ClienteResponseDTO;

@Service
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Override
    public List<ClienteResponseDTO> listarClientes() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO criarCliente(ClientePostPutRequestDTO clienteDTO){
        Cliente cliente = Cliente.builder()
                .nome(clienteDTO.getNome())
                .email(clienteDTO.getEmail())
                .telefone(clienteDTO.getTelefone())
                .cnpj(clienteDTO.getCnpj())
                .build();
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return new ClienteResponseDTO(clienteSalvo);
    }
}
