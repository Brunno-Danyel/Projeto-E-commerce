package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class EnderecoResponseDTO {

    private String cep;

    private String identificação;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String referencia;

    private String bairro;

    private String cidade;

    private String uf;
}
