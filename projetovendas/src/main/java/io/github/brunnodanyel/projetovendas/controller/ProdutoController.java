package io.github.brunnodanyel.projetovendas.controller;

import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseAdminDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;
import io.github.brunnodanyel.projetovendas.services.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping("/cadastrar")
    public void cadastrarProduto(@RequestBody @Valid ProdutoRequestDTO produtoRequestDTO) {
        produtoService.cadastrarProduto(produtoRequestDTO);
    }

    @GetMapping("/buscar/cod")
    public ProdutoResponseDTO buscarCodigoProduto(@RequestParam String cod) {
        return produtoService.buscarCodigoDoProduto(cod);
    }

    @GetMapping("/buscar/marca")
    public List<ProdutoResponseDTO> buscarMarcaProduto(@RequestParam String marca) {
        return produtoService.buscarMarcaProduto(marca);
    }

    @GetMapping("/buscar/nome")
    public List<ProdutoResponseDTO> buscarNomeProduto(@RequestParam String nome) {
        return produtoService.buscarNomeProduto(nome);
    }

    @GetMapping("/buscar/descricao")
    public List<ProdutoResponseDTO> buscarDescricaoProduto(@RequestParam String descricao) {
        return produtoService.buscarDescricaoProduto(descricao);
    }

    @GetMapping("/buscar/categoria")
    public List<ProdutoResponseDTO> buscarCategoriaProduto(@RequestParam String categoria) {
        return produtoService.buscarCategoriaProduto(categoria);
    }

    @GetMapping("/buscar/preco")
    public List<ProdutoResponseDTO> buscarPrecoProduto(@RequestParam BigDecimal precoInicial,
                                                       @RequestParam BigDecimal precoFinal) {
        return produtoService.buscarPrecoProduto(precoInicial, precoFinal);
    }

    @GetMapping("/buscar/todos")
    public List<ProdutoResponseDTO> buscarPrecoProduto() {
        return produtoService.listarTodos();
    }

    @PutMapping("/adicionar-produto/{cod}")
    public ProdutoResponseDTO addProduto(@PathVariable String cod, @RequestBody ProdutoAddRequestDTO produtoAddRequestDTO) {
        return produtoService.addProduto(cod, produtoAddRequestDTO);
    }

    @PutMapping("/atualizar/{cod}")
    public ProdutoResponseAdminDTO atualizarProduto(@PathVariable String cod, @RequestBody ProdutoUpdateRequestDTO produtoUpdateRequestDTO) {
        return produtoService.atualizarProduto(cod, produtoUpdateRequestDTO);
    }

}
