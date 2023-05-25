package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.exception.CepNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.exception.ClienteExistenteException;
import io.github.brunnodanyel.projetovendas.exception.ClienteNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.exception.SenhaIncorretaException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ClienteRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class ClienteServiceImpl implements UserDetailsService, ClienteService {

    public static final int IDADE_MINIMA = 18;
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
        if(clienteRepository.existsByCpf(cliente.getCpf()) || clienteRepository.existsByEmail(cliente.getEmail())){
            throw new ClienteExistenteException("Já existe um cadastro para o e-mail ou documento informado.");
        }
        verificarDataCliente(cliente);
        clienteRepository.save(cliente);
    }

    @Override
    public void addEnderecoCliente(EnderecoRequestDTO enderecoRequestDTO) {
        String cpf = retornaCpfClienteAutenticado();
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        Endereco endereco = converterEnderecoRequest(enderecoRequestDTO);
        if (endereco.getNumero().isEmpty()) {
            endereco.setNumero("S/N");
        }
        try {
            String cep = endereco.getCep();
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            RestTemplate restTemplate = new RestTemplate();
            Endereco enderecoEncontrado = restTemplate.getForObject(url, Endereco.class);
            endereco.setLocalidade(enderecoEncontrado.getLocalidade());
            endereco.setUf(enderecoEncontrado.getUf());
        }catch (RuntimeException e){
            throw new CepNaoEncontradoException("Cep " + endereco.getCep() + " não encontrado");
        }
        endereco.setCliente(cliente);
        cliente.getEnderecos().add(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public ClienteResponseDTO buscarId(Long clienteId) {
        return clienteRepository.findById(clienteId).map(this::retornaCliente)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente " + clienteId + " não encontrado"));

    }

    @Override
    public ClienteResponseDTO buscarCpf(String cpf) {
        return clienteRepository.findByCpf(cpf).map(this::retornaCliente)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente " + cpf + " não encontrado"));
    }

    @Override
    public ClienteResponseDTO atualizarCliente(ClienteUpdateRequestDTO clienteUpdateRequestDTO) {
        String cpf = retornaCpfClienteAutenticado();
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
        }).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente " + cpf + " não encontrado"));
    }

    public String retornaCpfClienteAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Cliente não autenticado.");
        }
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        return cliente.getCpf();
    }

    @Override
    public UserDetails autenticar(Cliente cliente) {
        UserDetails user = loadUserByUsername(cliente.getEmail());
        boolean senhasBatem = passwordEncoder.matches(cliente.getSenha(), user.getPassword());
        if (senhasBatem) {
            return user;
        }
        throw new SenhaIncorretaException("Senha inválida");
    }

    private ClienteResponseDTO retornaCliente(Cliente cliente) {
        ClienteResponseDTO clienteResponseDTO = modelMapper.map(cliente, ClienteResponseDTO.class);
        return clienteResponseDTO;
    }

    private void verificarDataCliente(Cliente cliente){
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataMinima = dataAtual.minusYears(IDADE_MINIMA);
        LocalDate dataNascimento = cliente.getDataNascimento();
        if(dataNascimento.isAfter(dataMinima)){
            throw new RuntimeException("Cliente dever ter pelo menos 18 anos");
        }
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

    private static Endereco converterEnderecoRequest(EnderecoRequestDTO enderecoRequestDTO) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(enderecoRequestDTO.getLogradouro());
        endereco.setNumero(enderecoRequestDTO.getNumero());
        endereco.setBairro(enderecoRequestDTO.getBairro());
        endereco.setIdentificacao(enderecoRequestDTO.getIdentificacao());
        endereco.setComplemento(enderecoRequestDTO.getComplemento());
        endereco.setReferencia(enderecoRequestDTO.getReferencia());
        endereco.setCep(enderecoRequestDTO.getCep());
        return endereco;
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
