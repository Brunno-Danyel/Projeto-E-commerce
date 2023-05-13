package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;

import java.util.List;

public interface EnderecoService {

    List<EnderecoResponseDTO> buscarEnderecoCliente();

    EnderecoResponseDTO atualizaEnderecoCliente(Long idEndereco, EnderecoRequestDTO enderecoRequestDTO);
}
