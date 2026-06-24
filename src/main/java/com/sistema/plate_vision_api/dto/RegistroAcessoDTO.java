package com.sistema.plate_vision_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegistroAcessoDTO {

    @NotBlank(message = "A placa detectada é obrigatória.")
    @Size(min = 7, max = 7, message = "A placa deve conter exatamente 7 caracteres.")
    private String placaDetectada;

    @NotBlank(message = "O status do acesso é obrigatório (ex: LIBERADO, NEGADO).")
    private String statusAcesso;

    @NotBlank(message = "A justificativa é obrigatória.")
    private String justificativa;

    // Construtor Padrão (Obrigatório para o Jackson/Spring)
    public RegistroAcessoDTO() {
    }

    // Construtor Completo
    public RegistroAcessoDTO(String placaDetectada, String statusAcesso, String justificativa) {
        this.placaDetectada = placaDetectada;
        this.statusAcesso = statusAcesso;
        this.justificativa = justificativa;
    }

    // Getters e Setters
    public String getPlacaDetectada() {
        return placaDetectada;
    }

    public void setPlacaDetectada(String placaDetectada) {
        this.placaDetectada = placaDetectada;
    }

    public String getStatusAcesso() {
        return statusAcesso;
    }

    public void setStatusAcesso(String statusAcesso) {
        this.statusAcesso = statusAcesso;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }
}