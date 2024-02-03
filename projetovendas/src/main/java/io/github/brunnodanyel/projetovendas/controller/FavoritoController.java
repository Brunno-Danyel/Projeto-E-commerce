package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.FavoritoService;
import io.github.brunnodanyel.projetovendas.services.impl.FavoritoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerFavorito(@PathVariable String numeroProduto){
        favoritoService.removerFavorito(numeroProduto);
    }
}
