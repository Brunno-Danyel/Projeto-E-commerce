package io.github.brunnodanyel.projetovendas.repositories;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<List<Endereco>> findByUsuarioCpf(String cpf);

    Optional<Endereco> findByUsuarioIdAndId(Long idCliente, Long id);


    Optional<List<Endereco>> findByUsuarioId(Long id);
}
