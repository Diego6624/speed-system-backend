package com.example.speedsystem.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String password;
}