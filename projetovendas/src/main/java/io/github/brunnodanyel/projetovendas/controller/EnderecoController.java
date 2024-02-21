package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.USER;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("/buscarEndereco/cliente")
    @PreAuthorize(USER)
    public List<EnderecoResponseDTO> buscarEnderecoCliente() {
        return enderecoService.buscarEnderecoCliente();
    }

    @PutMapping("atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize(USER)
    public EnderecoResponseDTO atualizarEnderecoCliente(@PathVariable Long id,
                                                        @RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO) {
        return enderecoService.atualizaEnderecoCliente(id, enderecoRequestDTO);
    }

}
