package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
public class ProdutoResponseAdminDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigoDoProduto;

    private String nome;

    private String marca;

    private String descricao;

    @Enumerated(EnumType.STRING)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoriaEnum categoria;

    private BigDecimal preco;

    private Integer quantidade;


    private DisponibilidadeEnum disponibilidade;
}
