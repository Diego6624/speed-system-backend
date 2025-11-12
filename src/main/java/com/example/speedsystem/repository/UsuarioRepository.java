package com.example.speedsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.speedsystem.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
