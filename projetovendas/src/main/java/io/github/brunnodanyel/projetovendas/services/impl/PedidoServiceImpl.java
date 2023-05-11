package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.Cliente;
import io.github.brunnodanyel.projetovendas.entities.ItemPedido;
import io.github.brunnodanyel.projetovendas.entities.Pedido;
import io.github.brunnodanyel.projetovendas.entities.Produto;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import io.github.brunnodanyel.projetovendas.exception.ClienteNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.exception.PedidoException;
import io.github.brunnodanyel.projetovendas.exception.ProdutoException;
import io.github.brunnodanyel.projetovendas.exception.ProdutoNaoEncontradoException;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ItensPedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoClienteResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoItemResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.ClienteRepository;
import io.github.brunnodanyel.projetovendas.repositories.ItemPedidoRepository;
import io.github.brunnodanyel.projetovendas.repositories.PedidoRepository;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.ClienteService;
import io.github.brunnodanyel.projetovendas.services.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ClienteService clienteService;

    public PedidoResponseDTO realizarPedido(PedidoRequestDTO pedidoRequestDTO) {
        String cpf = clienteService.retornaCpfClienteAutenticado();
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ClienteNaoEncontradoException("CPF " + cpf + " não encontrado!"));
        Pedido pedido = converterRequest(pedidoRequestDTO);

        UUID uuid = UUID.randomUUID();
        String numeroPedido = uuid.toString();
        List<ItemPedido> itensPedidos = converterItens(pedido, pedidoRequestDTO.getItens());
        BigDecimal totalPedido = calcularTotalPedido(itensPedidos);

        if (itensPedidos.isEmpty()) {
            throw new PedidoException("Não tem produtos para se realizar pedido, adicione os produtos que deseja");
        }
        pedido.setTotal(totalPedido);
        pedido.setNumeroPedido(numeroPedido);
        pedido.setCliente(cliente);
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return retornarPedido(pedido);
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItensPedidoRequestDTO> itens) {
        return itens.stream().map(dto -> {
            String numeroProduto = dto.getNumeroProduto();
            Produto produto = produtoRepository.findByCodigoDoProduto(numeroProduto)
                    .orElseThrow(() -> new ProdutoNaoEncontradoException("Codigo do produto não encontrado: " + numeroProduto));

            Integer quantidade = dto.getQuantidade();
            Integer quantidadeProduto = produto.getQuantidade() - quantidade;

            if (produto.getQuantidade() <= 0) {
                throw new ProdutoException("Produto em falta!");
            }

            BigDecimal quantidadeBD = BigDecimal.valueOf(dto.getQuantidade());
            BigDecimal totalProduto = produto.getPreco().multiply(quantidadeBD);
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setProduto(produto);
            itemPedido.setPedido(pedido);
            itemPedido.setTotalProduto(totalProduto);
            produto.setQuantidade(quantidadeProduto);
            produtoRepository.save(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }

    public List<PedidoBuscaResponseDTO> buscarPedidoCpf() {
        String cpf = clienteService.retornaCpfClienteAutenticado();
        List<PedidoBuscaResponseDTO> listaPedido = pedidoRepository.findByClienteCpf(cpf).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoBuscaResponseDTO.class)).collect(Collectors.toList());
        return listaPedido;
    }


    private static Pedido converterRequest(PedidoRequestDTO pedidoRequestDTO) {
        Pedido pedido = new Pedido();
        pedido.setDataDoPedido(LocalDate.now());
        pedido.setStatusPedido(StatusPedidoEnum.EM_PROCESSAMENTO);
        return pedido;
    }

    private BigDecimal calcularTotalPedido(List<ItemPedido> itensPedidos) {
        return itensPedidos.stream()
                .map(itemPedido -> {
                    BigDecimal quantidadeBD = BigDecimal.valueOf(itemPedido.getQuantidade());
                    BigDecimal total = itemPedido.getProduto().getPreco().multiply(quantidadeBD);
                    return total;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private PedidoResponseDTO retornarPedido(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        PedidoClienteResponseDTO clienteResponseDTO = modelMapper.map(cliente, PedidoClienteResponseDTO.class);

        List<PedidoItemResponseDTO> pedidoItemResponseDTOS = pedido.getItens().stream().map(itemPedido -> {
            PedidoItemResponseDTO pedidoItemResponseDTO = modelMapper.map(itemPedido, PedidoItemResponseDTO.class);
            return pedidoItemResponseDTO;
        }).collect(Collectors.toList());

        PedidoResponseDTO pedidoResponseDTO = modelMapper.map(pedido, PedidoResponseDTO.class);
        pedidoResponseDTO.setCliente(clienteResponseDTO);
        pedidoResponseDTO.setItens(pedidoItemResponseDTOS);
        return pedidoResponseDTO;
    }


}
