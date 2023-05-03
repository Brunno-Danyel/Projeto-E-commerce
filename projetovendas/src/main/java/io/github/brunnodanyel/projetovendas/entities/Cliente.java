package io.github.brunnodanyel.projetovendas.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tb_cpf")
    private String cpf;

    @Column(name = "tb_nome_completo")
    private String nomeCompleto;

    @Column(name = "tb_data_nascimento")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy", locale = "pt-BR", timezone = "Brazil/East")
    private LocalDate dataNascimento;

    @Column(name = "tb_telefone_celular")
    private String telefoneCelular;

    @Column(name = "tb_email")
    private String email;

    @Column(name = "tb_senha")
    private String senha;

    @Column(name = "tb_enderecos")
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.MERGE)
    private List<Endereco> enderecos = new ArrayList<>();

    @Column(name = "tb_pedidos")
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidos;

    private boolean admin;

}
