package io.github.brunnodanyel.projetovendas.services.impl;

import io.github.brunnodanyel.projetovendas.entities.*;
import io.github.brunnodanyel.projetovendas.enumeration.*;
import io.github.brunnodanyel.projetovendas.enumeration.TipoEntregaEnum;
import io.github.brunnodanyel.projetovendas.exception.BadRequestExecption;
import io.github.brunnodanyel.projetovendas.exception.EntidadeNaoEncontrada;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.ItensPedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoRequest.PedidoRequestDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoBuscaResponseDTO;
import io.github.brunnodanyel.projetovendas.model.dtoResponse.PedidoResponseDTO;
import io.github.brunnodanyel.projetovendas.repositories.EnderecoRepository;
import io.github.brunnodanyel.projetovendas.repositories.ItemPedidoRepository;
import io.github.brunnodanyel.projetovendas.repositories.PedidoRepository;
import io.github.brunnodanyel.projetovendas.repositories.ProdutoRepository;
import io.github.brunnodanyel.projetovendas.services.UsuarioService;
import io.github.brunnodanyel.projetovendas.services.PedidoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.github.brunnodanyel.projetovendas.enumeration.StatusPedidoEnum.*;

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
    private UsuarioService usuarioService;

    public PedidoResponseDTO realizarPedido(PedidoRequestDTO pedidoRequestDTO) {
        Usuario usuario = usuarioService.usuarioAutenticado();
        if (!pedidoRequestDTO.isEntrega() && !pedidoRequestDTO.isRetirada()) {
            throw new BadRequestExecption("É necessário escolher pelo menos uma opção: ENTREGA ou RETIRADA");
        }

        if(pedidoRequestDTO.isEntrega() && pedidoRequestDTO.isRetirada()){
            throw new BadRequestExecption("É necessário que escolha apenas uma opção: ENTREGA ou RETIRADA");
        }
        Pedido pedido = converterRequest(pedidoRequestDTO);

        UUID uuid = UUID.randomUUID();
        String numeroPedido = uuid.toString();

        if (pedidoRequestDTO.isEntrega()) {
            if (pedidoRequestDTO.getIdEnderecoEntrega() == null) {
                throw new BadRequestExecption("É necessário um endereço para a conclusão do pedido");
            }
            Endereco endereco = enderecoRepository.findById(pedidoRequestDTO.getIdEnderecoEntrega())
                    .orElseThrow(() -> new EntidadeNaoEncontrada("Endereço não encontrado"));
            pedido.setEnderecoEntrega(endereco);
            pedido.setTipoEntrega(TipoEntregaEnum.ENTREGA);
        } else {
            pedido.setTipoEntrega(TipoEntregaEnum.RETIRADA);
        }

        List<ItemPedido> itensPedidos = converterItens(pedido, pedidoRequestDTO.getItens());
        BigDecimal totalPedido = calcularTotalPedido(itensPedidos);

        if (itensPedidos.isEmpty()) {
            throw new BadRequestExecption("Não tem produtos para se realizar pedido, adicione os produtos que deseja");
        }
        pedido.setTotalPedido(totalPedido);
        pedido.setNumeroPedido(numeroPedido);
        pedido.setUsuario(usuario);
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itensPedidos);
        pedido.setItens(itensPedidos);
        return modelMapper.map(pedido, PedidoResponseDTO.class);
    }

    public void cancelaPedido(Long idPedido) {
        pedidoRepository.findById(idPedido).map(pedido -> {
            if(pedido.getStatusPedido().equals(ENTREGUE) || pedido.getStatusPedido().equals(CONCLUIDO)){
                throw new BadRequestExecption("Pedido não pode ser cancelar pois está com status: " + pedido.getStatusPedido().name());
            }
            pedido.setStatusPedido(CANCELADO);
            return pedido;
        }).orElseThrow(() -> new EntidadeNaoEncontrada("Pedido não encontrado!"));
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItensPedidoRequestDTO> itens) {
        return itens.stream().map(itemPedidoDto -> {
            String numeroProduto = itemPedidoDto.getNumeroProduto();
            Produto produto = produtoRepository.findByCodigoDoProduto(numeroProduto)
                    .orElseThrow(() -> new EntidadeNaoEncontrada("Codigo do produto não encontrado: " + numeroProduto));

            Integer quantidadePedido = itemPedidoDto.getQuantidade();
            Integer quantidadeDisponivel = produto.getQuantidade();
            Integer quantidadeProduto = quantidadeDisponivel - quantidadePedido;

            if (produto.getQuantidade() < quantidadePedido) {
                throw new BadRequestExecption("Não temos produto suficientes para atender a essa quantidade que deseja!");
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
        Usuario usuario = usuarioService.usuarioAutenticado();
        return pedidoRepository.findByUsuarioCpf(usuario.getCpf()).stream()
                .map(pedido -> modelMapper.map(pedido, PedidoBuscaResponseDTO.class)).collect(Collectors.toList());
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
