package com.example.speedsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speedsystem.entities.Recorrido;

public interface RecorridoRepository extends JpaRepository<Recorrido, Long>{
    
}
