package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.exception.SenhaIncorretaException;
import io.github.brunnodanyel.projetovendas.exception.ServicoCepException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ClienteUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.CredenciaisRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.TokenResponseDTO;
import io.github.brunnodanyel.projetovendas.security.JwtService;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    private final PasswordEncoder encoder;

    private final ClienteService clienteService;

    private final JwtService jwtService;

    @PostMapping("/cadastrar")
    @ResponseStatus(CREATED)
    public void cadastrarCliente(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        String senhaCriptografada = encoder.encode(clienteRequestDTO.getSenha());
        clienteRequestDTO.setSenha(senhaCriptografada);
        clienteService.cadastrarCliente(clienteRequestDTO);
    }

    @PostMapping("/adicionarEndereco/{id}")
    public void addEnderecoCliente(@RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO) throws ServicoCepException {
        clienteService.addEnderecoCliente(enderecoRequestDTO);
    }

    @PutMapping("atualizar/cliente")
    @ResponseStatus(NO_CONTENT)
    public ClienteResponseDTO atualizarCliente(@RequestBody ClienteUpdateRequestDTO requestDTO) {
        return clienteService.atualizarCliente(requestDTO);
    }

    @GetMapping("buscar/id/{id}")
    public ClienteResponseDTO buscarId(@PathVariable Long id) {
        return clienteService.buscarId(id);
    }

    @GetMapping("buscar/cpf/")
    public ClienteResponseDTO buscarCpf(@RequestParam String cpf) {
        return clienteService.buscarCpf(cpf);
    }

    @PostMapping("/auth")
    public TokenResponseDTO autenticar(@RequestBody @Valid CredenciaisRequestDTO dto) {
        try {
            Cliente cliente = Cliente.builder().email(dto.getEmail()).senha(dto.getSenha()).build();
            UserDetails usuarioAutenticado = clienteService.autenticar(cliente);
            String token = jwtService.gerarToken(cliente);
            return new TokenResponseDTO(cliente.getEmail(), token);

        } catch (RuntimeException e) {
            throw new SenhaIncorretaException("Senha inválida");
        }
    }

}
