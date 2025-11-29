package com.example.speedsystem.config;

import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class UserDetailsConfig {

    private final UsuarioService usuarioService;

    @Bean
    public UserDetailsService userDetailsService() {
        return correo -> {
            Usuario usuario = usuarioService.getPorCorreo(correo);
            if (usuario == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + correo);
            }

            return User.withUsername(usuario.getCorreo())
                       .password(usuario.getPassword())
                       .authorities("USER") // ← autoridad genérica, no rol formal
                       .build();
        };
    }
}
