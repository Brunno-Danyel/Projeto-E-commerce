package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("buscarEndereco/cliente/")
    public List<EnderecoResponseDTO> buscarEnderecoCliente() {
        return enderecoService.buscarEnderecoCliente();
    }

    @PutMapping("atualizar/{id}")
    @ResponseStatus(NO_CONTENT)
    public EnderecoResponseDTO atualizarEnderecoCliente(@PathVariable Long id,
                                                        @RequestBody @Valid EnderecoRequestDTO enderecoRequestDTO) {
        return enderecoService.atualizaEnderecoCliente(id, enderecoRequestDTO);
    }

}
