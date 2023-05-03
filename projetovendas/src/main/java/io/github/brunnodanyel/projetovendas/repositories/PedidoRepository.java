package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<String, Pedido> {
}