package com.example.speedsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speedsystem.entities.PuntoRecorrido;

public interface PuntoRecorridoRepository extends JpaRepository<PuntoRecorrido, Long> {
    List<PuntoRecorrido> findByRecorridoId(Long recorridoId);
}
