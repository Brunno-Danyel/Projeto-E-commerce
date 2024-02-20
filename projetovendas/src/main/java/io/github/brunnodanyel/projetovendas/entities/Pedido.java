package io.github.brunnodanyel.projetovendas.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.brunnodanyel.projetovendas.enumeration.PagamentoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Long id;

    private String numeroPedido;

    private LocalDate dataDoPedido;

    private StatusPedidoEnum statusPedido;

    private PagamentoEnum pagamentoEnum;

    @ManyToOne
    @JoinColumn(name = "cliente_tb_cpf")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    private TipoEntregaEnum tipoEntrega;

    @ManyToOne
    @JoinColumn(name = "id_endereco")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Endereco enderecoEntrega;

    private BigDecimal totalPedido;
}
