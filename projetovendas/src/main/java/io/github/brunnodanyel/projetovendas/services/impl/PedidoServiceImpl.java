package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.*;
import io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum;
import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import io.github.brunnodanyel.projetovendas.exception.*;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ItensPedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.*;
import io.github.brunnodanyel.projetovendas.repositories.*;
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
    private EnderecoRepository enderecoRepository;

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

        if (pedido.getTipoEntrega().equals(TipoEntregaEnum.ENTREGA)) {
            Endereco endereco = enderecoRepository.findById(pedidoRequestDTO.getIdEnderecoEntrega())
                    .orElseThrow(() -> new EnderecoNaoEncontradoException("Endereço não encontrado"));
            pedido.setEnderecoEntrega(endereco);
        }

        List<ItemPedido> itensPedidos = converterItens(pedido, pedidoRequestDTO.getItens());
        BigDecimal totalPedido = calcularTotalPedido(itensPedidos);

        if (itensPedidos.isEmpty()) {
            throw new PedidoException("Não tem produtos para se realizar pedido, adicione os produtos que deseja");
        }
        pedido.setTotalPedido(totalPedido);
        pedido.setNumeroPedido(numeroPedido);
        pedido.setCliente(cliente);
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return retornarPedido(pedido);
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItensPedidoRequestDTO> itens) {
        return itens.stream().map(itemPedidoDto -> {
            String numeroProduto = itemPedidoDto.getNumeroProduto();
            Produto produto = produtoRepository.findByCodigoDoProduto(numeroProduto)
                    .orElseThrow(() -> new ProdutoNaoEncontradoException("Codigo do produto não encontrado: " + numeroProduto));

            Integer quantidadePedido = itemPedidoDto.getQuantidade();
            Integer quantidadeDisponivel = produto.getQuantidade();
            Integer quantidadeProduto = quantidadeDisponivel - quantidadePedido;

            if (produto.getQuantidade() < quantidadePedido) {
                throw new ProdutoException("Não temos produto suficientes para atender a essa quantidade que deseja!");
            }

            BigDecimal quantidadeBD = BigDecimal.valueOf(itemPedidoDto.getQuantidade());
            BigDecimal totalProduto = produto.getPreco().multiply(quantidadeBD);

            ItemPedido itemPedido = criarItemPedido(pedido, produto, quantidadePedido, totalProduto);
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
        pedido.setTipoEntrega(pedidoRequestDTO.getTipoEntrega());
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

    private ItemPedido criarItemPedido(Pedido pedido, Produto produto, Integer quantidade, BigDecimal totalProduto) {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setQuantidade(quantidade);
        itemPedido.setProduto(produto);
        itemPedido.setPedido(pedido);
        itemPedido.setTotalProduto(totalProduto);
        return itemPedido;
    }

    private PedidoResponseDTO retornarPedido(Pedido pedido) {
        Cliente cliente = pedido.getCliente();
        PedidoClienteResponseDTO clienteResponseDTO = modelMapper.map(cliente, PedidoClienteResponseDTO.class);

        List<PedidoItemResponseDTO> pedidoItemResponseDTOS = pedido.getItens().stream().map(itemPedido -> {
            PedidoItemResponseDTO pedidoItemResponseDTO = modelMapper.map(itemPedido, PedidoItemResponseDTO.class);
            return pedidoItemResponseDTO;
        }).collect(Collectors.toList());

        Endereco endereco = pedido.getEnderecoEntrega();
        EnderecoResponseDTO enderecoResponseDTO = null;
        if (endereco != null) {
            enderecoResponseDTO = modelMapper.map(endereco, EnderecoResponseDTO.class);
        }

        PedidoResponseDTO pedidoResponseDTO = modelMapper.map(pedido, PedidoResponseDTO.class);
        pedidoResponseDTO.setCliente(clienteResponseDTO);
        pedidoResponseDTO.setEnderecoEntrega(enderecoResponseDTO);
        pedidoResponseDTO.setItens(pedidoItemResponseDTOS);
        return pedidoResponseDTO;
    }


}
