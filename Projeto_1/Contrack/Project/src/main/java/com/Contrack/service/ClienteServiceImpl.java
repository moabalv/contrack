package com.Contrack.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.Contrack.config.AuthenticationHelper;
import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.ClienteResponseDTO;
import com.Contrack.model.Cliente;
import com.Contrack.model.Documento.Documento;
import com.Contrack.model.Funcionario.Funcionario;
import com.Contrack.repository.ClienteRepository;
import com.Contrack.repository.DocumentoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    DocumentoRepository documentoRepository;
    
    private final AuthenticationHelper authHelper;

    @Override
    public List<ClienteResponseDTO> listarClientes(String ordenarPor, UserDetails userDetails) {

        List<Cliente> clientes;

        if (authHelper.isAdmin(userDetails)) {
            clientes = clienteRepository.findAll();
        } else {
            Funcionario funcionario = authHelper.obterFuncionarioAutenticado(userDetails);
            clientes = documentoRepository.findByColaboradoresContaining(funcionario)
                    .stream()
                    .map(Documento::getCliente)
                    .distinct()
                    .collect(Collectors.toList());
        }


        if (ordenarPor != null && ordenarPor.equalsIgnoreCase("nome")) {
            clientes = clientes.stream()
                    .sorted(Comparator.comparing(Cliente::getNome))
                    .collect(Collectors.toList());
        }

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
