package io.github.brunnodanyel.projetovendas.entities;


import io.github.brunnodanyel.projetovendas.enumeration.PagamentoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tb_numero_pedido")
    private String numeroPedido;

    @Column(name = "tb_data_pedido")
    private LocalDate dataDoPedido;

    @Column(name = "tb_status_pedido")
    private StatusPedidoEnum statusPedido;

    @Column(name = "tb_pagamento")
    private PagamentoEnum pagamentoEnum;

    @ManyToOne
    @JoinColumn(name = "cliente_tb_cpf")
    private Cliente cliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

    private BigDecimal total;
}
