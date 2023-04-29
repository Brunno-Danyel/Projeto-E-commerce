package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<String, Cliente> {
}
