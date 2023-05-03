package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

   Optional <Cliente> findByCpf(String cpf);

   Optional<Cliente> findByEmail(String email);
}
