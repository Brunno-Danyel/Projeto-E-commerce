package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import lombok.*;

@Getter
@Setter
public class EnderecoResponseDTO {

    private Long id;

    private String cep;

    private String identificacao;

    private String logradouro;

    private Integer numero;

    private String complemento;

    private String referencia;

    private String bairro;

    private String cidade;

    private String uf;
}
