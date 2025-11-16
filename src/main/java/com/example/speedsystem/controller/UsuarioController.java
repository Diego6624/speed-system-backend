package com.example.speedsystem.controller;

import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.JWTService;
import com.example.speedsystem.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {

    private final JWTService jwtService;
    private final UsuarioService usuarioService;

    @GetMapping("/me")
    public Usuario me(@RequestHeader("Authorization") String header) {
        String token = header.replace("Bearer ", "");
        String email = jwtService.obtenerEmail(token);
        return usuarioService.getPorEmail(email);
    }

    @PutMapping("/update")
    public Usuario update(@RequestHeader("Authorization") String header, @RequestBody Usuario datos) {
        String token = header.replace("Bearer ", "");
        String email = jwtService.obtenerEmail(token);
        return usuarioService.actualizar(email, datos);
    }
}
