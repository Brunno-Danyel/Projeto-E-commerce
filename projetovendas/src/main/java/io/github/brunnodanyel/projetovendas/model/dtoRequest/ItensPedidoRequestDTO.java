package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItensPedidoRequestDTO {

    @NotEmpty(message = "{campo.numeroProduto.obrigatorio}")
    private String numeroProduto;

    @NotNull(message = "{campo.quantidade.obrigatorio}")
    private Integer quantidade;
}
