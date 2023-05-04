package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.Endereco;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.EnderecoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.ClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.EnderecoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ClienteRepository;
import io.github.brunnodanyel.projetovendas.repositories.EnderecoRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import io.github.brunnodanyel.projetovendas.services.EnderecoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
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



}
