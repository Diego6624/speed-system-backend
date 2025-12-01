package com.example.speedsystem.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.speedsystem.entities.Recorrido;

public interface RecorridoRepository extends JpaRepository<Recorrido, Long> {
    List<Recorrido> findByUsuarioIdOrderByFechaInicioDesc(Long usuarioId);
    List<Recorrido> findByUsuarioIdAndFechaInicioAfter(long usuarioId, LocalDateTime fecha);
    // 🔑 Nuevo método: busca el recorrido activo de un usuario
    Optional<Recorrido> findByUsuarioIdAndActivoTrue(Long usuarioId);
}
