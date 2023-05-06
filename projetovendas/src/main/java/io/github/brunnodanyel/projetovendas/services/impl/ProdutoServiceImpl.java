package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.enumeration.CategoriaEnum;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import io.github.brunnodanyel.projetovendas.exception.ProdutoException;
import io.github.brunnodanyel.projetovendas.exception.ProdutoNaoEncontradoException;
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
            throw new ProdutoException("Código de produto existente");
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
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoRepository.findByMarca(marca).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Produto com a marca" + marca + "não foram encontrados");
        }
        return produtoResponseDTOS;
    }

    @Override
    public List<ProdutoResponseDTO> buscarNomeProduto(String nome) {
        List<ProdutoResponseDTO> produtoResponseDTOS =  produtoRepository.findByNome(nome).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Produto " + nome + "não encontrado");
        }
        return produtoResponseDTOS;
    }

    @Override
    public List<ProdutoResponseDTO> buscarDescricaoProduto(String descricao) {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoRepository.findByDescricao(descricao).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Produto " + descricao + "não encontrado");
        }
        return produtoResponseDTOS;
    }

    @Override
    public List<ProdutoResponseDTO> buscarCategoriaProduto(String categoria) {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoRepository.findByCategoria(categoria).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Categoria não encontrada");
        }
        return produtoResponseDTOS;
    }

    @Override
    public List<ProdutoResponseDTO> buscarPrecoProduto(BigDecimal precoInicial, BigDecimal precoFinal) {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoRepository.findByPrecoBetween(precoInicial, precoFinal).stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Não foram encontrados produtos com esssa faixa de preço");
        }
        return produtoResponseDTOS;
    }

    @Override
    public List<ProdutoResponseDTO> listarTodos() {
        List<ProdutoResponseDTO> produtoResponseDTOS = produtoRepository.findAll().stream()
                .map(this::retornaProduto).collect(Collectors.toList());
        if(produtoResponseDTOS.isEmpty()){
            throw new ProdutoNaoEncontradoException("Não existe produtos cadastrados");
        }
        return produtoResponseDTOS;
    }

    @Override
    public ProdutoResponseDTO atualizarProduto(String cod, ProdutoUpdateRequestDTO produtoUpdateRequestDTO) {
        return produtoRepository.findByCodigoDoProduto(cod).map(produto -> {

            String nome = produtoUpdateRequestDTO.getNome().isEmpty() ? produto.getNome() : produtoUpdateRequestDTO.getNome();
            String marca = produtoUpdateRequestDTO.getMarca().isEmpty() ? produto.getMarca() : produtoUpdateRequestDTO.getMarca();
            String descricao = produtoUpdateRequestDTO.getDescricao().isEmpty() ? produto.getDescricao() : produtoUpdateRequestDTO.getDescricao();
            CategoriaEnum categoria = produtoUpdateRequestDTO.getCategoria() == null ? produto.getCategoria() : produtoUpdateRequestDTO.getCategoria();
            BigDecimal preco = produtoUpdateRequestDTO.getPreco() == null ? produto.getPreco() : produtoUpdateRequestDTO.getPreco();

            produto.setNome(nome);
            produto.setMarca(marca);
            produto.setDescricao(descricao);
            produto.setCategoria(categoria);
            produto.setPreco(preco);
            produtoRepository.save(produto);
            return retornaProduto(produto);
        }).orElseThrow(() -> new RuntimeException(""));
    }

    @Override
    public ProdutoResponseDTO addProduto(String cod, ProdutoAddRequestDTO produtoAddRequestDTO) {
        return produtoRepository.findByCodigoDoProduto(cod).map(produto -> {
            produto.setQuantidade(produto.getQuantidade() + produtoAddRequestDTO.getQuantidade());
            produtoRepository.save(produto);
            return retornaProduto(produto);
        }).orElseThrow(() -> new RuntimeException(""));
    }

    private ProdutoResponseDTO retornaProduto(Produto produto) {
        ProdutoResponseDTO produtoResponseDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        return produtoResponseDTO;
    }


    private static Produto converterProdutoRequest(ProdutoRequestDTO produtoRequestDTO) {
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
