package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Getter
@Setter
public class PedidoRequestDTO {

    private List<ItensPedidoRequestDTO> itens;

    @Enumerated(EnumType.STRING)
    private TipoEntregaEnum tipoEntrega;

    private Long idEnderecoEntrega;


}
