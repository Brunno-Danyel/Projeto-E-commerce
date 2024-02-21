package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.exception.EntidadeExistenteException;
import io.github.brunnodanyel.projetovendas.exception.EntidadeNaoEncontrada;
import io.github.brunnodanyel.projetovendas.exception.IdadeException;
import io.github.brunnodanyel.projetovendas.exception.SenhaIncorretaException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.UsuarioResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.UsuarioRepository;
import io.github.brunnodanyel.projetovendas.services.UsuarioService;
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

import java.time.LocalDate;

@Service
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {

    public static final int IDADE_MINIMA = 18;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public void cadastrarCliente(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = converterUsuarioRequest(usuarioRequestDTO);
        if(usuarioRepository.existsByCpfOrEmail(usuario.getCpf(), usuario.getEmail())){
            throw new EntidadeExistenteException("Já existe um cadastro para o e-mail ou documento informado.");
        }
        verificarDataCliente(usuario);
        usuarioRepository.save(usuario);
    }

    @Override
    public void addEnderecoCliente(EnderecoRequestDTO enderecoRequestDTO) {
        Usuario usuario = usuarioAutenticado();
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
        }catch (EntidadeNaoEncontrada e){
            throw new EntidadeNaoEncontrada("Cep " + endereco.getCep() + " não encontrado");
        }
        endereco.setUsuario(usuario);
        usuario.getEnderecos().add(endereco);
        usuarioRepository.save(usuario);
    }

    @Override
    public UsuarioResponseDTO buscarId(Long clienteId) {
        return usuarioRepository.findById(clienteId).map(this::retornaCliente)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Cliente " + clienteId + " não encontrado"));

    }

    @Override
    public UsuarioResponseDTO buscarCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf).map(this::retornaCliente)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Cliente " + cpf + " não encontrado"));
    }

    @Override
    public UsuarioResponseDTO atualizarCliente(UsuarioUpdateRequestDTO usuarioUpdateRequestDTO) {
            Usuario usuario = usuarioAutenticado();
            String email = usuarioUpdateRequestDTO.getEmail().isEmpty() ? usuario.getEmail() : usuarioUpdateRequestDTO.getEmail();
            String nomeCompleto = usuarioUpdateRequestDTO.getNomeCompleto().isEmpty() ? usuario.getNomeCompleto() : usuarioUpdateRequestDTO.getNomeCompleto();
            LocalDate dataNascimento = usuarioUpdateRequestDTO.getDataNascimento() == null ? usuario.getDataNascimento() : usuarioUpdateRequestDTO.getDataNascimento();
            String telefoneCelular = usuarioUpdateRequestDTO.getTelefoneCelular().isEmpty() ? usuario.getTelefoneCelular() : usuarioUpdateRequestDTO.getTelefoneCelular();

            usuario.setEmail(email);
            usuario.setNomeCompleto(nomeCompleto);
            usuario.setDataNascimento(dataNascimento);
            usuario.setTelefoneCelular(telefoneCelular);
            usuarioRepository.save(usuario);
            return retornaCliente(usuario);
    }

    public Usuario usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Cliente não autenticado.");
        }
        String email = authentication.getName();
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new EntidadeNaoEncontrada("Cliente não encontrado"));
    }

    @Override
    public UserDetails autenticar(Usuario usuario) {
        UserDetails user = loadUserByUsername(usuario.getEmail());
        boolean senhasBatem = passwordEncoder.matches(usuario.getSenha(), user.getPassword());
        if (senhasBatem) {
            return user;
        }
        throw new SenhaIncorretaException("Senha inválida");
    }

    private UsuarioResponseDTO retornaCliente(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioResponseDTO.class);
    }

    private void verificarDataCliente(Usuario usuario){
        LocalDate dataAtual = LocalDate.now();
        LocalDate dataMinima = dataAtual.minusYears(IDADE_MINIMA);
        LocalDate dataNascimento = usuario.getDataNascimento();
        if(dataNascimento.isAfter(dataMinima)){
            throw new IdadeException("Cliente dever ter pelo menos 18 anos");
        }
    }

    private static Usuario converterUsuarioRequest(UsuarioRequestDTO usuarioRequestDTO) {
        Usuario usuario = new Usuario();
        usuario.setCpf(usuarioRequestDTO.getCpf());
        usuario.setEmail(usuarioRequestDTO.getEmail());
        usuario.setDataNascimento(usuarioRequestDTO.getDataNascimento());
        usuario.setNomeCompleto(usuarioRequestDTO.getNomeCompleto());
        usuario.setTelefoneCelular(usuarioRequestDTO.getTelefoneCelular());
        usuario.setSenha(usuarioRequestDTO.getSenha());
        if(usuarioRequestDTO.isAdmin()) {
            usuario.setAdmin(true);
        }
        return usuario;
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
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Usuário não encontrado"));

        String[] roles = usuario.isAdmin() ? new String[]{"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
