package com.sistema.plate_vision_api.model;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_proprietario")
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(length = 20)
    private String telefone;

    // Relação 1:N - Um proprietário pode ter muitos veículos
    // O cascade garante que se apagares um dono, os carros dele também são geridos corretamente
    @JsonIgnoreProperties("proprietario")
    @OneToMany(mappedBy = "proprietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Veiculo> veiculos = new ArrayList<>();

    // Construtores
    public Proprietario() {}

    public Proprietario(Long id, String nome, String cpf, String telefone) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(List<Veiculo> veiculos) { this.veiculos = veiculos; }
}