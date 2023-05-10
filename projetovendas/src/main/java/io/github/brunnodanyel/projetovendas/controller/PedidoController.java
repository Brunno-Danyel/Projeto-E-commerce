package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/realizarPedido")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO realizarPedido(@RequestBody PedidoRequestDTO pedidoRequestDTO){
        PedidoResponseDTO pedido = pedidoService.realizarPedido(pedidoRequestDTO);
        return pedido;
    }

    @GetMapping("buscar/pedido/cpf")
    public List<PedidoResponseDTO> buscarPedidoCpf(){
        List<PedidoResponseDTO> listaPedido = pedidoService.buscarPedidoCpf();
        return listaPedido;
    }
}
