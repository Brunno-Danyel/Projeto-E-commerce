package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
