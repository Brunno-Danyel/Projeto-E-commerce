package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import io.github.brunnodanyel.projetovendas.exception.SenhaIncorretaException;
import io.github.brunnodanyel.projetovendas.exception.ServicoCepException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.UsuarioUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.CredenciaisRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.UsuarioResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.TokenResponseDTO;
import io.github.brunnodanyel.projetovendas.security.JwtService;
import io.github.brunnodanyel.projetovendas.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/clientes")
public class UsuarioController {

    private final PasswordEncoder encoder;

    private final UsuarioService usuarioService;

    private final JwtService jwtService;

    @PostMapping("/cadastrar")
    @ResponseStatus(CREATED)
    public void cadastrarCliente(@RequestBody @Valid UsuarioRequestDTO usuarioRequestDTO) {
        String senhaCriptografada = encoder.encode(usuarioRequestDTO.getSenha());
        usuarioRequestDTO.setSenha(senhaCriptografada);
        usuarioService.cadastrarCliente(usuarioRequestDTO);
    }

    @PostMapping("/adicionarEndereco")
    public void addEnderecoCliente(@RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO) throws ServicoCepException {
        usuarioService.addEnderecoCliente(enderecoRequestDTO);
    }

    @PutMapping("atualizar/cliente")
    @ResponseStatus(NO_CONTENT)
    public UsuarioResponseDTO atualizarCliente(@RequestBody UsuarioUpdateRequestDTO requestDTO) {
        return usuarioService.atualizarCliente(requestDTO);
    }

    @GetMapping("buscar/id/{id}")
    public UsuarioResponseDTO buscarId(@PathVariable Long id) {
        return usuarioService.buscarId(id);
    }

    @GetMapping("buscar/cpf/{cpf}")
    public UsuarioResponseDTO buscarCpf(@RequestParam String cpf) {
        return usuarioService.buscarCpf(cpf);
    }

    @PostMapping("/auth")
    public TokenResponseDTO autenticar(@RequestBody @Valid CredenciaisRequestDTO dto) {
        try {
            Usuario usuario = Usuario.builder().email(dto.getEmail()).senha(dto.getSenha()).build();
            UserDetails usuarioAutenticado = usuarioService.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenResponseDTO(usuario.getEmail(), token);

        } catch (RuntimeException e) {
            throw new SenhaIncorretaException("Senha inválida");
        }
    }

}
