package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoRequestDTO {

    @NotEmpty(message = "{campo.codigoProduto.obrigatorio}")
    private String codigoDoProduto;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @NotEmpty(message = "{campo.marca.obrigatorio}")
    private String marca;

    @NotEmpty(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private CategoriaEnum categoria;

    @NotNull(message = "{campo.preco.obrigatorio}")
    private BigDecimal preco;

    @NotNull(message = "{campo.quantidadeProduto.obrigatorio}")
    private Integer quantidade;

}
