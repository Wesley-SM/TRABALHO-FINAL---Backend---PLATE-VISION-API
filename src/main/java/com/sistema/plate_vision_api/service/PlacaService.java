
package com.sistema.plate_vision_api.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlacaService {

    // Captura a chave direto do seu application.properties automaticamente
    @Value("${gemini.api.key}")
    private String apiKey;

    public PlacaService() {
        // Construtor limpo, sem o repositório antigo que foi removido do projeto
    }

    public String processarImagemPlaca(MultipartFile arquivo) {
        try {
            // 1. Converte a imagem recebida do celular para Base64
            String imagemBase64 = Base64.getEncoder().encodeToString(arquivo.getBytes());
            String mimeType = arquivo.getContentType();

            // 2. Monta a URL correta utilizando o modelo Gemini 2.5 Flash
            String url = "https://generativelanguage.googleapis.com/v1/models/gemini-2.5-flash:generateContent?key=" + apiKey;

            // 3. Monta a estrutura do JSON idêntica à que a API da Google exige
            Map<String, Object> requestBody = new HashMap<>();

            Map<String, Object> textPart = new HashMap<>();
            textPart.put("text", "Analise a imagem e retorne APENAS o texto da placa do veículo (ex: ABC1D23 ou ABC1234), sem espaços, pontos ou textos adicionais. Se não encontrar nenhuma placa, retorne exatamente a palavra: DESCONHECIDO");

            Map<String, Object> inlineData = new HashMap<>();
            inlineData.put("mimeType", mimeType);
            inlineData.put("data", imagemBase64);

            Map<String, Object> imagePart = new HashMap<>();
            imagePart.put("inlineData", inlineData);

            Map<String, Object> partsMap = new HashMap<>();
            partsMap.put("parts", new Object[]{textPart, imagePart});

            requestBody.put("contents", new Object[]{partsMap});

            // 4. Configura os cabeçalhos HTTP
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // 5. Faz a chamada de fato para a Google
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            // 6. Extrai o texto da resposta da Google de dentro da árvore do JSON de retorno
            List<?> candidates = (List<?>) response.getBody().get("candidates");
            Map<?, ?> firstCandidate = (Map<?, ?>) candidates.get(0);
            Map<?, ?> content = (Map<?, ?>) firstCandidate.get("content");
            List<?> parts = (List<?>) content.get("parts");
            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);

            String textoPlaca = firstPart.get("text").toString().trim();

            return "Placa identificada pelo Gemini: " + textoPlaca;

        } catch (Exception e) {
            throw new RuntimeException("Erro na comunicação direta HTTP com o Gemini: " + e.getMessage(), e);
        }
    }
}

