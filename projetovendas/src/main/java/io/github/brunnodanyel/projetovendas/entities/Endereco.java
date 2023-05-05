package io.github.brunnodanyel.projetovendas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Endereco {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tb_cep")
    private String cep;

    @Column(name = "tb_identificao")
    private String identificacao;

    @Column(name = "tb_logradouro")
    private String logradouro;

    @Column(name = "tb_numero")
    private Integer numero;

    @Column(name = "tb_complemento")
    private String complemento;

    @Column(name = "tb_referencia")
    private String referencia;

    @Column(name = "tb_bairro")
    private String bairro;

    @Column(name = "tb_cidade")
    private String cidade;

    @Column(name = "tb_uf")
    private String uf;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
}
