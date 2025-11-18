package com.example.speedsystem.controller;

import org.springframework.web.bind.annotation.*;

import com.example.speedsystem.entities.Recorrido;
import com.example.speedsystem.service.TrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recorrido")
@RequiredArgsConstructor
public class TrackingController {

    private final TrackingService trackingService;

    @PostMapping("/iniciar/{usuarioId}")
    public Recorrido iniciar(@PathVariable Long usuarioId) {
        return trackingService.iniciarRecorrido(usuarioId);
    }

    @PostMapping("/{id}/tracking")
    public void tracking(@PathVariable Long id, @RequestBody TrackingRequest req) {
        trackingService.registrarPunto(id, req.lat(), req.lng(), req.velocidad());
    }

    @PutMapping("/{id}/finalizar")
    public Recorrido finalizar(@PathVariable Long id) {
        return trackingService.finalizarRecorrido(id);
    }

    public record TrackingRequest(Double lat, Double lng, Double velocidad) {}
}
