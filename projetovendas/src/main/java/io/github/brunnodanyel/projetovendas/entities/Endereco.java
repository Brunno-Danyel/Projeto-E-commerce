package io.github.brunnodanyel.projetovendas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Endereco {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_endereco")
    private Long id;

    private String cep;

    private String identificacao;

    private String logradouro;

    private String numero;

    private String complemento;

    private String referencia;

    private String bairro;

    private String localidade;

    private String uf;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @OneToMany(mappedBy = "enderecoEntrega", cascade = CascadeType.ALL)
    private List<Pedido> pedidosEntrega;
}
