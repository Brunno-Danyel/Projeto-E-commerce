package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseAdminDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.util.List;

import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.ADMIN;
import static io.github.brunnodanyel.projetovendas.security.SecurityConfig.USER;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/cadastrar")
    @ResponseStatus(CREATED)
    @PreAuthorize(ADMIN)
    public void cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO) {
        produtoService.cadastrarProduto(produtoRequestDTO);
    }

    @GetMapping("/buscar/cod")
    @PreAuthorize(USER)
    public ProdutoResponseDTO buscarCodigoProduto(@RequestParam String cod) {
        return produtoService.buscarCodigoDoProduto(cod);
    }

    @GetMapping("/buscar/marca")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarMarcaProduto(@RequestParam String marca) {
        return produtoService.buscarMarcaProduto(marca);
    }

    @GetMapping("/buscar/nome")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarNomeProduto(@RequestParam String nome) {
        return produtoService.buscarNomeProduto(nome);
    }

    @GetMapping("/buscar/descricao")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarDescricaoProduto(@RequestParam String descricao) {
        return produtoService.buscarDescricaoProduto(descricao);
    }

    @GetMapping("/buscar/categoria")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarCategoriaProduto(@RequestParam String categoria) {
        return produtoService.buscarCategoriaProduto(categoria);
    }

    @GetMapping("/buscar/preco")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarPrecoProduto(@RequestParam BigDecimal precoInicial,
                                                       @RequestParam BigDecimal precoFinal) {
        return produtoService.buscarPrecoProduto(precoInicial, precoFinal);
    }

    @GetMapping("/buscar/todos")
    @PreAuthorize(USER)
    public List<ProdutoResponseDTO> buscarPrecoProduto() {
        return produtoService.listarTodos();
    }

    @GetMapping("/busca-geral")
    public List<ProdutoResponseDTO> findGeral(Produto produto){
        return produtoService.findGeral(produto);
    }

    @PutMapping("/adicionar-produto/{cod}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize(ADMIN)
    public ProdutoResponseDTO addProduto(@PathVariable String cod, @RequestBody ProdutoAddRequestDTO produtoAddRequestDTO) {
        return produtoService.addProduto(cod, produtoAddRequestDTO);
    }

    @PutMapping("/atualizar/{cod}")
    @ResponseStatus(NO_CONTENT)
    @PreAuthorize(ADMIN)
    public ProdutoResponseAdminDTO atualizarProduto(@PathVariable String cod, @RequestBody ProdutoUpdateRequestDTO produtoUpdateRequestDTO) {
        return produtoService.atualizarProduto(cod, produtoUpdateRequestDTO);
    }

}
