package io.github.brunnodanyel.projetovendas.services;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;

import java.util.List;

public interface FavoritoService {

    void addProdutoFavorito(FavoritoRequestDTO favoritoRequestDTO);

    List<FavoritoResponseDTO> listarFavoritosCliente();

    void removerFavorito(String numeroProduto);
}
