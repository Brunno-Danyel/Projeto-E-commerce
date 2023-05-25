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
        ProdutoResponseDTO produto = produtoService.buscarCodigoDoProduto(cod);
        return produto;
    }

    @GetMapping("/buscar/marca")
    public List<ProdutoResponseDTO> buscarMarcaProduto(@RequestParam String marca) {
        List<ProdutoResponseDTO> listaProduto = produtoService.buscarMarcaProduto(marca);
        return listaProduto;
    }

    @GetMapping("/buscar/nome")
    public List<ProdutoResponseDTO> buscarNomeProduto(@RequestParam String nome) {
        List<ProdutoResponseDTO> listaProduto = produtoService.buscarNomeProduto(nome);
        return listaProduto;
    }

    @GetMapping("/buscar/descricao")
    public List<ProdutoResponseDTO> buscarDescricaoProduto(@RequestParam String descricao) {
        List<ProdutoResponseDTO> listaProduto = produtoService.buscarDescricaoProduto(descricao);
        return listaProduto;
    }

    @GetMapping("/buscar/categoria")
    public List<ProdutoResponseDTO> buscarCategoriaProduto(@RequestParam String categoria) {
        List<ProdutoResponseDTO> listaProduto = produtoService.buscarCategoriaProduto(categoria);
        return listaProduto;
    }

    @GetMapping("/buscar/preco")
    public List<ProdutoResponseDTO> buscarPrecoProduto(@RequestParam BigDecimal precoInicial,
                                                       @RequestParam BigDecimal precoFinal) {
        List<ProdutoResponseDTO> listaProduto = produtoService.buscarPrecoProduto(precoInicial, precoFinal);
        return listaProduto;
    }

    @GetMapping("/buscar/todos")
    public List<ProdutoResponseDTO> buscarPrecoProduto() {
        List<ProdutoResponseDTO> listaProduto = produtoService.listarTodos();
        return listaProduto;
    }

    @PutMapping("/adicionar-produto/{cod}")
    public ProdutoResponseDTO addProduto(@PathVariable String cod, @RequestBody ProdutoAddRequestDTO produtoAddRequestDTO) {
        ProdutoResponseDTO produtoResponseDTO = produtoService.addProduto(cod, produtoAddRequestDTO);
        return produtoResponseDTO;
    }

    @PutMapping("/atualizar/{cod}")
    public ProdutoResponseAdminDTO atualizarProduto(@PathVariable String cod, @RequestBody ProdutoUpdateRequestDTO produtoUpdateRequestDTO) {
        ProdutoResponseAdminDTO produtoAtualizado = produtoService.atualizarProduto(cod, produtoUpdateRequestDTO);
        return produtoAtualizado;
    }

}
