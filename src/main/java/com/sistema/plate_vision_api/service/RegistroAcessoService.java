package com.sistema.plate_vision_api.service;

import com.sistema.plate_vision_api.model.RegistroAcesso;
import com.sistema.plate_vision_api.repository.RegistroAcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class RegistroAcessoService {

    @Autowired
    private RegistroAcessoRepository repository;

    // Salva um novo registro de acesso (será chamado quando a IA ler a placa)
    public RegistroAcesso salvarAcesso(RegistroAcesso registro) {
        return repository.save(registro);
    }

    // GET ALL (Historico com Paginação e Ordenação)
    public Page<RegistroAcesso> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // GET ONE
    public RegistroAcesso getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro de acesso não encontrado."));
    }

    // DELETE (Caso precise limpar do histórico)
    public void delete(Long id) {
        RegistroAcesso registro = getOne(id);
        repository.delete(registro);
    }
}