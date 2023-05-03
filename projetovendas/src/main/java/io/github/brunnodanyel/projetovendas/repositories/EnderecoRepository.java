package io.github.brunnodanyel.projetovendas.repositories;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    List<Endereco> findByClienteCpf(String cpf);
}
