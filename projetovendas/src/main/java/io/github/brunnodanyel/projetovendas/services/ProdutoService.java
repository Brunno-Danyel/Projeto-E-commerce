package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseAdminDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

public interface ProdutoService {

    void cadastrarProduto(ProdutoRequestDTO produtoRequestDTO);

    ProdutoResponseDTO buscarCodigoDoProduto(String cod);

    List<ProdutoResponseDTO> buscarMarcaProduto(String marca);

    List<ProdutoResponseDTO> buscarNomeProduto(String nome);

    List<ProdutoResponseDTO> buscarDescricaoProduto(String descricao);

    List<ProdutoResponseDTO> buscarCategoriaProduto(String categoria);

    List<ProdutoResponseDTO> buscarPrecoProduto(BigDecimal precoInicial, BigDecimal precoFinal);

    List<ProdutoResponseDTO> listarTodos();

    ProdutoResponseAdminDTO atualizarProduto(String cod, ProdutoUpdateRequestDTO produtoUpdateRequestDTO);

    ProdutoResponseDTO addProduto(String cod, ProdutoAddRequestDTO produtoAddRequestDTO);

    List<ProdutoResponseDTO> findGeral(Produto produto);
}
