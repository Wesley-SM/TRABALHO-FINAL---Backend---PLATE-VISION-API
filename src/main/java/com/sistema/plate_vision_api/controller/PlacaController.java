package com.sistema.plate_vision_api.controller;

import com.sistema.plate_vision_api.model.RegistroAcesso;
import com.sistema.plate_vision_api.model.Veiculo;
import com.sistema.plate_vision_api.repository.VeiculoRepository;
import com.sistema.plate_vision_api.service.PlacaService;
import com.sistema.plate_vision_api.service.RegistroAcessoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/placas")
@CrossOrigin(origins = "*")
public class PlacaController {

    private final PlacaService placaService;
    private final VeiculoRepository veiculoRepository;
    private final RegistroAcessoService registroAcessoService;

    // Construtor atualizado injetando os novos componentes necessários
    public PlacaController(PlacaService placaService,
                           VeiculoRepository veiculoRepository,
                           RegistroAcessoService registroAcessoService) {
        this.placaService = placaService;
        this.veiculoRepository = veiculoRepository;
        this.registroAcessoService = registroAcessoService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> enviarImagem(@RequestParam("foto") MultipartFile arquivo) {
        try {
            // 1. Envia para o seu PlacaService processar com a IA do Gemini
            String resultadoGemini = placaService.processarImagemPlaca(arquivo);

            String placaBruta = resultadoGemini;
            if (resultadoGemini.contains(":")) {
                placaBruta = resultadoGemini.substring(resultadoGemini.lastIndexOf(":") + 1);
            }

            // 2. Remove espaços, hífenes e força Letras Maiúsculas
            placaBruta = placaBruta.replaceAll("[^a-zA-Z0-9]", "").toUpperCase().trim();

            // 3. Executa o seu Robô de Correção Avançado
            String placaCorrigida = corrigirOcrPlaca(placaBruta);

            // 4. Procura o veículo na nova tabela tb_veiculo
            Optional<Veiculo> veiculoBanco = veiculoRepository.findByPlaca(placaCorrigida);

            // Variáveis para salvar o histórico de acesso obrigatório
            String statusAcesso;
            String justificativa;

            if (veiculoBanco.isPresent()) {
                Veiculo veiculo = veiculoBanco.get();

                // Verifica o status de regras de negócio do veículo
                if ("REGULAR".equalsIgnoreCase(veiculo.getStatus())) {
                    statusAcesso = "LIBERADO";
                    justificativa = "Veículo autorizado. Proprietário: " + veiculo.getProprietario().getNome();
                } else {
                    statusAcesso = "NEGADO";
                    justificativa = "Acesso Bloqueado: Veículo com restrição interna. Proprietário: " + veiculo.getProprietario().getNome();
                }

                // Salva o log no banco de dados em tempo real
                RegistroAcesso novoAcesso = new RegistroAcesso(placaCorrigida, LocalDateTime.now(), statusAcesso, justificativa);
                registroAcessoService.salvarAcesso(novoAcesso);

                // BLINDAGEM CONTRA NULL POINTER EXCEPTION:
                return ResponseEntity.ok(Map.of(
                        "mensagem", statusAcesso.equals("LIBERADO") ? "VEÍCULO IDENTIFICADO E LIBERADO!" : "VEÍCULO IDENTIFICADO, MAS BLOQUEADO!",
                        "placa_original_gemini", placaBruta,
                        "placa_processada_sistema", veiculo.getPlaca() != null ? veiculo.getPlaca() : placaCorrigida,
                        "proprietario", (veiculo.getProprietario() != null && veiculo.getProprietario().getNome() != null) ? veiculo.getProprietario().getNome() : "Não Cadastrado",
                        "modelo", veiculo.getModelo() != null ? veiculo.getModelo() : "---",
                        "cor", veiculo.getCor() != null ? veiculo.getCor() : "---",
                        "status", veiculo.getStatus() != null ? veiculo.getStatus() : "DESCONHECIDO",
                        "status_acesso", statusAcesso,
                        "justificativa", justificativa
                ));
            }

            // Se o veículo não constar na tabela de cadastros
            statusAcesso = "NEGADO";
            justificativa = "Veículo não cadastrado no sistema.";

            // Salva o registro de tentativa de invasão ou veículo desconhecido no histórico
            RegistroAcesso novoAcesso = new RegistroAcesso(placaCorrigida, LocalDateTime.now(), statusAcesso, justificativa);
            registroAcessoService.salvarAcesso(novoAcesso);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "alerta", "VEÍCULO NÃO CADASTRADO NO SISTEMA!",
                    "placa_detectada", placaCorrigida,
                    "status_acesso", statusAcesso,
                    "acao", "Acesso bloqueado. Cadastre o proprietário e o veículo antes de permitir a entrada."
            ));

        } catch (Exception e) {
            // CORREÇÃO DO RETORNO DE ERRO: Devolve JSON estruturado para o front-end ler sem estourar o script
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                    "status_acesso", "ERRO",
                    "acao", "Erro interno no servidor Java: " + (e.getMessage() != null ? e.getMessage() : e.toString())
            ));
        }
    }

    /**
     * ROBÔ DE CORREÇÃO INTELIGENTE DE OCR (Mantido Intacto)
     */
    private String corrigirOcrPlaca(String placa) {
        if (placa == null || placa.length() != 7) {
            return placa;
        }

        char[] car = placa.toCharArray();

        for (int i = 0; i < 3; i++) {
            car[i] = forçarLetra(car[i]);
        }

        car[3] = forçarNumero(car[3]);
        car[6] = forçarNumero(car[6]);

        boolean ehCarroMercosul = (Character.isLetter(car[4]) || car[4] == 'O' || car[4] == 'I' || car[4] == 'S');

        boolean ehMotoMercosul = (Character.isDigit(car[4]) || car[4] == '0' || car[4] == '1')
                && (Character.isLetter(car[5]) || car[5] == 'O' || car[5] == 'I');

        if (ehCarroMercosul) {
            car[4] = forçarLetra(car[4]);
            car[5] = forçarNumero(car[5]);
        } else if (ehMotoMercosul) {
            car[4] = forçarNumero(car[4]);
            car[5] = forçarLetra(car[5]);
        } else {
            car[4] = forçarNumero(car[4]);
            car[5] = forçarNumero(car[5]);
        }

        return new String(car);
    }

    private char forçarLetra(char c) {
        switch (c) {
            case '0': return 'O';
            case '1': return 'I';
            case '2': return 'Z';
            case '5': return 'S';
            case '6': return 'G';
            case '8': return 'B';
            default: return c;
        }
    }

    private char forçarNumero(char c) {
        switch (c) {
            case 'O': case 'Q': return '0';
            case 'I': case 'L': return '1';
            case 'Z': return '2';
            case 'S': return '5';
            case 'G': return '6';
            case 'B': return '8';
            default: return c;
        }
    }
}