package com.sistema.plate_vision_api.service;

import com.sistema.plate_vision_api.dto.VeiculoDTO;
import com.sistema.plate_vision_api.model.Proprietario;
import com.sistema.plate_vision_api.model.Veiculo;
import com.sistema.plate_vision_api.repository.ProprietarioRepository;
import com.sistema.plate_vision_api.repository.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository repository;

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    // CREATE
    public Veiculo create(VeiculoDTO dto) {
        if (repository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Placa já cadastrada no sistema.");
        }

        Proprietario proprietario = proprietarioRepository.findById(dto.getProprietarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Proprietário não encontrado."));

        Veiculo veiculo = new Veiculo();
        veiculo.setPlaca(dto.getPlaca().toUpperCase().replaceAll("[^a-zA-Z0-9]", "").trim());
        veiculo.setModelo(dto.getModelo());
        veiculo.setCor(dto.getCor());
        veiculo.setStatus(dto.getStatus());
        veiculo.setProprietario(proprietario);

        return repository.save(veiculo);
    }

    // GET ALL
    public Page<Veiculo> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // GET ONE
    public Veiculo getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));
    }

    // UPDATE
    @Transactional
    public Veiculo update(Long id, VeiculoDTO dto) {
        // 1. Busca o veículo como ele está hoje no banco de dados
        Veiculo veiculo = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Veículo não encontrado."));

        // 2. A SUA REGRA DE NEGÓCIO:
        // Se o carro já está bloqueado E o ID do novo dono é diferente do dono atual...
        if ("BLOQUEADO".equalsIgnoreCase(veiculo.getStatus()) &&
                !veiculo.getProprietario().getId().equals(dto.getProprietarioId())) {

            // Barra a operação na hora e avisa o motivo
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Transferência negada! Este veículo está BLOQUEADO e não pode ser transferido.");
        }

        // 3. Se passou pela regra, atualiza os dados normalmente
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setCor(dto.getCor());
        veiculo.setStatus(dto.getStatus()); // Permite mudar o status (ex: de BLOQUEADO para REGULAR)

        // Busca o novo proprietário para fazer o vínculo
        Proprietario novoDono = proprietarioRepository.findById(dto.getProprietarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Novo proprietário não encontrado."));
        veiculo.setProprietario(novoDono);

        return repository.save(veiculo);
    }

    // DELETE
    public void delete(Long id) {
        Veiculo veiculo = getOne(id);
        repository.delete(veiculo);
    }
}