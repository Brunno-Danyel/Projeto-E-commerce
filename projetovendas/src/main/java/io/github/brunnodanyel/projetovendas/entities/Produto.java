package io.github.brunnodanyel.projetovendas.entities;

import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tb_codigo_produto")
    private String codigoDoProduto;

    @Column(name = "tb_nome_produto")
    private String nome;

    @Column(name = "tb_marca")
    private String marca;

    @Column(name = "tb_descricao")
    private String descricao;

    @Column(name = "tb_categoria")
    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    @Column(name = "tb_preco")
    private BigDecimal preco;

    @Column(name = "tb_quantidade")
    private Integer quantidade;

    @Column(name = "tb_disponibilidade")
    @Enumerated(EnumType.STRING)
    private DisponibilidadeEnum disponibilidade;

    @ManyToMany(mappedBy = "produtos")
    private List<Pedido> pedidos;
}
