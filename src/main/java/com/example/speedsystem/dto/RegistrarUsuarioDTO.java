package com.example.speedsystem.dto;

public record RegistrarUsuarioDTO(
        String nombre, 
        String apellido, 
        String correo, 
        String password
) {}
