package io.github.brunnodanyel.projetovendas.model.dtoRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.brunnodanyel.projetovendas.validation.anotacao.ValidPassword;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class ClienteRequestDTO {

    @NotEmpty(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    private String cpf;

    @NotEmpty(message = "{campo.nomeCompleto.obrigatorio}")
    private String nomeCompleto;

    @NotNull(message = "{campo.dataNascimento.obrigatorio}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDate dataNascimento;

    @NotEmpty(message = "{campo.telefoneCelular.obrigatorio}")
    private String telefoneCelular;

    @NotEmpty(message = "{campo.email.obrigatorio}")
    @Email(message = "{campo.email.valido}")
    private String email;

    @NotEmpty(message = "{campo.senha.obrigatorio}")
    @ValidPassword
    private String senha;


}
