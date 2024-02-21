package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoBuscaResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.USER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/realizarPedido")
    @ResponseStatus(CREATED)
    @PreAuthorize(USER)
    public PedidoResponseDTO realizarPedido(@RequestBody @Valid PedidoRequestDTO pedidoRequestDTO){
        return pedidoService.realizarPedido(pedidoRequestDTO);
    }

    @GetMapping("buscar/pedido/cpf")
    @PreAuthorize(USER)
    public List<PedidoBuscaResponseDTO> buscarPedidoCpf(){
        return pedidoService.buscarPedidoCpf();
    }

    @PutMapping("/cancela/{idPedido}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize(USER)
    public void cancelaPedido(Long idPedido) {
        pedidoService.cancelaPedido(idPedido);
    }
}
