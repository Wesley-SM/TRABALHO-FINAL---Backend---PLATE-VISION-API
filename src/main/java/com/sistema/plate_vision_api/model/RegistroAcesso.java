package com.sistema.plate_vision_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_registro_acesso")
public class RegistroAcesso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 7)
    private String placaDetectada;

    @Column(nullable = false)
    private LocalDateTime dataHoraAcesso;

    @Column(nullable = false)
    private String statusAcesso; // Ex: "LIBERADO" ou "NEGADO"

    @Column(length = 255)
    private String justificativa; // Ex: "Veículo cadastrado de Lucas" ou "Placa não encontrada no sistema"

    // Construtores
    public RegistroAcesso() {}

    public RegistroAcesso(String placaDetectada, LocalDateTime dataHoraAcesso, String statusAcesso, String justificativa) {
        this.placaDetectada = placaDetectada;
        this.dataHoraAcesso = dataHoraAcesso;
        this.statusAcesso = statusAcesso;
        this.justificativa = justificativa;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlacaDetectada() { return placaDetectada; }
    public void setPlacaDetectada(String placaDetectada) { this.placaDetectada = placaDetectada; }

    public LocalDateTime getDataHoraAcesso() { return dataHoraAcesso; }
    public void setDataHoraAcesso(LocalDateTime dataHoraAcesso) { this.dataHoraAcesso = dataHoraAcesso; }

    public String getStatusAcesso() { return statusAcesso; }
    public void setStatusAcesso(String statusAcesso) { this.statusAcesso = statusAcesso; }

    public String getJustificativa() { return justificativa; }
    public void setJustificativa(String justificativa) { this.justificativa = justificativa; }
}