package com.example.speedsystem.service;

import com.example.speedsystem.dto.RegisterRequest;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;

    public Usuario registrar(RegisterRequest req) {

        if (req.getPaswword() == null || req.getPaswword().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setCorreo(req.getCorreo());
        u.setPaswword(passwordEncoder.encode(req.getPaswword()));

        return repo.save(u);
    }

    public Usuario getPorEmail(String correo) {
        return repo.findByCorreo(correo).orElse(null);
    }

    public Usuario actualizar(String correo, Usuario datos) {
        Usuario u = getPorEmail(correo);
        if (u == null)
            return null;

        if (datos.getNombre() != null)
            u.setNombre(datos.getNombre());
        if (datos.getApellido() != null)
            u.setApellido(datos.getApellido());
        if (datos.getCorreo() != null)
            u.setCorreo(datos.getCorreo());
        if (datos.getPaswword() != null)
            u.setPaswword(passwordEncoder.encode(datos.getPaswword()));

        return repo.save(u);
    }
}