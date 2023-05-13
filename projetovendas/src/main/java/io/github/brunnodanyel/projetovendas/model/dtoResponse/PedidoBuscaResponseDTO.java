package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.brunnodanyel.projetovendas.enumeration.PagamentoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PedidoBuscaResponseDTO {

    private String numeroPedido;

    private LocalDate dataDoPedido;

    private StatusPedidoEnum statusPedido;

    private PagamentoEnum pagamentoEnum;

    private TipoEntregaEnum tipoEntrega;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EnderecoResponseDTO enderecoEntrega;

    private List<PedidoItemResponseDTO> itens;

    private BigDecimal total;
}
