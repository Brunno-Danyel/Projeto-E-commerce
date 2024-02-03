package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoBuscaResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;

import java.util.List;

public interface PedidoService {

    PedidoResponseDTO realizarPedido(PedidoRequestDTO pedidoRequestDTO);

    List<PedidoBuscaResponseDTO> buscarPedidoCpf();

    void cancelaPedido(Long id);
}
