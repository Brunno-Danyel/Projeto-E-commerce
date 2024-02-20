package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Favorito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoritoRepository extends JpaRepository<Favorito, Long> {

    List<Favorito> findByUsuarioCpf(String cpf);


}
