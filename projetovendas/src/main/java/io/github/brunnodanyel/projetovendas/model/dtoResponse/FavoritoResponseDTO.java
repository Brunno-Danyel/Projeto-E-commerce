package io.github.brunnodanyel.projetovendas.model.dtoResponse;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FavoritoResponseDTO {

    private List<ProdutoResponseDTO> produtos;
}
