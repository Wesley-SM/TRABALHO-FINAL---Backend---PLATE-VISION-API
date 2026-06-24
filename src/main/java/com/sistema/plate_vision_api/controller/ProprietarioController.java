package com.sistema.plate_vision_api.controller;

import com.sistema.plate_vision_api.dto.ProprietarioDTO;
import com.sistema.plate_vision_api.model.Proprietario;
import com.sistema.plate_vision_api.service.ProprietarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/proprietarios")
@CrossOrigin(origins = "*")
public class ProprietarioController {

    @Autowired
    private ProprietarioService service;

    // POST - Criar (Retorna 201 Created)
    @PostMapping
    public ResponseEntity<Proprietario> create(@Valid @RequestBody ProprietarioDTO dto) {
        Proprietario novo = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    // GET ALL - Listar com paginação, ordenação e filtro por nome (Ex: /proprietarios?page=0&size=5&sort=nome,asc)
    @GetMapping
    public ResponseEntity<Page<Proprietario>> getAll(
            @RequestParam(required = false) String nome,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(service.getAll(nome, pageable));
    }

    // GET ONE - Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(service.getOne(id));
    }

    // PUT - Atualizar por ID
    @PutMapping("/{id}")
    public ResponseEntity<Proprietario> update(@PathVariable Long id, @Valid @RequestBody ProprietarioDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // DELETE - Deletar por ID (Retorna 204 No Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}