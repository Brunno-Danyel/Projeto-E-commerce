package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredenciaisRequestDTO {

    @NotEmpty(message = "{campo.email.obrigatorio}")
    @Email(message = "{campo.email.valido}")
    private String email;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    private String senha;
}
