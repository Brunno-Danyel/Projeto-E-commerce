package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import lombok.*;

@Getter
@Setter
public class EnderecoRequestDTO {


    private String cep;

    private String identificação;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String referencia;

    private String bairro;

    private String cidade;

    private String uf;

    private Cliente cliente;
}
