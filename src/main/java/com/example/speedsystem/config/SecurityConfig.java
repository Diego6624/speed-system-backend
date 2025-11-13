package com.example.speedsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desactiva CSRF para poder hacer pruebas POST desde Swagger
            .authorizeHttpRequests()
                .requestMatchers("/api/**").permitAll() // Permitir todas las peticiones a tus endpoints de la API
                .anyRequest().authenticated() // El resto requiere autenticación
            .and()
            .formLogin().disable()
            .httpBasic().disable();

        return http.build();
    }
}
