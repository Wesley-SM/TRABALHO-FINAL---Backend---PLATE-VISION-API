package com.sistema.plate_vision_api.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_veiculo")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String placa;

    @Column(length = 50)
    private String modelo;

    @Column(length = 30)
    private String cor;

    @Column(nullable = false)
    private String status; // Ex: "REGULAR", "BLOQUEADO"

    // Relação N:1 - Muitos veículos pertencem a um Proprietário
    @JsonIgnoreProperties("veiculos")
    @ManyToOne
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Proprietario proprietario;

    // Construtores
    public Veiculo() {}

    public Veiculo(Long id, String placa, String modelo, String cor, String status, Proprietario proprietario) {
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.cor = cor;
        this.status = status;
        this.proprietario = proprietario;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

public String getStatus() { return status; }
public void setStatus(String status) { this.status = status; }

public Proprietario getProprietario() { return proprietario; }
public void setProprietario(Proprietario proprietario) { this.proprietario = proprietario; }
}