package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.FavoritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.ADMIN;
import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.USER;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("api/favoritos")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    @PreAuthorize(USER)
    public void addProdutoFavorito(@RequestBody FavoritoRequestDTO favoritoRequestDTO){
        favoritoService.addProdutoFavorito(favoritoRequestDTO);
    }

    @GetMapping
    @PreAuthorize(USER)
    public List<FavoritoResponseDTO> listarFavoritosCliente(){
        return favoritoService.listarFavoritosCliente();
    }

    @DeleteMapping("remover/{numeroProduto}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize(USER)
    public void removerFavorito(@PathVariable String numeroProduto){
        favoritoService.removerFavorito(numeroProduto);
    }
}
