package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;

public interface ProdutoService {

    void cadastrarProduto(ProdutoRequestDTO produtoRequestDTO);

    ProdutoResponseDTO addProduto(Integer cod, ProdutoAddRequestDTO produtoAddRequestDTO);
}
