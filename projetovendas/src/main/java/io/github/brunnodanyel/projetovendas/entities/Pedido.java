package io.github.brunnodanyel.projetovendas.entities;


import io.github.brunnodanyel.projetovendas.enumeration.PagamentoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @Column(name = "tb_numero_pedido")
    private String numeroPedido;

    @Column(name = "tb_data_pedido")
    private Date dataDoPedido;

    @Column(name = "tb_status_pedido")
    private StatusPedidoEnum statusPedido;

    @Column(name = "tb_pagamento")
    private PagamentoEnum pagamentoEnum;

    @ManyToOne
    @JoinColumn(name = "cliente_tb_cpf")
    private Cliente cliente;
}
