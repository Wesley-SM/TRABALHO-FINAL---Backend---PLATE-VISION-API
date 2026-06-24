package com.sistema.plate_vision_api.service;

import com.sistema.plate_vision_api.dto.ProprietarioDTO;
import com.sistema.plate_vision_api.model.Proprietario;
import com.sistema.plate_vision_api.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository repository;

    // CREATE
    public Proprietario create(ProprietarioDTO dto) {
        if (repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado no sistema.");
        }
        Proprietario proprietario = new Proprietario();
        proprietario.setNome(dto.getNome());
        proprietario.setCpf(dto.getCpf());
        proprietario.setTelefone(dto.getTelefone());
        return repository.save(proprietario);
    }

   //getall
    public Page<Proprietario> getAll(String nome, Pageable pageable) {
        if (nome != null && !nome.trim().isEmpty()) {
            return repository.findByNomeContainingIgnoreCase(nome, pageable);
        }
        return repository.findAll(pageable);
    }

    // GET ONE
    public Proprietario getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proprietário não encontrado."));
    }

    // UPDATE
    public Proprietario update(Long id, ProprietarioDTO dto) {
        Proprietario proprietario = getOne(id);

        // 1. Validação de segurança extra (Opcional, mas evita crash no banco):
        // Se o CPF enviado for diferente do atual, confere se já não pertence a outra pessoa
        if (!proprietario.getCpf().equals(dto.getCpf()) && repository.findByCpf(dto.getCpf()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este CPF já pertence a outro proprietário cadastrado.");
        }

        // 2. Atualiza os campos de fato
        proprietario.setNome(dto.getNome());
        proprietario.setTelefone(dto.getTelefone());
        proprietario.setCpf(dto.getCpf()); // <-- Linha ativada com sucesso!

        return repository.save(proprietario);
    }

    // DELETE
    public void delete(Long id) {
        Proprietario proprietario = getOne(id);
        repository.delete(proprietario);
    }
}