package com.sistema.plate_vision_api.repository;

import com.sistema.plate_vision_api.model.Proprietario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProprietarioRepository extends JpaRepository<Proprietario, Long> {
    // Método extra útil para buscar proprietários pelo CPF depois
    Optional<Proprietario> findByCpf(String cpf);
    Page<Proprietario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}