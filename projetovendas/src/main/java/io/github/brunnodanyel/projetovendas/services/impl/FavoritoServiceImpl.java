package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.Favorito;
import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.exception.ClienteNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.exception.ProdutoExistenteFavoritoException;
import io.github.brunnodanyel.projetovendas.exception.ProdutoNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ClienteRepository;
import io.github.brunnodanyel.projetovendas.repositories.FavoritoRepository;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import io.github.brunnodanyel.projetovendas.services.FavoritoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoritoServiceImpl implements FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void addProdutoFavorito(FavoritoRequestDTO favoritoRequestDTO){
        Cliente cliente = clienteService.usuarioAutenticado();

        Favorito favorito = cliente.getFavorito();

        if (favorito == null) {
            favorito = new Favorito();
            favorito.setCliente(cliente);
            cliente.setFavorito(favorito);
        }
        String codigoProduto = favoritoRequestDTO.getCodigoProduto();
        Produto produto = produtoRepository.findByCodigoDoProduto(codigoProduto)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado"));
        List<Produto> produtos = favorito.getProdutos();
        if (produtos.contains(produto)) {
            throw new ProdutoExistenteFavoritoException("Produto já adicionado aos favoritos");
        }

        produtos.add(produto);
        favoritoRepository.save(favorito);
    }

    public List<FavoritoResponseDTO> listarFavoritosCliente(){
        Cliente cliente = clienteService.usuarioAutenticado();
         return favoritoRepository.findByClienteCpf(cliente.getCpf()).stream()
                 .map(favorito -> modelMapper.map(favorito, FavoritoResponseDTO.class)).collect(Collectors.toList());
    }

    public void removerFavorito(String numeroProduto){
        Cliente cliente = clienteService.usuarioAutenticado();
        Favorito favorito = cliente.getFavorito();
        List<Produto> produtos = favorito.getProdutos();
        produtos.removeIf(produto -> produto.getCodigoDoProduto().equals(numeroProduto));
        favoritoRepository.save(favorito);

    }


}
