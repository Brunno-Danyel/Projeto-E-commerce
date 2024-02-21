package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {

    private List<ItensPedidoRequestDTO> itens;

    private Long idEnderecoEntrega;

    private boolean entrega;

    private boolean retirada;

}
