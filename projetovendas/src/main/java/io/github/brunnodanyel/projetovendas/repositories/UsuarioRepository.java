package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

   Optional <Usuario> findByCpf(String cpf);

   boolean existsByCpfOrEmail(String cpf, String email);

   Optional<Usuario> findByEmail(String email);
}
