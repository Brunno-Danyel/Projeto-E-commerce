package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.EnderecoRepository;
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


    @Override
    public List<EnderecoResponseDTO> buscarEnderecoCliente(String cpf) {
        List<Endereco> enderecos = enderecoRepository.findByClienteCpf(cpf);
        return enderecos.stream()
                .map(endereco -> modelMapper.map(endereco, EnderecoResponseDTO.class)).collect(Collectors.toList());
    }

    public EnderecoResponseDTO atualizaEnderecoCliente(String cpf, Long idEndereco, EnderecoRequestDTO enderecoRequestDTO) {
        EnderecoResponseDTO enderecoResponseDTO = enderecoRepository.findByClienteCpfAndId(cpf, idEndereco).map(endereco -> {
            Endereco enderecoAt = enderecoRepository.findById(idEndereco).orElseThrow(() -> new RuntimeException(""));

            enderecoAt.setBairro(enderecoRequestDTO.getBairro());
            enderecoAt.setCep(enderecoRequestDTO.getCep());
            enderecoAt.setIdentificacao(enderecoRequestDTO.getIdentificacao());
            enderecoAt.setLogradouro(enderecoRequestDTO.getLogradouro());
            enderecoAt.setNumero(enderecoRequestDTO.getNumero());
            enderecoAt.setComplemento(enderecoRequestDTO.getComplemento());
            enderecoAt.setReferencia(enderecoRequestDTO.getReferencia());
            enderecoAt.setCidade(enderecoRequestDTO.getCidade());
            enderecoAt.setUf(enderecoRequestDTO.getUf());

            enderecoRepository.save(enderecoAt);
            return modelMapper.map(enderecoAt, EnderecoResponseDTO.class);
        }).orElseThrow(() -> new RuntimeException(""));
        return enderecoResponseDTO;
    }


}
