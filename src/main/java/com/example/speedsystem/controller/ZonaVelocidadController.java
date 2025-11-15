package com.example.speedsystem.controller;

import org.springframework.web.bind.annotation.*;

import com.example.speedsystem.service.ZonaVelocidadService;

@RestController
@RequestMapping("/zona")
@CrossOrigin("*")
public class ZonaVelocidadController {

    private final ZonaVelocidadService service;

    public ZonaVelocidadController(ZonaVelocidadService service) {
        this.service = service;
    }

    @GetMapping("/velocidad")
    public VelocidadResponse obtenerVelocidad(@RequestParam double lat, @RequestParam double lon) {
        Integer velocidad = service.obtenerVelocidadMaxima(lat, lon);
        return new VelocidadResponse(velocidad);
    }

    record VelocidadResponse(Integer limiteVelocidad) {}
}