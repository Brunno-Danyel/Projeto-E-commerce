package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ClienteService {

    void cadastrarCliente(ClienteRequestDTO clienteRequestDTO);

    void addEnderecoCliente(EnderecoRequestDTO enderecoRequestDTO);

    ClienteResponseDTO buscarId(Long id);

    ClienteResponseDTO buscarCpf(String cpf);

    ClienteResponseDTO atualizarCliente(ClienteUpdateRequestDTO clienteUpdateRequestDTO);

    UserDetails autenticar(Cliente cliente);

    String retornaCpfClienteAutenticado();


}
