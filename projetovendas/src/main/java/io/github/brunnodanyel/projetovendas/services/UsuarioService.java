package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.UsuarioResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioService {

    void cadastrarCliente(UsuarioRequestDTO usuarioRequestDTO);

    void addEnderecoCliente(EnderecoRequestDTO enderecoRequestDTO);

    UsuarioResponseDTO buscarId(Long id);

    UsuarioResponseDTO buscarCpf(String cpf);

    UsuarioResponseDTO atualizarCliente(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO);

    UserDetails autenticar(Usuario usuario);

     Usuario usuarioAutenticado();


}
