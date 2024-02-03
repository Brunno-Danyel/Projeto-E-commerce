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
        Cliente cliente = clienteService.usuarioAutenticado();
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
        return modelMapper.map(pedido, PedidoResponseDTO.class);
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
        Cliente cliente = clienteService.usuarioAutenticado();
        return pedidoRepository.findByClienteCpf(cliente.getCpf()).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoBuscaResponseDTO.class)).collect(Collectors.toList());
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
                    return itemPedido.getProduto().getPreco().multiply(quantidadeBD);
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



}
