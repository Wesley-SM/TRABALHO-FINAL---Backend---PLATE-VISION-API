package com.sistema.plate_vision_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class VeiculoDTO {

    @NotBlank(message = "A placa é obrigatória.")
    @Size(min = 7, max = 7, message = "A placa deve ter exatamente 7 caracteres.")
    private String placa;

    @NotBlank(message = "O modelo do veículo é obrigatório.")
    private String modelo;

    private String cor;

    @NotBlank(message = "O status do veículo é obrigatório (REGULAR/BLOQUEADO).")
    private String status;

    private Long proprietarioId; // ID do dono do carro para linkar no banco

    // Construtores
    public VeiculoDTO() {}

    // Getters e Setters
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getProprietarioId() { return proprietarioId; }
    public void setProprietarioId(Long proprietarioId) { this.proprietarioId = proprietarioId; }
}