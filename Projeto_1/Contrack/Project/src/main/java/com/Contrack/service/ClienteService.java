package com.Contrack.service;

import com.Contrack.dto.ClientePostPutRequestDTO;
import com.Contrack.dto.ClienteResponseDTO;
import java.util.List;

public interface ClienteService {
    List<ClienteResponseDTO> listarClientes();
    ClienteResponseDTO criarCliente(ClientePostPutRequestDTO clienteDTO);
}
