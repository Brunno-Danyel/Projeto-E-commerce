package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.exception.ClienteNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.exception.EnderecoNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.EnderecoRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
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
    private ClienteService clienteService;


    @Override
    public List<EnderecoResponseDTO> buscarEnderecoCliente() {
        String cpf = clienteService.retornaCpfClienteAutenticado();
        List<Endereco> enderecos = enderecoRepository.findByClienteCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        if(enderecos.isEmpty()){
            throw new EnderecoNaoEncontradoException("Cliente não possui endereços");
        }
        return enderecos.stream()
                .map(endereco -> modelMapper.map(endereco, EnderecoResponseDTO.class)).collect(Collectors.toList());
    }

    public EnderecoResponseDTO atualizaEnderecoCliente(Long idEndereco, EnderecoRequestDTO enderecoRequestDTO) {
        String cpf = clienteService.retornaCpfClienteAutenticado();
        EnderecoResponseDTO enderecoResponseDTO = enderecoRepository.findByClienteCpfAndId(cpf, idEndereco).map(endereco -> {
            Endereco enderecoAt = enderecoRepository.findById(idEndereco)
                    .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço não encontrado"));

            enderecoAt.setBairro(enderecoRequestDTO.getBairro());
            enderecoAt.setCep(enderecoRequestDTO.getCep());
            enderecoAt.setIdentificacao(enderecoRequestDTO.getIdentificacao());
            enderecoAt.setLogradouro(enderecoRequestDTO.getLogradouro());
            enderecoAt.setNumero(enderecoRequestDTO.getNumero());
            enderecoAt.setComplemento(enderecoRequestDTO.getComplemento());
            enderecoAt.setReferencia(enderecoRequestDTO.getReferencia());

            enderecoRepository.save(enderecoAt);
            return modelMapper.map(enderecoAt, EnderecoResponseDTO.class);
        }).orElseThrow(() -> new ClienteNaoEncontradoException("Cliente não encontrado"));
        return enderecoResponseDTO;
    }


}
