package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Usuario;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.exception.EntidadeNaoEncontrada;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.EnderecoRepository;
import io.github.brunnodanyel.projetovendas.services.UsuarioService;
import io.github.brunnodanyel.projetovendas.services.EnderecoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoServiceImpl implements EnderecoService {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioService usuarioService;


    @Override
    public List<EnderecoResponseDTO> buscarEnderecoCliente() {
        Usuario usuario = usuarioService.usuarioAutenticado();
        List<Endereco> enderecos = enderecoRepository.findByUsuarioId(usuario.getId())
                .orElseThrow(() -> new EntidadeNaoEncontrada("Cliente não encontrado"));
        if(enderecos.isEmpty()){
            throw new EntidadeNaoEncontrada("Cliente não possui endereços");
        }
        return enderecos.stream()
                .map(endereco -> modelMapper.map(endereco, EnderecoResponseDTO.class)).collect(Collectors.toList());
    }

    public EnderecoResponseDTO atualizaEnderecoCliente(Long idEndereco, EnderecoRequestDTO enderecoRequestDTO) {
        Usuario usuario = usuarioService.usuarioAutenticado();
        return enderecoRepository.findByUsuarioIdAndId(usuario.getId(), idEndereco).map(endereco -> {
                    Endereco enderecoAt = enderecoRepository.findById(idEndereco)
                            .orElseThrow(() -> new EntidadeNaoEncontrada("Endereço não encontrado"));

                    enderecoAt.setBairro(enderecoRequestDTO.getBairro());
                    enderecoAt.setCep(enderecoRequestDTO.getCep());
                    enderecoAt.setIdentificacao(enderecoRequestDTO.getIdentificacao());
                    enderecoAt.setLogradouro(enderecoRequestDTO.getLogradouro());
                    enderecoAt.setNumero(enderecoRequestDTO.getNumero());
                    enderecoAt.setComplemento(enderecoRequestDTO.getComplemento());
                    enderecoAt.setReferencia(enderecoRequestDTO.getReferencia());

                    enderecoRepository.save(enderecoAt);
                    return modelMapper.map(enderecoAt, EnderecoResponseDTO.class);
                }).orElseThrow(() -> new EntidadeNaoEncontrada("Cliente não encontrado"));
    }


}
