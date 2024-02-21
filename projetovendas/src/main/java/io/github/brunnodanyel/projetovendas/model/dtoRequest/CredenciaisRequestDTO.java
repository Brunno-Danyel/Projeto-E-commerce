package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


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
