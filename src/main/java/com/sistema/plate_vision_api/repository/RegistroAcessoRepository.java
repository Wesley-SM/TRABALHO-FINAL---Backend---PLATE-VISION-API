package com.sistema.plate_vision_api.repository;

import com.sistema.plate_vision_api.model.RegistroAcesso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistroAcessoRepository extends JpaRepository<RegistroAcesso, Long> {
    // JpaRepository já nos dá tudo o que precisamos para o histórico de acessos
}