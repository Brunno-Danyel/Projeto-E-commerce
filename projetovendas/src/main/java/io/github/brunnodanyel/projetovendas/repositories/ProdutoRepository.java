package io.github.brunnodanyel.projetovendas.repositories;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigoDoProduto(String cod);

    Optional<Produto> findByCodigoDoProduto(String cod);

    @Query(value = "SELECT * FROM Produto p WHERE p.tb_marca like %:marca%", nativeQuery = true)
    List<Produto> findByMarca(@Param("marca") String marca);

    @Query(value = "SELECT * FROM Produto p WHERE p.tb_nome_produto like %:nome%", nativeQuery = true)
    List<Produto> findByNome(@Param("nome") String nome);

    @Query(value = "SELECT * FROM Produto p WHERE p.tb_descricao like %:descricao%", nativeQuery = true)
    List<Produto> findByDescricao(@Param("descricao") String descricao);

    @Query(value = "SELECT * FROM Produto p WHERE p.tb_categoria like %:categoria%", nativeQuery = true)
    List<Produto> findByCategoria(@Param("categoria") String categoria);

    List<Produto> findByPrecoBetween(BigDecimal precoInicial, BigDecimal precoFinal);



}
