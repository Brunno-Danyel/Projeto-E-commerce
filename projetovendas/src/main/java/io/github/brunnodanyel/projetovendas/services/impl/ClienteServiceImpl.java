package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ClienteRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class ClienteServiceImpl implements UserDetailsService, ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    @Transactional
    public void cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = converterClienteRequest(clienteRequestDTO);
        clienteRepository.save(cliente);
    }

    @Override
    public void addEnderecoCliente(Long clienteId, EnderecoRequestDTO enderecoRequestDTO) {
        Endereco endereco = modelMapper.map(enderecoRequestDTO, Endereco.class);
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponseDTO buscarId(Long clienteId) {
        return clienteRepository.findById(clienteId).map(this::retornaCliente).orElseThrow(() -> new RuntimeException("Erro!"));

    }

    @Override
    public ClienteResponseDTO buscarCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).map(this::retornaCliente).orElseThrow(() -> new RuntimeException("Erro"));
    }

    @Override
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteUpdateRequestDTO clienteUpdateRequestDTO) {
        return clienteRepository.findByCpf(cpf).map(cliente -> {

            String email = clienteUpdateRequestDTO.getEmail().isEmpty() ? cliente.getEmail() : clienteUpdateRequestDTO.getEmail();
            String nomeCompleto = clienteUpdateRequestDTO.getNomeCompleto().isEmpty() ? cliente.getNomeCompleto() : clienteUpdateRequestDTO.getNomeCompleto();
            LocalDate dataNascimento = clienteUpdateRequestDTO.getDataNascimento() == null ? cliente.getDataNascimento() : clienteUpdateRequestDTO.getDataNascimento();
            String telefoneCelular = clienteUpdateRequestDTO.getTelefoneCelular().isEmpty() ? cliente.getTelefoneCelular() : clienteUpdateRequestDTO.getTelefoneCelular();

            cliente.setEmail(email);
            cliente.setNomeCompleto(nomeCompleto);
            cliente.setDataNascimento(dataNascimento);
            cliente.setTelefoneCelular(telefoneCelular);
            clienteRepository.save(cliente);
            return retornaCliente(cliente);
        }).orElseThrow(() -> new RuntimeException(""));
    }

    @Override
    public UserDetails autenticar(Cliente cliente) {
        UserDetails user = loadUserByUsername(cliente.getEmail());
        boolean senhasBatem = passwordEncoder.matches(cliente.getSenha(), user.getPassword());
        if (senhasBatem) {
            return user;
        }
        throw new RuntimeException("Senha invalida");
    }

    private ClienteResponseDTO retornaCliente(Cliente cliente) {
        ClienteResponseDTO clienteResponseDTO = modelMapper.map(cliente, ClienteResponseDTO.class);
        return clienteResponseDTO;
    }

    private static Cliente converterClienteRequest(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = new Cliente();
        cliente.setCpf(clienteRequestDTO.getCpf());
        cliente.setEmail(clienteRequestDTO.getEmail());
        cliente.setDataNascimento(clienteRequestDTO.getDataNascimento());
        cliente.setNomeCompleto(clienteRequestDTO.getNomeCompleto());
        cliente.setTelefoneCelular(clienteRequestDTO.getTelefoneCelular());
        cliente.setSenha(clienteRequestDTO.getSenha());
        return cliente;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new RuntimeException(""));

        String[] roles = cliente.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(cliente.getEmail())
                .password(cliente.getSenha())
                .roles(roles)
                .build();
    }
}
