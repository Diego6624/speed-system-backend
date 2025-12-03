package com.example.speedsystem.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.*;

import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.TrackingService;
import com.example.speedsystem.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recorrido")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;
    private final UsuarioService usuarioService;

    @PostMapping("/iniciar")
    public Recorrido iniciar(Principal principal) {
        Usuario usuario = usuarioService.getPorCorreo(principal.getName());
        if (usuario == null) {
            throw new RuntimeException("Usuario no existe en DB: " + principal.getName());
        }
        return trackingService.iniciarRecorrido(usuario.getId());
    }

    @PostMapping("/{id}/tracking")
    public void tracking(
        @PathVariable Long id,
        @RequestBody TrackingRequest req
    ) {
        System.out.println("📍 Punto recibido: " + req.lat() + "," + req.lng() + " vel=" + req.velocidad());
        trackingService.registrarPunto(id, req.lat(), req.lng(), req.velocidad());
    }

    @PutMapping("/{id}/finalizar")
    public Recorrido finalizar(@PathVariable Long id) {
        return trackingService.finalizarRecorrido(id);
    }

    public record TrackingRequest(Double lat, Double lng, Double velocidad) {}
}
