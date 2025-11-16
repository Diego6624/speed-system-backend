package com.example.speedsystem.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JWTService {

    private final Key secretKey = Keys.hmacShaKeyFor("MI_LLAVE_SECRETA_SUPER_SEGURA_1234567890".getBytes());

    public String generarToken(String correo) {
        return Jwts.builder()
                .setSubject(correo)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
    
    public String extraerCorreo(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // asegúrate que tu secretKey sea la misma que usas para generar el token
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
