package com.Contrack.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.ClienteResponseDTO;
import com.Contrack.model.Cliente;
import com.Contrack.repository.ClienteRepository;

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
    public ClienteResponseDTO listarClienteId(Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Cliente não encontrado")); 
        return new ClienteResponseDTO(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listarClientesPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O campo nome é obrigatório");
        }

        List<Cliente> clientesEncontrados = clienteRepository.findByNomeContainingIgnoreCase(nome);

        if (clientesEncontrados.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Nenhum cliente encontrado");
        }

        return clientesEncontrados.stream()
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
