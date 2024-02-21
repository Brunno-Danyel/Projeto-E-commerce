package io.github.brunnodanyel.projetovendas.entities;

import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_produto")
    private Long id;

    private String codigoDoProduto;

    private String nome;

    private String marca;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private CategoriaEnum categoria;

    private BigDecimal preco;

    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    private DisponibilidadeEnum disponibilidade;

    @ManyToMany(mappedBy = "produtos")
    private List<Favorito> favoritos;
}
