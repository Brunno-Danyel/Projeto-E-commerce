package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import io.github.brunnodanyel.projetovendas.entities.Favorito;
import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.exception.EntidadeExistenteException;
import io.github.brunnodanyel.projetovendas.exception.EntidadeNaoEncontrada;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.FavoritoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.FavoritoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.FavoritoRepository;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.UsuarioService;
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
    private UsuarioService usuarioService;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public void addProdutoFavorito(FavoritoRequestDTO favoritoRequestDTO){
        Usuario usuario = usuarioService.usuarioAutenticado();

        Favorito favorito = usuario.getFavorito();

        if (favorito == null) {
            favorito = new Favorito();
            favorito.setUsuario(usuario);
            usuario.setFavorito(favorito);
        }
        String codigoProduto = favoritoRequestDTO.getCodigoProduto();
        Produto produto = produtoRepository.findByCodigoDoProduto(codigoProduto)
                .orElseThrow(() -> new EntidadeNaoEncontrada("Produto não encontrado"));
        List<Produto> produtos = favorito.getProdutos();
        if (produtos.contains(produto)) {
            throw new EntidadeExistenteException("Produto já adicionado aos favoritos");
        }

        produtos.add(produto);
        favoritoRepository.save(favorito);
    }

    public List<FavoritoResponseDTO> listarFavoritosCliente(){
        Usuario usuario = usuarioService.usuarioAutenticado();
         return favoritoRepository.findByUsuarioCpf(usuario.getCpf()).stream()
                 .map(favorito -> modelMapper.map(favorito, FavoritoResponseDTO.class)).collect(Collectors.toList());
    }

    public void removerFavorito(String numeroProduto){
        Usuario usuario = usuarioService.usuarioAutenticado();
        Favorito favorito = usuario.getFavorito();
        List<Produto> produtos = favorito.getProdutos();
        produtos.removeIf(produto -> produto.getCodigoDoProduto().equals(numeroProduto));
        favoritoRepository.save(favorito);

    }


}
