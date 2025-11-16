package com.example.speedsystem.controller;

import com.example.speedsystem.dto.AuthRequest;
import com.example.speedsystem.dto.RegisterRequest;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.JWTService;
import com.example.speedsystem.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        usuarioService.registrar(request);
        return "Usuario registrado correctamente";
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {
        Usuario u = usuarioService.getPorEmail(request.getEmail());
        if (u == null || !passwordEncoder.matches(request.getPassword(), u.getPaswword())) {
            return "Credenciales inválidas";
        }
        return jwtService.generarToken(u.getCorreo());
    }
}
