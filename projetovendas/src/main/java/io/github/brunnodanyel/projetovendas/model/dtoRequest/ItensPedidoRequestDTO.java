package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItensPedidoRequestDTO {

    @NotEmpty(message = "{campo.numeroProduto.obrigatorio}")
    private String numeroProduto;

    @NotNull(message = "{campo.quantidade.obrigatorio}")
    private Integer quantidade;
}
