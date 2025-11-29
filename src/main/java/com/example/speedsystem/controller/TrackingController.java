package com.example.speedsystem.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.entities.Usuario;
import com.example.speedsystem.service.TrackingService;
import com.example.speedsystem.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recorrido")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;
    private final UsuarioService usuarioService;

    @PostMapping("/iniciar")
    public Recorrido iniciar(@AuthenticationPrincipal User user) {
      Usuario usuario = usuarioService.getPorCorreo(user.getUsername());
      return trackingService.iniciarRecorrido(usuario.getId());
    }

    @PostMapping("/{id}/tracking")
    public void tracking(
      @PathVariable Long id,
      @RequestBody TrackingRequest req,
      @AuthenticationPrincipal User user
    ) {
      // Opcional: validar que el recorrido pertenece al usuario del token
      trackingService.registrarPunto(id, req.lat(), req.lng(), req.velocidad());
    }

    @PutMapping("/{id}/finalizar")
    public Recorrido finalizar(
      @PathVariable Long id,
      @AuthenticationPrincipal User user
    ) {
      // Opcional: validar que el recorrido pertenece al usuario del token
      return trackingService.finalizarRecorrido(id);
    }

    public record TrackingRequest(Double lat, Double lng, Double velocidad) {}
}
