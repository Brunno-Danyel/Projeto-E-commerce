package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping("buscarEndereco/cliente/")
    public List<EnderecoResponseDTO> buscarEnderecoCliente(@RequestParam String cpf){
        List<EnderecoResponseDTO> enderecoResponseDTOS = enderecoService.buscarEnderecoCliente(cpf);
        return enderecoResponseDTOS;
    }

}
