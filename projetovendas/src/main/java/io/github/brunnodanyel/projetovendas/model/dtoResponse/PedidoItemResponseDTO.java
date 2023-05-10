package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PedidoItemResponseDTO {

    private ProdutoResponseDTO produto;

    private Integer quantidade;

    private BigDecimal totalProduto;
}
