package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.enumeration.DisponibilidadeEnum;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoAddRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ProdutoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ProdutoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.ProdutoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class ProdutoServiceImpl implements ProdutoService {

    public static final int QUANTIDADE_MINIMA_PARA_DISPONIBILIDADE = 1;
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public void cadastrarProduto(ProdutoRequestDTO produtoRequestDTO) {
        Produto produto = converterProdutoRequest(produtoRequestDTO);
        if(produtoRepository.existsByCodigoDoProduto(produto.getCodigoDoProduto())){
            throw new RuntimeException("");
        }
        produto.setDisponibilidade(produto.getQuantidade() >= QUANTIDADE_MINIMA_PARA_DISPONIBILIDADE ? DisponibilidadeEnum.DISPONIVEL : DisponibilidadeEnum.EM_FALTA);
        produtoRepository.save(produto);
    }

    @Override
    public ProdutoResponseDTO addProduto(Integer cod, ProdutoAddRequestDTO produtoAddRequestDTO) {
        Produto produto = produtoRepository.findByCodigoDoProduto(cod).orElseThrow(() -> new RuntimeException("Erro!"));
        produto.setQuantidade(produtoAddRequestDTO.getQuantidade());
        return retornaProduto(produto);
    }

    private ProdutoResponseDTO retornaProduto(Produto produto){
        ProdutoResponseDTO produtoResponseDTO = modelMapper.map(produto, ProdutoResponseDTO.class);
        return produtoResponseDTO;
    }


    private static Produto converterProdutoRequest(ProdutoRequestDTO produtoRequestDTO){
        Produto produto = new Produto();
        produto.setCodigoDoProduto(produto.getCodigoDoProduto());
        produto.setNome(produtoRequestDTO.getNome());
        produto.setMarca(produtoRequestDTO.getMarca());
        produto.setPreco(produtoRequestDTO.getPreco());
        produto.setCategoria(produtoRequestDTO.getCategoria());
        produto.setQuantidade(produtoRequestDTO.getQuantidade());
        return produto;
    }
}
