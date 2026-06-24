package com.sistema.plate_vision_api.repository;

import com.sistema.plate_vision_api.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    // Método fundamental para o seu robô buscar se a placa existe no banco
    Optional<Veiculo> findByPlaca(String placa);
}