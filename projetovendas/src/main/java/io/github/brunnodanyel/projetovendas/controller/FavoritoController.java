package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public void addProdutoFavorito(@RequestBody FavoritoRequestDTO favoritoRequestDTO){
        favoritoService.addProdutoFavorito(favoritoRequestDTO);
    }

    @GetMapping
    public List<FavoritoResponseDTO> listarFavoritosCliente(){
        return favoritoService.listarFavoritosCliente();
    }

    @DeleteMapping("remover/{numeroProduto}")
    @ResponseStatus(NO_CONTENT)
    public void removerFavorito(@PathVariable String numeroProduto){
        favoritoService.removerFavorito(numeroProduto);
    }
}
