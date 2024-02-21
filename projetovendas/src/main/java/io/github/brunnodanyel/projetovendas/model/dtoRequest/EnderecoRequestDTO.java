package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EnderecoRequestDTO {


    @NotEmpty(message = "{campo.cep.obrigatorio}")
    private String cep;

    @NotEmpty(message = "{campo.identificacao.obrigatorio}")
    private String identificacao;

    @NotEmpty(message = "{campo.logradouro.obrigatorio}")
    private String logradouro;

    private String numero;

    @NotEmpty(message = "{campo.complemento.obrigatorio}")
    private String complemento;

    @NotEmpty(message = "{campo.referencia.obrigatorio}")
    private String referencia;

    @NotEmpty(message = "{campo.bairro.obrigatorio}")
    private String bairro;

    private Usuario usuario;
}
