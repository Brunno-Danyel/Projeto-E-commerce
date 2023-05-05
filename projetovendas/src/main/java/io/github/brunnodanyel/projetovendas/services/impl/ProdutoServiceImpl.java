package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoUpdateRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    public static final int QUANTIDADE_MINIMA_PARA_DISPONIBILIDADE = 1;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void cadastrarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = converterProdutoRequest(produtoRequestDTO);
        if (produtoRepository.existsByCodigoDoProduto(produto.getCodigoDoProduto())) {
            throw new RuntimeException("");
        }
        produto.setDisponibilidade(produto.getQuantidade() >= QUANTIDADE_MINIMA_PARA_DISPONIBILIDADE ? DisponibilidadeEnum.DISPONIVEL : DisponibilidadeEnum.EM_FALTA);
        produtoRepository.save(produto);
    }

    @Override
    public ProdutoResponseDTO buscarCodigoDoProduto(String cod) {
        return produtoRepository.findByCodigoDoProduto(cod).map(this::retornaProduto)
                .orElseThrow(() -> new RuntimeException(""));
    }

    @Override
    public List<ProdutoResponseDTO> buscarMarcaProduto(String marca) {
        return produtoRepository.findByMarca(marca).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> buscarNomeProduto(String nome) {
        return produtoRepository.findByNome(nome).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> buscarDescricaoProduto(String descricao) {
        return produtoRepository.findByDescricao(descricao).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> buscarCategoriaProduto(String categoria) {
        return produtoRepository.findByCategoria(categoria).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> buscarPrecoProduto(BigDecimal precoInicial, BigDecimal precoFinal) {
        return produtoRepository.findByPrecoBetween(precoInicial, precoFinal).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::retornaProduto).collect(Collectors.toList());
    }

    @Override
    public ProdutoResponseDTO addProduto(String cod, ProdutoAddRequestDTO produtoAddRequestDTO) {
        return produtoRepository.findByCodigoDoProduto(cod).map(produto -> {
            produto.setQuantidade(produto.getQuantidade() + produtoAddRequestDTO.getQuantidade());
            produtoRepository.save(produto);
           return retornaProduto(produto);
        }).orElseThrow(() -> new RuntimeException(""));
    }

    private ProdutoResponseDTO retornaProduto(Produto produto){
        ProdutoResponseDTO produtoResponseDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        return produtoResponseDTO;
    }


    private static Produto converterProdutoRequest(ProdutoRequestDTO produtoRequestDTO){
        Produto produto = new Produto();
        produto.setCodigoDoProduto(produtoRequestDTO.getCodigoDoProduto());
        produto.setDescricao(produtoRequestDTO.getDescricao());
        produto.setNome(produtoRequestDTO.getNome());
        produto.setMarca(produtoRequestDTO.getMarca());
        produto.setPreco(produtoRequestDTO.getPreco());
        produto.setCategoria(produtoRequestDTO.getCategoria());
        produto.setQuantidade(produtoRequestDTO.getQuantidade());
        return produto;
    }
}
