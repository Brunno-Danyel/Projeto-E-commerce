package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoClienteResponseDTO {

    private String cpf;

    private String nomeCompleto;

    private String telefoneCelular;

    private String email;

}
