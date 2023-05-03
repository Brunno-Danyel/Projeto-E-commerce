package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;

import java.util.List;

public interface EnderecoService {


//    void cadastrarEndereco(EnderecoRequestDTO enderecoRequestDTO, String cpf);

    List<EnderecoResponseDTO> buscarEnderecoCliente(String cpf);
}
