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

        if (req.getPassword() == null || req.getPassword().isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        Usuario u = new Usuario();
        u.setNombre(req.getNombre());
        u.setApellido(req.getApellido());
        u.setCorreo(req.getCorreo());
        u.setPassword(passwordEncoder.encode(req.getPassword()));

        return repo.save(u);
    }

    public Usuario getPorCorreo(String correo) {
        return repo.findByCorreo(correo).orElse(null);
    }

    public Usuario actualizar(String correo, Usuario datos) {
        Usuario u = getPorCorreo(correo);
        if (u == null)
            return null;

        if (datos.getNombre() != null)
            u.setNombre(datos.getNombre());
        if (datos.getApellido() != null)
            u.setApellido(datos.getApellido());
        if (datos.getCorreo() != null)
            u.setCorreo(datos.getCorreo());
        if (datos.getPassword() != null)
            u.setPassword(passwordEncoder.encode(datos.getPassword()));

        return repo.save(u);
    }
}