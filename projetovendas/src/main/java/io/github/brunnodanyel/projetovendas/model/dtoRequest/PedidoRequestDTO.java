package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {

    private List<ItensPedidoRequestDTO> itens;

    private Long idEnderecoEntrega;

    private boolean entrega;

    private boolean retirada;

}
