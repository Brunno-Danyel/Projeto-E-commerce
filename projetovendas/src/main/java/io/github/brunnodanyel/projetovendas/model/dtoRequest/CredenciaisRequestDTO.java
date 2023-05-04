package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CredenciaisRequestDTO {

    private String email;

    private String senha;
}
