package com.sistema.plate_vision_api.controller;

import com.sistema.plate_vision_api.model.RegistroAcesso;
import com.sistema.plate_vision_api.service.RegistroAcessoService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/acessos")
@CrossOrigin(origins = "*")
public class RegistroAcessoController {

    @Autowired
    private RegistroAcessoService service;

    // GET ALL - Histórico de acessos com paginação e ordenação por data decrescente (mais recentes primeiro)
    @GetMapping
    public ResponseEntity<Page<RegistroAcesso>> getAll(
            @PageableDefault(page = 0, size = 20, sort = "dataHoraAcesso", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    // 🃏 CARTA-DESAFIO: Endpoint para exportar os dados reais da entidade em formato CSV para download
    @GetMapping("/export")
    public void exportarParaCSV(HttpServletResponse response) throws IOException {
        // Configura o tipo do arquivo e o cabeçalho de download
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=historico_acessos.csv");

        // Busca todos os registros sem paginação para colocar no relatório
        Page<RegistroAcesso> todosAcessos = service.getAll(Pageable.unpaged());

        // Escreve o arquivo tratando acentos para abrir perfeitamente no Excel
        try (PrintWriter writer = new PrintWriter(response.getOutputStream(), false, StandardCharsets.UTF_8)) {
            // Escreve a BOM do UTF-8 para o Excel reconhecer os acentos do Português
            writer.write('\ufeff');

            // Cabeçalho do CSV separados por ponto e vírgula (;)
            writer.println("ID;Placa Detectada;Data e Hora;Status do Acesso;Justificativa");

            // Linhas de dados
            for (RegistroAcesso acesso : todosAcessos.getContent()) {
                writer.println(String.format("%d;%s;%s;%s;%s",
                        acesso.getId(),
                        acesso.getPlacaDetectada(),
                        acesso.getDataHoraAcesso().toString(),
                        acesso.getStatusAcesso(),
                        acesso.getJustificativa()
                ));
            }
            writer.flush();
        }
    }
}