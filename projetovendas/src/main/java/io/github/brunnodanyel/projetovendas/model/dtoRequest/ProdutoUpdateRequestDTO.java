package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoUpdateRequestDTO {

    private String nome;

    private String marca;

    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoriaEnum categoria;

    private BigDecimal preco;
}
