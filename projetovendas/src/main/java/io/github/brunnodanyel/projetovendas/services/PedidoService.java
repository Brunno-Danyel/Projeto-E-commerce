package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;

public interface PedidoService {

    PedidoResponseDTO realizarPedido(PedidoRequestDTO pedidoRequestDTO);
}
