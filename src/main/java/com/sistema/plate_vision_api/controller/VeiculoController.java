package com.sistema.plate_vision_api.controller;

import com.sistema.plate_vision_api.dto.VeiculoDTO;
import com.sistema.plate_vision_api.model.Veiculo;
import com.sistema.plate_vision_api.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculos")
@CrossOrigin(origins = "*")
public class VeiculoController {

    @Autowired
    private VeiculoService service;

    // POST - Criar Veículo ligado a um Proprietário
    @PostMapping
    public ResponseEntity<Veiculo> create(@Valid @RequestBody VeiculoDTO dto) {
        Veiculo novo = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // GET ALL - Listar Veículos com Paginação e Ordenação
    @GetMapping
    public ResponseEntity<Page<Veiculo>> getAll(
            @PageableDefault(page = 0, size = 10, sort = "placa", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(pageable));
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    // PUT
    @PutMapping("/{id}")
    public ResponseEntity<Veiculo> update(@PathVariable Long id, @Valid @RequestBody VeiculoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}