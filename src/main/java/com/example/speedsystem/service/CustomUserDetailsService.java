package com.example.speedsystem.service;

import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

// import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        System.out.println("🔎 Buscando usuario por correo: " + username);
        Usuario usuario = usuarioRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return new org.springframework.security.core.userdetails.User(usuario.getCorreo(), usuario.getPassword(),
                new ArrayList<>());
    }
}
